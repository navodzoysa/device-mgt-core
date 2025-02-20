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
package io.entgra.device.mgt.core.device.mgt.core.dao;

import io.entgra.device.mgt.core.device.mgt.common.Device;
import io.entgra.device.mgt.core.device.mgt.common.DeviceIdentifier;
import io.entgra.device.mgt.core.device.mgt.common.EnrolmentInfo;
import io.entgra.device.mgt.core.device.mgt.core.common.BaseDeviceManagementTest;
import io.entgra.device.mgt.core.device.mgt.core.common.TestDataHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class EnrolmentPersistenceTests extends BaseDeviceManagementTest {

    private static final Log log = LogFactory.getLog(EnrolmentPersistenceTests.class);
    EnrollmentDAO enrollmentDAO;

    @BeforeClass
    @Override
    public void init() throws Exception {
        this.initDataSource();
        enrollmentDAO = DeviceManagementDAOFactory.getEnrollmentDAO();
    }

    @Test
    public void testAddEnrolment() {
        int deviceId = -1;
        String owner = "admin";

        /* Initializing source enrolment configuration bean to be tested */
        EnrolmentInfo source =
                new EnrolmentInfo(owner, EnrolmentInfo.OwnerShip.BYOD,
                        EnrolmentInfo.Status.ACTIVE);

        /* Adding dummy enrolment configuration to the device management metadata store */
        try {
            DeviceManagementDAOFactory.openConnection();
            Device device = TestDataHolder.generateDummyDeviceData(TestDataHolder.TEST_DEVICE_TYPE);
            DeviceDAO deviceDAO = DeviceManagementDAOFactory.getDeviceDAO();
            deviceId = deviceDAO.addDevice(TestDataHolder.initialTestDeviceType.getId(), device, TestDataHolder.SUPER_TENANT_ID);
            device.setId(deviceId);
            enrollmentDAO.addEnrollment(deviceId, new DeviceIdentifier(device.getDeviceIdentifier(), device.getType()),
                    source, TestDataHolder.SUPER_TENANT_ID);
        } catch (DeviceManagementDAOException | SQLException e) {
            log.error("Error occurred while adding enrollment", e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
        /* Retrieving the enrolment associated with the given deviceId and owner */
        if (deviceId != -1) {
            EnrolmentInfo target = null;
            try {
                target = this.getEnrolmentConfig(deviceId, owner, TestDataHolder.SUPER_TENANT_ID);
            } catch (DeviceManagementDAOException e) {
                String msg = "Error occurred while retrieving application info";
                log.error(msg, e);
                Assert.fail(msg, e);
            }

            Assert.assertEquals(target, source, "Enrolment configuration added is not as same as what's retrieved");
        }
    }

    private EnrolmentInfo getEnrolmentConfig(int deviceId, String currentOwner,
                                             int tenantId) throws DeviceManagementDAOException {
        EnrolmentInfo enrolmentInfo = null;
        try {
            DeviceManagementDAOFactory.openConnection();
            enrolmentInfo = enrollmentDAO.getEnrollment(deviceId, currentOwner, tenantId);
        } catch (SQLException e) {
            log.error("Error occurred while retrieving enrolment corresponding to device id '" + deviceId + "'", e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
        return enrolmentInfo;
    }
}
