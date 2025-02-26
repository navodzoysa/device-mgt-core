/*
 * Copyright (c) 2018 - 2023, Entgra (Pvt) Ltd. (http://www.entgra.io) All Rights Reserved.
 *
 * Entgra (Pvt) Ltd. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.entgra.device.mgt.core.device.mgt.api.jaxrs.service.impl;

import io.entgra.device.mgt.core.device.mgt.api.jaxrs.service.api.DeviceManagementService;
import io.entgra.device.mgt.core.device.mgt.api.jaxrs.service.impl.util.DeviceMgtAPITestHelper;
import io.entgra.device.mgt.core.device.mgt.api.jaxrs.util.DeviceMgtAPIUtils;
import io.entgra.device.mgt.core.device.mgt.common.Device;
import io.entgra.device.mgt.core.device.mgt.common.DeviceIdentifier;
import io.entgra.device.mgt.core.device.mgt.common.EnrolmentInfo;
import io.entgra.device.mgt.core.device.mgt.common.PaginationRequest;
import io.entgra.device.mgt.core.device.mgt.common.app.mgt.ApplicationManagementException;
import io.entgra.device.mgt.core.device.mgt.common.authorization.DeviceAccessAuthorizationException;
import io.entgra.device.mgt.core.device.mgt.common.authorization.DeviceAccessAuthorizationService;
import io.entgra.device.mgt.core.device.mgt.common.exceptions.DeviceManagementException;
import io.entgra.device.mgt.core.device.mgt.common.exceptions.DeviceTypeNotFoundException;
import io.entgra.device.mgt.core.device.mgt.common.operation.mgt.OperationManagementException;
import io.entgra.device.mgt.core.device.mgt.common.search.SearchContext;
import io.entgra.device.mgt.core.device.mgt.core.app.mgt.ApplicationManagementProviderService;
import io.entgra.device.mgt.core.device.mgt.core.authorization.DeviceAccessAuthorizationServiceImpl;
import io.entgra.device.mgt.core.device.mgt.core.device.details.mgt.DeviceDetailsMgtException;
import io.entgra.device.mgt.core.device.mgt.core.device.details.mgt.DeviceInformationManager;
import io.entgra.device.mgt.core.device.mgt.core.device.details.mgt.impl.DeviceInformationManagerImpl;
import io.entgra.device.mgt.core.device.mgt.core.search.mgt.SearchManagerService;
import io.entgra.device.mgt.core.device.mgt.core.search.mgt.SearchMgtException;
import io.entgra.device.mgt.core.device.mgt.core.search.mgt.impl.SearchManagerServiceImpl;
import io.entgra.device.mgt.core.device.mgt.core.service.DeviceManagementProviderService;
import io.entgra.device.mgt.core.device.mgt.core.service.DeviceManagementProviderServiceImpl;
import io.entgra.device.mgt.core.policy.mgt.common.PolicyManagementException;
import io.entgra.device.mgt.core.policy.mgt.core.PolicyManagerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class includes unit tests for testing the functionality of {@link DeviceManagementServiceImpl}
 */
@PowerMockIgnore({"javax.ws.rs.*", "org.apache.log4j.*", "org.mockito.*"})
@SuppressStaticInitializationFor({"io.entgra.device.mgt.core.device.mgt.api.jaxrs.util.DeviceMgtAPIUtils",
        "org.wso2.carbon.context.CarbonContext", "org.wso2.carbon.user.core.service.RealmService"})
@PrepareForTest({DeviceMgtAPIUtils.class, MultitenantUtils.class, CarbonContext.class, RealmService.class})
public class DeviceManagementServiceImplTest {

