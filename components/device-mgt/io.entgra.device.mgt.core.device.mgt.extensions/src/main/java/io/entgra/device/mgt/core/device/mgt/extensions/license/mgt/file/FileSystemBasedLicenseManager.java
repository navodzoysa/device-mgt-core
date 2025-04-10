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
package io.entgra.device.mgt.core.device.mgt.extensions.license.mgt.file;

import io.entgra.device.mgt.core.device.mgt.common.license.mgt.License;
import io.entgra.device.mgt.core.device.mgt.common.license.mgt.LicenseManagementException;
import io.entgra.device.mgt.core.device.mgt.common.license.mgt.LicenseManager;
import org.wso2.carbon.utils.CarbonUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class FileSystemBasedLicenseManager implements LicenseManager {

    private static final String PATH_MOBILE_PLUGIN_CONF_DIR =
            CarbonUtils.getEtcCarbonConfigDirPath() + File.separator + "device-mgt-plugins";

    @Override
    public License getLicense(String deviceType, String languageCode) throws LicenseManagementException {
        try {
            String licenseConfigPath =
                    PATH_MOBILE_PLUGIN_CONF_DIR + File.separator + deviceType + File.separator + "license.xml";
            File licenseConfig = new File(licenseConfigPath);

            if (!licenseConfig.exists()) {
                throw new LicenseManagementException(
                        "License file not found in the path for the device type " + deviceType);
            }
            JAXBContext context = JAXBContext.newInstance(License.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (License) unmarshaller.unmarshal(licenseConfig);

        } catch (JAXBException e) {
            throw new LicenseManagementException("Error occurred while un-marshalling license configuration " +
                    "used for '" + deviceType + "' platform from file system", e);
        }
    }

    @Override
    public void addLicense(String deviceType, License license) throws LicenseManagementException {
        throw new UnsupportedOperationException("'addLicense' method is not supported in " +
                "FileSystemBasedLicenseManager");
    }

}
