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
package io.entgra.device.mgt.core.device.mgt.extensions.device.type.template;

import io.entgra.device.mgt.core.device.mgt.common.DeviceManager;
import io.entgra.device.mgt.core.device.mgt.common.DeviceStatusTaskPluginConfig;
import io.entgra.device.mgt.core.device.mgt.common.InitialOperationConfig;
import io.entgra.device.mgt.core.device.mgt.common.MonitoringOperation;
import io.entgra.device.mgt.core.device.mgt.common.OperationMonitoringTaskConfig;
import io.entgra.device.mgt.core.device.mgt.common.ProvisioningConfig;
import io.entgra.device.mgt.core.device.mgt.common.StartupOperationConfig;
import io.entgra.device.mgt.core.device.mgt.common.type.mgt.DeviceTypeMetaDetails;
import io.entgra.device.mgt.core.device.mgt.common.exceptions.DeviceManagementException;
import io.entgra.device.mgt.core.device.mgt.common.app.mgt.ApplicationManager;
import io.entgra.device.mgt.core.device.mgt.common.configuration.mgt.ConfigurationEntry;
import io.entgra.device.mgt.core.device.mgt.common.configuration.mgt.PlatformConfiguration;
import io.entgra.device.mgt.core.device.mgt.common.general.GeneralConfig;
import io.entgra.device.mgt.core.device.mgt.common.invitation.mgt.DeviceEnrollmentInvitationDetails;
import io.entgra.device.mgt.core.device.mgt.common.license.mgt.License;
import io.entgra.device.mgt.core.device.mgt.common.policy.mgt.PolicyMonitoringManager;
import io.entgra.device.mgt.core.device.mgt.common.pull.notification.PullNotificationSubscriber;
import io.entgra.device.mgt.core.device.mgt.common.push.notification.PushNotificationConfig;
import io.entgra.device.mgt.core.device.mgt.common.spi.DeviceManagementService;
import io.entgra.device.mgt.core.device.mgt.common.type.mgt.DeviceTypeMetaDefinition;
import io.entgra.device.mgt.core.device.mgt.common.type.mgt.DeviceTypePlatformDetails;
import io.entgra.device.mgt.core.device.mgt.extensions.device.type.template.config.Feature;
import io.entgra.device.mgt.core.device.mgt.extensions.device.type.template.config.*;
import io.entgra.device.mgt.core.device.mgt.extensions.device.type.template.policy.mgt.DefaultPolicyMonitoringManager;
import io.entgra.device.mgt.core.device.mgt.extensions.device.type.template.pull.notification.PullNotificationSubscriberLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the template for device type manager service. This will create and instance of device management service
 * through the configuration file.
 */
public class DeviceTypeManagerService implements DeviceManagementService {

    private static final Log log = LogFactory.getLog(DeviceTypeManagerService.class);
    private static final String NOTIFIER_PROPERTY = "notifierType";
    private static final String NOTIFIER_TYPE_LOCAL = "LOCAL";

    private final DeviceManager deviceManager;
    private PushNotificationConfig pushNotificationConfig;
    private ProvisioningConfig provisioningConfig;
    private String type;
    private final OperationMonitoringTaskConfig operationMonitoringConfigs;
    private List<MonitoringOperation> monitoringOperations = new ArrayList<>();
    private PolicyMonitoringManager policyMonitoringManager;
    private final InitialOperationConfig initialOperationConfig;
    private StartupOperationConfig startupOperationConfig;
    private License licenseConfig;
    private PullNotificationSubscriber pullNotificationSubscriber;
    private final DeviceStatusTaskPluginConfig deviceStatusTaskPluginConfig;
    private DeviceTypePlatformDetails deviceTypePlatformDetails;
    private DeviceEnrollmentInvitationDetails deviceEnrollmentInvitationDetails;
    private GeneralConfig generalConfig;
    private DeviceTypeMetaDefinition deviceTypeMetaDefinition;
    private boolean isRegistryBasedConfigs = false;
    private boolean isScheduled = false;
    private String notifierType;