    private static final Log log = LogFactory.getLog(DeviceManagementServiceImplTest.class);
    private static final String TEST_DEVICE_IDENTIFIER = "TEST_DEVICE_IDENTIFIER";
    private static final String TEST_DEVICE_TYPE = "TEST-DEVICE-TYPE";
    private static final String TEST_DEVICE_NAME = "TEST-DEVICE";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String TENANT_AWARE_USERNAME = "admin@carbon.super";
    private static final String DEFAULT_ROLE = "admin";
    private static final String DEFAULT_OWNERSHIP = "BYOD";
    private static final List<String> DEFAULT_STATUS_LIST = new ArrayList<>();
    private static final String DEFAULT_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
    private DeviceManagementService deviceManagementService;
    private DeviceAccessAuthorizationService deviceAccessAuthorizationService;
    private DeviceManagementProviderService deviceManagementProviderService;
    private static Device demoDevice;

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    @BeforeClass
    public void init() {
        log.info("Initializing DeviceManagementServiceImpl tests");
        initMocks(this);
        this.deviceManagementProviderService = Mockito
                .mock(DeviceManagementProviderServiceImpl.class, Mockito.RETURNS_MOCKS);
        this.deviceManagementService = new DeviceManagementServiceImpl();
        this.deviceAccessAuthorizationService = Mockito.mock(DeviceAccessAuthorizationServiceImpl.class);
        demoDevice = DeviceMgtAPITestHelper.generateDummyDevice(TEST_DEVICE_TYPE, TEST_DEVICE_IDENTIFIER);
        DEFAULT_STATUS_LIST.add("ACTIVE");
        DEFAULT_STATUS_LIST.add("REMOVED");
    }

