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

package io.entgra.device.mgt.core.application.mgt.common.services;

import io.entgra.device.mgt.core.application.mgt.common.config.LifecycleState;
import io.entgra.device.mgt.core.application.mgt.common.exception.ApplicationManagementException;
import io.entgra.device.mgt.core.application.mgt.common.exception.LifecycleManagementException;

import java.io.InputStream;
import java.util.Map;

public interface AppmDataHandler {

    Map<String, LifecycleState> getLifecycleConfiguration() throws LifecycleManagementException;

    /**
     * Get the Input Stream of the Artifact
     * @param tenantId Tenant Id
     * @param appHashValue Hash Value of the application
     * @param folderName Folder Name
     * @param artifactName Artifact Name
     * @return {@link InputStream}
     * @throws ApplicationManagementException if error occurred while getting Input Stream of the artifact
     */
    InputStream getArtifactStream(int tenantId, String appHashValue, String folderName, String artifactName)
            throws ApplicationManagementException;

    /**
     * Get agent apk
     *
     * @param tenantDomain   tenant domain name
     * @param deviceType device type name
     * @return {@link InputStream}
     * @throws ApplicationManagementException throws if an error occurs when accessing the file.
     */
    InputStream getAgentStream(String tenantDomain, String deviceType) throws ApplicationManagementException;
}