    public DeviceTypeManagerService(DeviceTypeConfigIdentifier deviceTypeConfigIdentifier,
                                    DeviceTypeConfiguration deviceTypeConfiguration) {
        this.setProvisioningConfig(deviceTypeConfigIdentifier.getTenantDomain(), deviceTypeConfiguration);
        this.deviceManager = new DeviceTypeManager(deviceTypeConfigIdentifier, deviceTypeConfiguration);
        this.setType(deviceTypeConfiguration.getName());
        this.populatePushNotificationConfig(deviceTypeConfiguration.getPushNotificationProvider());
        this.operationMonitoringConfigs = new OperationMonitoringTaskConfig();
        this.setOperationMonitoringConfig(deviceTypeConfiguration);
        this.initialOperationConfig = new InitialOperationConfig();
        this.setInitialOperationConfig(deviceTypeConfiguration);
        this.startupOperationConfig = new StartupOperationConfig();
        this.setStartupOperationConfig(deviceTypeConfiguration);
        this.deviceStatusTaskPluginConfig = new DeviceStatusTaskPluginConfig();
        this.deviceTypePlatformDetails = new DeviceTypePlatformDetails();
        this.setDeviceTypePlatformDetails(deviceTypeConfiguration);
        this.setDeviceStatusTaskPluginConfig(deviceTypeConfiguration.getDeviceStatusTaskConfiguration());
        this.setPolicyMonitoringManager(deviceTypeConfiguration.getPolicyMonitoring());
        this.setPullNotificationSubscriber(deviceTypeConfiguration.getPullNotificationSubscriberConfig());
        this.setGeneralConfig(deviceTypeConfiguration);
        this.deviceEnrollmentInvitationDetails = new DeviceEnrollmentInvitationDetails();
        this.setDeviceEnrollmentInvitationDetails(deviceTypeConfiguration);
        this.licenseConfig = new License();
        this.setLicenseConfig(deviceTypeConfiguration);
        this.deviceTypeMetaDefinition = new DeviceTypeMetaDefinition();
        this.setDeviceTypeMetaDefinition(deviceTypeConfiguration);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public OperationMonitoringTaskConfig getOperationMonitoringConfig() {
        return operationMonitoringConfigs;
    }

    private void setOperationMonitoringConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        //Read the config file and take the list of operations there in the config
        TaskConfiguration taskConfiguration = deviceTypeConfiguration.getTaskConfiguration();
        if (taskConfiguration != null) {
            operationMonitoringConfigs.setEnabled(taskConfiguration.isEnabled());
            operationMonitoringConfigs.setFrequency(taskConfiguration.getFrequency());
            List<TaskConfiguration.Operation> ops = taskConfiguration.getOperations();
            if (ops != null && !ops.isEmpty()) {
                monitoringOperations = new ArrayList<>();
                for (TaskConfiguration.Operation op : ops) {
                    MonitoringOperation monitoringOperation = new MonitoringOperation();
                    monitoringOperation.setTaskName(op.getOperationName());
                    monitoringOperation.setRecurrentTimes(op.getRecurrency());
                    monitoringOperation.setResponsePersistence(op.getResponsePersistence());
                    monitoringOperations.add(monitoringOperation);
                }
            }
            operationMonitoringConfigs.setMonitoringOperation(monitoringOperations);
        }
    }

    @Override
    public void init() throws DeviceManagementException {
    }

    private void populatePushNotificationConfig(PushNotificationProvider pushNotificationProvider) {
        if (pushNotificationProvider != null) {
            if (pushNotificationProvider.isFileBasedProperties()) {
                isRegistryBasedConfigs = false;
                Map<String, String> staticProps = new HashMap<>();
                ConfigProperties configProperties = pushNotificationProvider.getConfigProperties();
                if (configProperties != null) {
                    List<Property> properties = configProperties.getProperty();
                    if (properties != null && !properties.isEmpty()) {
                        for (Property property : properties) {
                            staticProps.put(property.getName(), property.getValue());
                        }
                    }
                }
                pushNotificationConfig = new PushNotificationConfig(pushNotificationProvider.getType(),
                        pushNotificationProvider.isScheduled(), staticProps);
            } else {
                isRegistryBasedConfigs = true;
                isScheduled = pushNotificationProvider.isScheduled();
                notifierType = pushNotificationProvider.getType();
                refreshPlatformConfigurations();
            }
        }
    }