    @Test(description = "Testing if the device is enrolled when the device is enrolled.")
    public void testIsEnrolledWhenDeviceIsEnrolled() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService.isEnrolled(Mockito.any(DeviceIdentifier.class)))
                .thenReturn(true);
        Response response = this.deviceManagementService.isEnrolled(TEST_DEVICE_TYPE, UUID.randomUUID().toString());
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing if the device is enrolled when the device is not enrolled.",
            dependsOnMethods = "testIsEnrolledWhenDeviceIsEnrolled")
    public void testIsEnrolledWhenDeviceIsNotEnrolled() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService.isEnrolled(Mockito.any(DeviceIdentifier.class)))
                .thenReturn(false);
        Response response = this.deviceManagementService.isEnrolled(TEST_DEVICE_TYPE, UUID.randomUUID().toString());
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing if the device enrolled api when exception occurred.",
            dependsOnMethods = "testIsEnrolledWhenDeviceIsNotEnrolled")
    public void testIsEnrolledError() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService.isEnrolled(Mockito.any(DeviceIdentifier.class)))
                .thenThrow(new DeviceManagementException());
        Response response = this.deviceManagementService.isEnrolled(TEST_DEVICE_TYPE, UUID.randomUUID().toString());
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing get devices when request exists both name and role.")
    public void testGetDevicesWhenBothNameAndRoleAvailable() {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        Response response = this.deviceManagementService
                .getDevices(TEST_DEVICE_NAME, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 1, 0, null, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(description = "Testing get devices with correct request.")
    public void testGetDevices() {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(MultitenantUtils.class, "getTenantAwareUsername"))
                .toReturn(TENANT_AWARE_USERNAME);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));

        Response response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null,null, DEFAULT_STATUS_LIST, 1, 0, null, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService
                .getDevices(TEST_DEVICE_NAME, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, null, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 1, 0, null, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService
                .getDevices(TEST_DEVICE_NAME, TEST_DEVICE_TYPE, null, null, null, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 1, 0, null, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService
                .getDevices(TEST_DEVICE_NAME, TEST_DEVICE_TYPE, null, null, null, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 1, 0, null, null, true,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing get devices by identifier with correct request.")
    public void testGetDeviceByID() throws DeviceAccessAuthorizationException {
        String ifModifiedSince = new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getAuthenticatedUser"))
                .toReturn(DEFAULT_USERNAME);
        CarbonContext carbonContext = Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS);
        DeviceAccessAuthorizationService deviceAccessAuthorizationService =
                Mockito.mock(DeviceAccessAuthorizationService.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class,
                                              "getDeviceAccessAuthorizationService"))
                .toReturn(deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(carbonContext);
        Mockito.when(carbonContext.getTenantId()).thenReturn(-1234);
        Mockito.when(carbonContext.getUsername()).thenReturn(DEFAULT_USERNAME);
        Mockito.when(deviceAccessAuthorizationService.isUserAuthorized(Mockito.any(DeviceIdentifier.class),
                                                                       Mockito.anyString(), Mockito.any(String[].class))).thenReturn(true);

        Response response = this.deviceManagementService
                .getDeviceByID(TEST_DEVICE_IDENTIFIER, ifModifiedSince,true);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService
                .getDeviceByID(TEST_DEVICE_IDENTIFIER, null,true);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

    }

    @Test(description = "Testing get devices by identifier when unauthorized user.")
    public void testGetDeviceByIDWithErroneousUnauthorizedException()
            throws DeviceAccessAuthorizationException {
        String ifModifiedSince = new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getAuthenticatedUser"))
                .toReturn(DEFAULT_USERNAME);
        CarbonContext carbonContext = Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS);
        DeviceAccessAuthorizationService deviceAccessAuthorizationService =
                Mockito.mock(DeviceAccessAuthorizationService.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class,
                                              "getDeviceAccessAuthorizationService"))
                .toReturn(deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(carbonContext);
        Mockito.when(carbonContext.getTenantId()).thenReturn(-1234);
        Mockito.when(carbonContext.getUsername()).thenReturn(DEFAULT_USERNAME);
        Mockito.when(deviceAccessAuthorizationService.isDeviceAdminUser()).thenReturn(false);

        Response response = this.deviceManagementService
                .getDeviceByID(TEST_DEVICE_IDENTIFIER, ifModifiedSince,true);
        Assert.assertEquals(response.getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());

    }

    @Test(description = "Testing get device when DeviceAccessAuthorizationService is not available",
            expectedExceptions = ExceptionInInitializerError.class)
    public void testGetDeviceByIDWithErroneousDeviceAccessAuthorizationService()
            throws DeviceAccessAuthorizationException {
        String ifModifiedSince = new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getAuthenticatedUser"))
                .toReturn(DEFAULT_USERNAME);
        CarbonContext carbonContext = Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(carbonContext);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));
        Mockito.when(carbonContext.getTenantId()).thenReturn(-1234);
        Mockito.when(carbonContext.getUsername()).thenReturn(DEFAULT_USERNAME);
        Mockito.when(deviceAccessAuthorizationService.isDeviceAdminUser()).thenReturn(true);

        this.deviceManagementService.getDeviceByID(TEST_DEVICE_IDENTIFIER, ifModifiedSince, true);

    }

    @Test(description = "Testing get device when DeviceManagementProviderService is not available",
            expectedExceptions = NoClassDefFoundError.class)
    public void testGetDeviceByIDWithErroneousDeviceManagementProviderService()
            throws DeviceAccessAuthorizationException {
        String ifModifiedSince = new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getAuthenticatedUser"))
                .toReturn(DEFAULT_USERNAME);
        CarbonContext carbonContext = Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS);
        DeviceAccessAuthorizationService deviceAccessAuthorizationService =
                Mockito.mock(DeviceAccessAuthorizationService.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class,
                                              "getDeviceAccessAuthorizationService"))
                .toReturn(deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(carbonContext);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));
        Mockito.when(carbonContext.getTenantId()).thenReturn(-1234);
        Mockito.when(carbonContext.getUsername()).thenReturn(DEFAULT_USERNAME);
        Mockito.when(deviceAccessAuthorizationService.isDeviceAdminUser()).thenReturn(true);

        this.deviceManagementService.getDeviceByID(TEST_DEVICE_IDENTIFIER, ifModifiedSince,true);
    }

    @Test(description = "Testing get devices when DeviceAccessAuthorizationService is not available",
            expectedExceptions = NoClassDefFoundError.class)
    public void testGetDevicesWithErroneousDeviceAccessAuthorizationService()
            throws DeviceAccessAuthorizationException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(deviceAccessAuthorizationService.isDeviceAdminUser()).thenReturn(true);
        deviceManagementService.getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null,
                                           DEFAULT_ROLE, DEFAULT_OWNERSHIP, null,null, DEFAULT_STATUS_LIST, 1,
                                           0, null, null, false, null, 10, 5);
    }

    @Test(description = "Testing get devices when user is the device admin")
    public void testGetDevicesWhenUserIsAdmin() throws DeviceAccessAuthorizationException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(MultitenantUtils.class, "getTenantAwareUsername"))
                .toReturn(TENANT_AWARE_USERNAME);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));
        Mockito.when(deviceAccessAuthorizationService.isDeviceAdminUser()).thenReturn(true);

        Response response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP
                        , null, null, DEFAULT_STATUS_LIST, 1, 0, null, null, false, null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, null, DEFAULT_USERNAME, DEFAULT_ROLE, DEFAULT_OWNERSHIP
                        , null, null, DEFAULT_STATUS_LIST, 1, 0, null, null, false, null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing get devices when user is unauthorized.")
    public void testGetDevicesWhenUserIsUnauthorized() throws Exception {
        PowerMockito.spy(MultitenantUtils.class);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        PowerMockito.doReturn(TENANT_AWARE_USERNAME)
                .when(MultitenantUtils.class, "getTenantAwareUsername", DEFAULT_USERNAME);
        PowerMockito.doReturn("newuser@carbon.super").when(MultitenantUtils.class, "getTenantAwareUsername", "newuser");
        Mockito.when(this.deviceAccessAuthorizationService.isDeviceAdminUser()).thenReturn(false);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));

        Response response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, "newuser", null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 0, 0, null, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
        Mockito.reset(this.deviceAccessAuthorizationService);
    }

    @Test(description = "Testing get devices with IF-Modified-Since")
    public void testGetDevicesWithModifiedSince() {
        String ifModifiedSince = new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(MultitenantUtils.class, "getTenantAwareUsername"))
                .toReturn(TENANT_AWARE_USERNAME);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));

        Response response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 0, 0, null, ifModifiedSince, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_MODIFIED.getStatusCode());
        response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 0, 0, null, ifModifiedSince, true,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_MODIFIED.getStatusCode());
        response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 0, 0, null, "ErrorModifiedSince",
                        false, null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(description = "Testing get devices with Since")
    public void testGetDevicesWithSince() {
        String since = new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(MultitenantUtils.class, "getTenantAwareUsername"))
                .toReturn(TENANT_AWARE_USERNAME);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));

        Response response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null,DEFAULT_STATUS_LIST, 0, 0, since, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null,DEFAULT_STATUS_LIST, 0, 0, since, null, true,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null,DEFAULT_STATUS_LIST, 0, 0, "ErrorSince", null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(description = "Testing get devices when unable to retrieve devices")
    public void testGetDeviceServerErrorWhenGettingDeviceList() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(MultitenantUtils.class, "getTenantAwareUsername"))
                .toReturn(TENANT_AWARE_USERNAME);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));
        Mockito.when(this.deviceManagementProviderService
                .getAllDevices(Mockito.any(PaginationRequest.class), Mockito.anyBoolean()))
                .thenThrow(new DeviceManagementException());

        Response response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 1, 0, null, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing get devices when unable to check if the user is the admin user")
    public void testGetDevicesServerErrorWhenCheckingAdminUser() throws DeviceAccessAuthorizationException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceAccessAuthorizationService"))
                .toReturn(this.deviceAccessAuthorizationService);
        PowerMockito.stub(PowerMockito.method(MultitenantUtils.class, "getTenantAwareUsername"))
                .toReturn(TENANT_AWARE_USERNAME);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getRealmService"))
                .toReturn(Mockito.mock(RealmService.class, Mockito.RETURNS_MOCKS));
        Mockito.when(this.deviceAccessAuthorizationService.isDeviceAdminUser())
                .thenThrow(new DeviceAccessAuthorizationException());

        Response response = this.deviceManagementService
                .getDevices(null, TEST_DEVICE_TYPE, DEFAULT_USERNAME, null, DEFAULT_ROLE, DEFAULT_OWNERSHIP,
                        null, null, DEFAULT_STATUS_LIST, 1, 0,null, null, false,
                        null, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        Mockito.reset(this.deviceAccessAuthorizationService);
    }

    @Test(description = "Testing get devices with correct request")
    public void testGetDeviceTypesByUser() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));

        Response response = this.deviceManagementService.getDeviceByUser(true, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        response = this.deviceManagementService.getDeviceByUser(false, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing get devices with correct request when unable to get devices.")
    public void testGetDeviceTypesByUserException() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        PowerMockito.stub(PowerMockito.method(CarbonContext.class, "getThreadLocalCarbonContext"))
                .toReturn(Mockito.mock(CarbonContext.class, Mockito.RETURNS_MOCKS));
        Mockito.when(this.deviceManagementProviderService.getDevicesOfUser(Mockito.any(PaginationRequest.class)))
                .thenThrow(new DeviceManagementException());

        Response response = this.deviceManagementService.getDeviceByUser(true, 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test(description = "Testing delete device with correct request.")
    public void testDeleteDevice() {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Response response = this.deviceManagementService.deleteDevice(TEST_DEVICE_TYPE, UUID.randomUUID().toString());
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing delete unavailable device.")
    public void testDeleteUnavailableDevice() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService
                .getDevice(Mockito.any(DeviceIdentifier.class), Mockito.anyBoolean())).thenReturn(null);
        Response response = this.deviceManagementService.deleteDevice(TEST_DEVICE_TYPE, UUID.randomUUID().toString());
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing delete device when unable to delete device.")
    public void testDeleteDeviceException() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService.disenrollDevice(Mockito.any(DeviceIdentifier.class)))
                .thenThrow(new DeviceManagementException());
        Response response = this.deviceManagementService.deleteDevice(TEST_DEVICE_TYPE, UUID.randomUUID().toString());
        Assert.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing getting device location")
    public void testGetDeviceLocation() {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceInformationManagerService")).
                toReturn(Mockito.mock(DeviceInformationManagerImpl.class, Mockito.RETURNS_MOCKS));
        Response response = this.deviceManagementService
                .getDeviceLocation(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), null);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing getting device location when unable to retrieve location")
    public void testGetDeviceLocationException() throws DeviceDetailsMgtException {
        DeviceInformationManager deviceInformationManager = Mockito
                .mock(DeviceInformationManagerImpl.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceInformationManagerService")).
                toReturn(deviceInformationManager);
        Mockito.when(deviceInformationManager.getDeviceLocation(Mockito.any(DeviceIdentifier.class)))
                .thenThrow(new DeviceDetailsMgtException());
        Response response = this.deviceManagementService
                .getDeviceLocation(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), null);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test(description = "Testing getting device information")
    public void testGetDeviceInformation() {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceInformationManagerService")).
                toReturn(Mockito.mock(DeviceInformationManagerImpl.class, Mockito.RETURNS_MOCKS));
        Response response = this.deviceManagementService
                .getDeviceInformation(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), null);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing getting device information when unable to retrieve information")
    public void testGetDeviceInformationException() throws DeviceDetailsMgtException {
        DeviceInformationManager deviceInformationManager = Mockito
                .mock(DeviceInformationManagerImpl.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceInformationManagerService")).
                toReturn(deviceInformationManager);
        Mockito.when(deviceInformationManager.getDeviceInfo(Mockito.any(DeviceIdentifier.class)))
                .thenThrow(new DeviceDetailsMgtException());
        Response response = this.deviceManagementService
                .getDeviceInformation(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), null);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test(description = "Testing getting device features")
    public void testGetFeaturesOfDevice() {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Response response = this.deviceManagementService
                .getFeaturesOfDevice(TEST_DEVICE_TYPE, null);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing getting device features when unable to get the feature manager")
    public void testGetFeaturesException() throws DeviceTypeNotFoundException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService.getFeatureManager(Mockito.anyString()))
                .thenThrow(new DeviceTypeNotFoundException());
        Response response = this.deviceManagementService
                .getFeaturesOfDevice(TEST_DEVICE_TYPE, null);
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing search devices")
    public void testSearchDevices() {
        SearchManagerService searchManagerService = Mockito.mock(SearchManagerServiceImpl.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getSearchManagerService"))
                .toReturn(searchManagerService);
        Response response = this.deviceManagementService
                .searchDevices(10, 5, new SearchContext());
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(),
                "Expects to return HTTP 200 when the search is successful");
    }

    @Test(description = "Testing search devices when unable to search devices")
    public void testSearchDevicesException() throws SearchMgtException {
        SearchManagerService searchManagerService = Mockito.mock(SearchManagerServiceImpl.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getSearchManagerService"))
                .toReturn(searchManagerService);
        Mockito.when(searchManagerService.search(Mockito.any(SearchContext.class))).thenThrow(new SearchMgtException());
        Response response = this.deviceManagementService
                .searchDevices(10, 5, new SearchContext());
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Expects HTTP 500 when an exception occurred while searching the device");
    }

    @Test(description = "Testing getting installed applications of a device")
    public void testGetInstalledApplications() {
        ApplicationManagementProviderService applicationManagementProviderService = Mockito
                .mock(ApplicationManagementProviderService.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getAppManagementService"))
                .toReturn(applicationManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Response response = this.deviceManagementService
                .getInstalledApplications(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), "", 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(),
                "Expects to return HTTP 200 when the application list is retrieved successfully.");
    }

    @Test(description = "Testing getting installed applications of a device when unable to fetch applications")
    public void testGetInstalledApplicationsException() throws ApplicationManagementException {
        ApplicationManagementProviderService applicationManagementProviderService = Mockito
                .mock(ApplicationManagementProviderService.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getAppManagementService"))
                .toReturn(applicationManagementProviderService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(
                applicationManagementProviderService.getApplicationListForDevice(Mockito.any(Device.class)))
                .thenThrow(new ApplicationManagementException());
        Response response = this.deviceManagementService
                .getInstalledApplications(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), "", 10, 5);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Expects HTTP 500 when an exception occurred while retrieving application list of the device");
    }

    @Test(description = "Testing getting operation list of a device")
    public void testGetDeviceOperations() {
        List<String> operationCodes = new ArrayList<>();
        List<String> statusCodes = new ArrayList<>();
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Response response = this.deviceManagementService
                .getDeviceOperations(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), "", 10, 5, DEFAULT_USERNAME,
                        DEFAULT_OWNERSHIP, null, null, null, null, operationCodes, statusCodes);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(),
                "Expects to return HTTP 200 when the operation is retrieved successfully.");
    }

    @Test(description = "Testing getting operation list of a device when unable to retrieve operations")
    public void testGetDeviceOperationsException() throws OperationManagementException {
        List<String> operationCodes = new ArrayList<>();
        List<String> statusCodes = new ArrayList<>();
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService.getOperations(Mockito.any(DeviceIdentifier.class),
                Mockito.any(PaginationRequest.class))).thenThrow(new OperationManagementException());
        Response response = this.deviceManagementService
                .getDeviceOperations(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), "", 10, 5, DEFAULT_USERNAME,
                        DEFAULT_OWNERSHIP, null, null, null, null, operationCodes, statusCodes);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Expects to return HTTP 500 when an exception occurred while retrieving operation list of the device");
    }

    @Test(description = "Testing getting effective policy of a device")
    public void testGetEffectivePolicyOfDevice() throws PolicyManagementException {
        PolicyManagerService policyManagerService = Mockito.mock(PolicyManagerService.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getPolicyManagementService"))
                .toReturn(policyManagerService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Response response = this.deviceManagementService
                .getEffectivePolicyOfDevice(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), null);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode(),
                "Expects to return HTTP 200 when retrieving effective policy is successful");
    }

    @Test(description = "Testing getting effective policy of a device when unable to retrieve effective policy")
    public void testGetEffectivePolicyOfDeviceException() throws PolicyManagementException {
        PolicyManagerService policyManagerService = Mockito.mock(PolicyManagerService.class, Mockito.RETURNS_MOCKS);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getPolicyManagementService"))
                .toReturn(policyManagerService);
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(policyManagerService.getAppliedPolicyToDevice(Mockito.any(Device.class)))
                .thenThrow(new PolicyManagementException());
        Response response = this.deviceManagementService
                .getEffectivePolicyOfDevice(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), null);
        Assert.assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Expects to return HTTP 500 when an exception occurred while getting effective policy of the device");
    }

    @Test(description = "Testing changing device status")
    public void testChangeDeviceStatus() {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Response response = this.deviceManagementService
                .changeDeviceStatus(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), EnrolmentInfo.Status.INACTIVE);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test(description = "Testing changing device status when device does not exist")
    public void testChangeDeviceStatusWhenDeviceNotExists() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService
                .getDevice(Mockito.any(DeviceIdentifier.class), Mockito.anyBoolean())).thenReturn(null);
        Response response = this.deviceManagementService
                .changeDeviceStatus(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), EnrolmentInfo.Status.INACTIVE);
        Assert.assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing changing device status when device cannot be retrieved")
    public void testChangeDeviceStatusException() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService
                .getDevice(Mockito.any(DeviceIdentifier.class), Mockito.anyBoolean()))
                .thenThrow(new DeviceManagementException());
        Response response = this.deviceManagementService
                .changeDeviceStatus(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), EnrolmentInfo.Status.ACTIVE);
        Assert.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }

    @Test(description = "Testing changing device status when unable to change device status")
    public void testChangeDeviceStatusWhenUnableToChangeStatus() throws DeviceManagementException {
        PowerMockito.stub(PowerMockito.method(DeviceMgtAPIUtils.class, "getDeviceManagementService"))
                .toReturn(this.deviceManagementProviderService);
        Mockito.when(this.deviceManagementProviderService
                .changeDeviceStatus(Mockito.any(DeviceIdentifier.class), Mockito.any()))
                .thenThrow(new DeviceManagementException());
        Response response = this.deviceManagementService
                .changeDeviceStatus(TEST_DEVICE_TYPE, UUID.randomUUID().toString(), EnrolmentInfo.Status.ACTIVE);
        Assert.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        Mockito.reset(this.deviceManagementProviderService);
    }
}
