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

package io.entgra.device.mgt.core.device.mgt.core.dao.impl.group;

import io.entgra.device.mgt.core.device.mgt.common.Device;
import io.entgra.device.mgt.core.device.mgt.common.GroupPaginationRequest;
import io.entgra.device.mgt.core.device.mgt.common.group.mgt.DeviceGroup;
import io.entgra.device.mgt.core.device.mgt.core.dao.GroupManagementDAOException;
import io.entgra.device.mgt.core.device.mgt.core.dao.GroupManagementDAOFactory;
import io.entgra.device.mgt.core.device.mgt.core.dao.impl.AbstractGroupDAOImpl;
import io.entgra.device.mgt.core.device.mgt.core.dao.util.DeviceManagementDAOUtil;
import io.entgra.device.mgt.core.device.mgt.core.dao.util.GroupManagementDAOUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents implementation of GroupDAO
 */
public class SQLServerGroupDAOImpl extends AbstractGroupDAOImpl {

    @Override
    public List<DeviceGroup> getGroups(GroupPaginationRequest request, int tenantId)
            throws GroupManagementDAOException {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<DeviceGroup> deviceGroupList = null;

        String groupName = request.getGroupName();
        boolean hasGroupName = false;
        String owner = request.getOwner();
        boolean hasOwner = false;
        String status = request.getStatus();
        boolean hasStatus = false;
        boolean hasLimit = request.getRowCount() != 0;

        try {
            Connection conn = GroupManagementDAOFactory.getConnection();
            String sql = "SELECT ID, DESCRIPTION, GROUP_NAME, OWNER, STATUS, PARENT_PATH, PARENT_GROUP_ID FROM DM_GROUP "
                    + "WHERE TENANT_ID = ?";
            if (groupName != null && !groupName.isEmpty()) {
                sql += " AND GROUP_NAME LIKE ?";
                hasGroupName = true;
            }
            if (owner != null && !owner.isEmpty()) {
                sql += " AND OWNER LIKE ?";
                hasOwner = true;
            }
            if (status != null && !status.isEmpty()) {
                sql += " AND STATUS = ?";
                hasStatus = true;
            }
            if (StringUtils.isNotBlank(request.getParentPath())) {
                sql += " AND PARENT_PATH LIKE ?";
            }
            if (hasLimit) {
                sql += " ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            }

            int paramIndex = 1;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(paramIndex++, tenantId);
            if (hasGroupName) {
                stmt.setString(paramIndex++, groupName + "%");
            }
            if (hasOwner) {
                stmt.setString(paramIndex++, owner + "%");
            }
            if (hasStatus) {
                stmt.setString(paramIndex++, status.toUpperCase());
            }
            if (StringUtils.isNotBlank(request.getParentPath())) {
                stmt.setString(paramIndex++, request.getParentPath());
            }
            if (hasLimit) {
                stmt.setInt(paramIndex++, request.getStartIndex());
                stmt.setInt(paramIndex, request.getRowCount());
            }
            resultSet = stmt.executeQuery();
            deviceGroupList = new ArrayList<>();
            while (resultSet.next()) {
                deviceGroupList.add(GroupManagementDAOUtil.loadGroup(resultSet));
            }
        } catch (SQLException e) {
            throw new GroupManagementDAOException("Error occurred while listing all groups in tenant: " + tenantId, e);
        } finally {
            GroupManagementDAOUtil.cleanupResources(stmt, resultSet);
        }
        return deviceGroupList;
    }

    @Override
    public List<DeviceGroup> getGroups(GroupPaginationRequest request, List<Integer> deviceGroupIds,
                                       int tenantId) throws GroupManagementDAOException {
        int deviceGroupIdsCount = deviceGroupIds.size();
        if (deviceGroupIdsCount == 0) {
            return new ArrayList<>();
        }
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<DeviceGroup> deviceGroupList = null;

        String groupName = request.getGroupName();
        boolean hasGroupName = false;
        String owner = request.getOwner();
        boolean hasOwner = false;
        boolean hasLimit = request.getRowCount() != 0;

        try {
            Connection conn = GroupManagementDAOFactory.getConnection();
            String sql = "SELECT ID, DESCRIPTION, GROUP_NAME, OWNER, STATUS, PARENT_PATH, PARENT_GROUP_ID FROM DM_GROUP "
                    + "WHERE TENANT_ID = ?";
            if (groupName != null && !groupName.isEmpty()) {
                sql += " AND GROUP_NAME LIKE ?";
                hasGroupName = true;
            }
            if (owner != null && !owner.isEmpty()) {
                sql += " AND OWNER LIKE ?";
                hasOwner = true;
            }
            if (StringUtils.isNotBlank(request.getParentPath())) {
                sql += " AND PARENT_PATH LIKE ?";
            }
            sql += " AND ID IN (";
            for (int i = 0; i < deviceGroupIdsCount; i++) {
                sql += (deviceGroupIdsCount - 1 != i) ? "?," : "?";
            }
            sql += ")";
            if (hasLimit) {
                sql += " ORDER BY ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            }

            int paramIndex = 1;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(paramIndex++, tenantId);
            if (hasGroupName) {
                stmt.setString(paramIndex++, groupName + "%");
            }
            if (hasOwner) {
                stmt.setString(paramIndex++, owner + "%");
            }
            if (StringUtils.isNotBlank(request.getParentPath())) {
                stmt.setString(paramIndex++, request.getParentPath());
            }
            for (Integer deviceGroupId : deviceGroupIds) {
                stmt.setInt(paramIndex++, deviceGroupId);
            }
            if (hasLimit) {
                stmt.setInt(paramIndex++, request.getStartIndex());
                stmt.setInt(paramIndex, request.getRowCount());
            }
            resultSet = stmt.executeQuery();
            deviceGroupList = new ArrayList<>();
            while (resultSet.next()) {
                deviceGroupList.add(GroupManagementDAOUtil.loadGroup(resultSet));
            }
        } catch (SQLException e) {
            throw new GroupManagementDAOException("Error occurred while listing all groups in tenant: " + tenantId, e);
        } finally {
            GroupManagementDAOUtil.cleanupResources(stmt, resultSet);
        }
        return deviceGroupList;
    }

    @Override
    public List<Device> getDevices(int groupId, int startIndex, int rowCount, int tenantId)
            throws GroupManagementDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Device> devices = null;
        try {
            conn = GroupManagementDAOFactory.getConnection();
            String sql = "SELECT d1.DEVICE_ID, d1.DESCRIPTION, d1.NAME AS DEVICE_NAME, e.DEVICE_TYPE, " +
                    "d1.DEVICE_IDENTIFICATION, d1.LAST_UPDATED_TIMESTAMP, e.OWNER, e.OWNERSHIP, e.STATUS, " +
                    "e.IS_TRANSFERRED, e.DATE_OF_LAST_UPDATE, " +
                    "e.DATE_OF_ENROLMENT, e.ID AS ENROLMENT_ID FROM DM_ENROLMENT e, " +
                    "(SELECT gd.DEVICE_ID, gd.DESCRIPTION, gd.NAME, gd.DEVICE_IDENTIFICATION, gd.LAST_UPDATED_TIMESTAMP " +
                    "FROM " +
                    "(SELECT d.ID AS DEVICE_ID, d.DESCRIPTION, d.NAME, d.DEVICE_IDENTIFICATION, d.LAST_UPDATED_TIMESTAMP FROM" +
                    " DM_DEVICE d, (" +
                    "SELECT dgm.DEVICE_ID FROM DM_DEVICE_GROUP_MAP dgm WHERE dgm.GROUP_ID = ?) dgm1 " +
                    "WHERE d.ID = dgm1.DEVICE_ID AND d.TENANT_ID = ?) gd) d1 " +
                    "WHERE d1.DEVICE_ID = e.DEVICE_ID AND TENANT_ID = ? ORDER BY d1.DEVICE_ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, groupId);
            stmt.setInt(2, tenantId);
            stmt.setInt(3, tenantId);
            //noinspection JpaQueryApiInspection
            stmt.setInt(4, startIndex);
            //noinspection JpaQueryApiInspection
            stmt.setInt(5, rowCount);
            rs = stmt.executeQuery();
            devices = new ArrayList<>();
            while (rs.next()) {
                Device device = DeviceManagementDAOUtil.loadDevice(rs);
                devices.add(device);
            }
        } catch (SQLException e) {
            throw new GroupManagementDAOException("Error occurred while retrieving information of all " +
                    "registered devices", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, rs);
        }
        return devices;
    }
}