    private void refreshPlatformConfigurations() {
        //Build up push notification configs to use with push notification provider.
        try {
            PlatformConfiguration deviceTypeConfig = deviceManager.getConfiguration();
            if (deviceTypeConfig != null) {
                List<ConfigurationEntry> configuration = deviceTypeConfig.getConfiguration();
                if (configuration != null && !configuration.isEmpty()) {
                    Map<String, String> properties = this.getConfigProperty(configuration);
                    String notifierValue = properties.get(NOTIFIER_PROPERTY);
                    String enabledNotifierType = notifierType;
                    //In registry we are keeping local notifier as value "1". Other notifiers will have
                    // a number grater than 1.
                    if (notifierValue != null && notifierValue.equals("1")) {
                        enabledNotifierType = NOTIFIER_TYPE_LOCAL;
                    }
                    pushNotificationConfig = new PushNotificationConfig(enabledNotifierType, isScheduled, properties);
                }
            } else {
                if (notifierType != null) {
                    if (notifierType.equals("1")) {
                        pushNotificationConfig = new PushNotificationConfig(NOTIFIER_TYPE_LOCAL, isScheduled, null);
                    } else {
                        pushNotificationConfig = new PushNotificationConfig(notifierType, isScheduled, null);
                    }
                }
            }
        } catch (DeviceManagementException e) {
            log.error("Unable to get the " + type + " platform configuration from registry.", e);
        }
    }

    @Override
    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    @Override
    public ApplicationManager getApplicationManager() {
        return null;
    }

    @Override
    public ProvisioningConfig getProvisioningConfig() {
        return provisioningConfig;
    }

    @Override
    public PushNotificationConfig getPushNotificationConfig() {
        //We only need to update push notification configs if this device type uses registry based configs.
        if (isRegistryBasedConfigs) {
            refreshPlatformConfigurations();
        }
        return pushNotificationConfig;
    }

    @Override
    public PolicyMonitoringManager getPolicyMonitoringManager() {
        return policyMonitoringManager;
    }

    @Override
    public InitialOperationConfig getInitialOperationConfig() {
        return initialOperationConfig;
    }

    @Override
    public StartupOperationConfig getStartupOperationConfig() {
        return startupOperationConfig;
    }

    @Override
    public PullNotificationSubscriber getPullNotificationSubscriber() {
        return pullNotificationSubscriber;
    }

    public DeviceStatusTaskPluginConfig getDeviceStatusTaskPluginConfig() {
        return deviceStatusTaskPluginConfig;
    }

    @Override
    public DeviceTypePlatformDetails getDeviceTypePlatformDetails() {
        return deviceTypePlatformDetails;
    }

    @Override
    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    @Override
    public DeviceEnrollmentInvitationDetails getDeviceEnrollmentInvitationDetails() {
        return deviceEnrollmentInvitationDetails;
    }

    @Override
    public License getLicenseConfig() {
        return licenseConfig;
    }

    @Override
    public DeviceTypeMetaDefinition getDeviceTypeMetaDefinition() {
        return deviceTypeMetaDefinition;
    }

    private void setProvisioningConfig(String tenantDomain, DeviceTypeConfiguration deviceTypeConfiguration) {
        if (deviceTypeConfiguration.getProvisioningConfig() != null) {
            boolean sharedWithAllTenants = deviceTypeConfiguration.getProvisioningConfig().isSharedWithAllTenants();
            provisioningConfig = new ProvisioningConfig(tenantDomain, sharedWithAllTenants);
        } else {
            provisioningConfig = new ProvisioningConfig(tenantDomain, false);
        }
    }

    private void setDeviceStatusTaskPluginConfig(DeviceStatusTaskConfiguration deviceStatusTaskConfiguration) {
        if (deviceStatusTaskConfiguration != null && deviceStatusTaskConfiguration.isEnabled()) {
            deviceStatusTaskPluginConfig.setRequireStatusMonitoring(deviceStatusTaskConfiguration.isEnabled());
            deviceStatusTaskPluginConfig.setIdleTimeToMarkInactive(deviceStatusTaskConfiguration.getIdleTimeToMarkInactive());
            deviceStatusTaskPluginConfig.setIdleTimeToMarkUnreachable(deviceStatusTaskConfiguration.getIdleTimeToMarkUnreachable());
            deviceStatusTaskPluginConfig.setFrequency(deviceStatusTaskConfiguration.getFrequency());
        }
    }

