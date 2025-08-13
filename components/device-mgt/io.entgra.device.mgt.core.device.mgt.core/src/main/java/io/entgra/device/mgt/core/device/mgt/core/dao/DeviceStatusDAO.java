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

import io.entgra.device.mgt.core.device.mgt.common.EnrolmentInfo.Status;
import io.entgra.device.mgt.core.device.mgt.common.type.mgt.DeviceStatus;

import java.util.Date;
import java.util.List;

public interface DeviceStatusDAO {

//    boolean updateStatus(int deviceId, Status status, int tenantId) throws DeviceManagementDAOException;

//    boolean updateStatus(int enrolmentId, Status status) throws DeviceManagementDAOException;

    List<DeviceStatus> getStatus(int deviceId, int tenantId) throws DeviceManagementDAOException;

    List<DeviceStatus> getStatus(int deviceId, int tenantId, Date fromDate, Date toDate, boolean billingStatus) throws DeviceManagementDAOException;

    List<DeviceStatus> getStatus(int enrolmentId) throws DeviceManagementDAOException;

    List<DeviceStatus> getStatus(int enrolmentId, Date fromDate, Date toDate) throws DeviceManagementDAOException;

    /**
     * Get the specific {@link Status} of a device between the given dates.
     * @param deviceId Device ID
     * @param fromDate from date
     * @param toDate to date
     * @param billingStatus whether the result be ordered in descending order
     * @param status {@link Status} to be queried. If null, all statuses will be returned.
     * @return List of {@link DeviceStatus} objects. If no status is found, an empty list will be returned.
     * @throws DeviceManagementDAOException if an error occurred while querying the database.
     */
    List<DeviceStatus> getStatus(int deviceId, Date fromDate, Date toDate, boolean billingStatus,
                                 Status status) throws DeviceManagementDAOException;

}
