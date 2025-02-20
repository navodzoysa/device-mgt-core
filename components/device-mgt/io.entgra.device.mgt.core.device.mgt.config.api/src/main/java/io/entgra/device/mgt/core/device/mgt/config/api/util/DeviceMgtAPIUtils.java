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

package io.entgra.device.mgt.core.device.mgt.config.api.util;

import io.entgra.device.mgt.core.device.mgt.common.spi.OTPManagementService;
import io.entgra.device.mgt.core.device.mgt.core.service.DeviceManagementProviderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * MDMAPIUtils class provides utility function used by CDM REST-API classes.
 */
public class DeviceMgtAPIUtils {

    private static final Log log = LogFactory.getLog(DeviceMgtAPIUtils.class);

    private static DeviceManagementProviderService deviceManagementProviderService = null;
    private static RealmService realmService = null;

    private static OTPManagementService otpManagementService = null;

    public static DeviceManagementProviderService getDeviceManagementService() {
        if (deviceManagementProviderService == null) {
            PrivilegedCarbonContext ctx = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            deviceManagementProviderService =
                    (DeviceManagementProviderService) ctx.getOSGiService(DeviceManagementProviderService.class, null);
            if (deviceManagementProviderService == null) {
                String msg = "Device Management provider service has not initialized.";
                log.error(msg);
                throw new IllegalStateException(msg);
            }
        }
        return deviceManagementProviderService;
    }

    public static OTPManagementService getOtpManagementService() {
        if (otpManagementService == null) {
            PrivilegedCarbonContext ctx = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            otpManagementService = (OTPManagementService) ctx.getOSGiService(OTPManagementService.class, null);
            if (otpManagementService == null) {
                String msg = "OTP Management Service has not initialized.";
                log.error(msg);
                throw new IllegalStateException(msg);
            }
        }
        return otpManagementService;
    }

    public static RealmService getRealmService() {
        if (realmService == null) {
            PrivilegedCarbonContext ctx = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            realmService =
                    (RealmService) ctx.getOSGiService(RealmService.class, null);
            if (realmService == null) {
                String msg = "Realm service has not initialized.";
                log.error(msg);
                throw new IllegalStateException(msg);
            }
        }
        return realmService;
    }

}