    protected void setInitialOperationConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        if (deviceTypeConfiguration.getOperations() != null) {
            List<String> ops = deviceTypeConfiguration.getOperations();
            if (!ops.isEmpty() && deviceTypeConfiguration.getFeatures() != null
                    && deviceTypeConfiguration.getFeatures().getFeature() != null) {
                List<String> validOps = new ArrayList<>();
                for (String operation : ops) {
                    boolean isFeatureExist = false;
                    for (Feature feature : deviceTypeConfiguration.getFeatures().getFeature()) {
                        if (feature.getCode().equals(operation)) {
                            isFeatureExist = true;
                            break;
                        }
                    }
                    if (isFeatureExist) {
                        validOps.add(operation);
                    } else {
                        log.warn("Couldn't fine a valid feature for the operation : " + operation);
                    }
                }
                initialOperationConfig.setOperations(validOps);
            }
        }
    }

    private void setStartupOperationConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        if (deviceTypeConfiguration.getOperations() != null) {
            List<String> startupOperations = deviceTypeConfiguration.getStartupOperations();
            if (startupOperations != null && !startupOperations.isEmpty()) {
                startupOperationConfig.setStartupOperations(startupOperations);
            }
        }
    }

    private void setType(String type) {
        this.type = type;
    }

    private Map<String, String> getConfigProperty(List<ConfigurationEntry> configs) {
        Map<String, String> propertMap = new HashMap<>();
        for (ConfigurationEntry entry : configs) {
            if (entry != null && entry.getValue() != null && entry.getName() != null) {
                propertMap.put(entry.getName(), entry.getValue().toString());
            } else {
                int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
                String domain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
                String message = "Could not find a property in tenant id: " + tenantId + ", " +
                        "domain: " + domain;
                if (entry != null && entry.getName() != null) {
                    message += ", missing value for properly: " + entry.getName();
                }

                log.error(message);
            }
        }
        return propertMap;
    }

    private void setPolicyMonitoringManager(PolicyMonitoring policyMonitoring) {
        if (policyMonitoring != null && policyMonitoring.isEnabled()) {
            this.policyMonitoringManager = new DefaultPolicyMonitoringManager();
        }
    }

    private void setPullNotificationSubscriber(PullNotificationSubscriberConfig pullNotificationSubscriberConfig) {
        if (pullNotificationSubscriberConfig != null) {
            String className = pullNotificationSubscriberConfig.getClassName();
            if (className != null && !className.isEmpty()) {
                PullNotificationSubscriberLoader pullNotificationSubscriberLoader = new PullNotificationSubscriberLoader
                        (className, pullNotificationSubscriberConfig.getConfigProperties());
                this.pullNotificationSubscriber = pullNotificationSubscriberLoader.getPullNotificationSubscriber();
            }
        }
    }


    public void setGeneralConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        this.generalConfig = new GeneralConfig();
        if (deviceTypeConfiguration.getPolicyMonitoring() != null) {
            this.generalConfig.setPolicyMonitoringEnabled(deviceTypeConfiguration.getPolicyMonitoring().isEnabled());
        }
    }

    protected void setDeviceTypePlatformDetails(DeviceTypeConfiguration deviceTypeConfiguration) {
        DeviceTypePlatformDetails deviceTypeVersions = deviceTypeConfiguration.getDeviceTypePlatformDetails();
        if (deviceTypeVersions != null) {
            deviceTypePlatformDetails.setDeviceTypePlatformVersion(deviceTypeVersions.getDeviceTypePlatformVersion());
        }
    }

    public void setDeviceEnrollmentInvitationDetails(DeviceTypeConfiguration deviceTypeConfiguration) {
        DeviceEnrollmentInvitationDetails deviceEnrollmentInvitationDetailsFromConfig = deviceTypeConfiguration
                .getDeviceEnrollmentInvitationDetails();
        if (deviceEnrollmentInvitationDetailsFromConfig != null) {
            deviceEnrollmentInvitationDetails.setEnrollmentDetails(
                    deviceEnrollmentInvitationDetailsFromConfig.getEnrollmentDetails());
        }
    }

    public void setLicenseConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        io.entgra.device.mgt.core.device.mgt.extensions.device.type.template.config.License license = deviceTypeConfiguration
                .getLicense();
        if (license != null) {
            licenseConfig.setName(deviceTypeConfiguration.getName());
            licenseConfig.setLanguage(license.getLanguage());
            licenseConfig.setVersion(license.getVersion());
            licenseConfig.setText(license.getText());
        }
    }

    public void setDeviceTypeMetaDefinition(DeviceTypeConfiguration deviceTypeConfiguration) {
        DeviceTypeMetaDetails deviceTypeMetaDefinitions = deviceTypeConfiguration.getDeviceTypeMetaDetails();
        if (deviceTypeMetaDefinitions != null) {
            deviceTypeMetaDefinition.setStoreVisibilityEnabled(deviceTypeMetaDefinitions.isStoreVisibilityEnabled());
        }
    }
}
