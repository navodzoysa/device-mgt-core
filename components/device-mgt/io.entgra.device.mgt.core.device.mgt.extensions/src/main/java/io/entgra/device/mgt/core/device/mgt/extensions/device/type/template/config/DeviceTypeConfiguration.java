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
package io.entgra.device.mgt.core.device.mgt.extensions.device.type.template.config;

import io.entgra.device.mgt.core.device.mgt.common.invitation.mgt.DeviceEnrollmentInvitationDetails;
import io.entgra.device.mgt.core.device.mgt.common.type.mgt.DeviceTypeMetaDetails;
import io.entgra.device.mgt.core.device.mgt.common.type.mgt.DeviceTypePlatformDetails;

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * <p>Java class for DeviceTypeConfiguration complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="DeviceTypeConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DeviceDetails" type="{}DeviceDetails"/>
 *         &lt;element name="Features" type="{}Features"/>
 *         &lt;element name="PolicyUIConfigurations" type="{}PolicyUIConfigurations"/>
 *         &lt;element name="ProvisioningConfig" type="{}ProvisioningConfig"/>
 *         &lt;element name="PushNotificationProviderConfig" type="{}PushNotificationProviderConfig"/>
 *         &lt;element name="License" type="{}License"/>
 *         &lt;element name="DataSource" type="{}DataSource"/>
 *         &lt;element name="PolicyMonitoring" type="{}PolicyMonitoring"/>
 *         &lt;element name="DeviceAuthorizationConfig" type="{}DeviceAuthorizationConfig"/>
 *         &lt;element name="DeviceStatusTaskConfig" type="{}DeviceStatusTaskConfig"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DeviceTypeConfiguration")
public class DeviceTypeConfiguration {

    @XmlElement(name = "DeviceDetails", required = true)
    protected DeviceDetails deviceDetails;
    @XmlElement(name = "Features", required = true)
    protected Features features;
    @XmlElement(name = "PolicyUIConfigurations", required = true)
    protected PolicyUIConfigurations policies;
    @XmlElement(name = "ProvisioningConfig", required = true)
    protected ProvisioningConfig provisioningConfig;
    @XmlElement(name = "PushNotificationProviderConfig", required = true)
    protected PushNotificationProvider pushNotificationProvider;
    @XmlElement(name = "PullNotificationSubscriberConfig", required = true)
    protected PullNotificationSubscriberConfig pullNotificationSubscriberConfig;
    @XmlElement(name = "License", required = true)
    protected License license;
    @XmlElement(name = "DataSource", required = true)
    protected DataSource dataSource;
    @XmlElement(name = "TaskConfiguration", required = true)
    private TaskConfiguration taskConfiguration;
    @XmlElement(name = "DeviceStatusTaskConfig")
    private DeviceStatusTaskConfiguration deviceStatusTaskConfiguration;
    @XmlElement(name = "DeviceAuthorizationConfig", required = true)
    protected DeviceAuthorizationConfig deviceAuthorizationConfig;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlElement(name = "PolicyMonitoring", required = true)
    protected PolicyMonitoring policyMonitoring;
    @XmlElementWrapper(name = "InitialOperationConfig")
    @XmlElement(name = "Operation", required = true)
    protected List<String> operations;
    @XmlElement(name = "DeviceTypePlatformDetails", required = true)
    protected DeviceTypePlatformDetails deviceTypePlatformDetails;
    @XmlElement(name = "DeviceEnrollmentInvitationDetails", required = true)
    protected DeviceEnrollmentInvitationDetails deviceEnrollmentInvitationDetails;
    @XmlElement(name = "DeviceTypeMetaDetails")
    protected DeviceTypeMetaDetails deviceTypeMetaDetails;

    public DeviceTypePlatformDetails getDeviceTypePlatformDetails() {
        return deviceTypePlatformDetails;
    }

    public void setDeviceTypePlatformDetails(DeviceTypePlatformDetails deviceTypePlatformDetails) {
        this.deviceTypePlatformDetails = deviceTypePlatformDetails;
    }

    @XmlElementWrapper(name = "StartupOperationConfig")
    @XmlElement(name = "Operation", required = true)
    protected List<String> startupOperations;

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    /**
     * Gets the value of the deviceStatusTaskConfiguration property.
     *
     * @return
     *     possible object is
     *     {@link DeviceStatusTaskConfiguration }
     *
     */
    public DeviceStatusTaskConfiguration getDeviceStatusTaskConfiguration() {
        return deviceStatusTaskConfiguration;
    }

    /**
     * Sets the value of the deviceStatusTaskConfiguration property.
     *
     * @param deviceStatusTaskConfiguration
     *     allowed object is
     *     {@link DeviceStatusTaskConfiguration }
     *
     */
    public void setDeviceStatusTaskConfiguration(DeviceStatusTaskConfiguration deviceStatusTaskConfiguration) {
        this.deviceStatusTaskConfiguration = deviceStatusTaskConfiguration;
    }

    /**
     * Gets the value of the taskConfiguration property.
     *
     * @return
     *     possible object is
     *     {@link TaskConfiguration }
     *
     */
    public TaskConfiguration getTaskConfiguration() {
        return taskConfiguration;
    }

    /**
     * Sets the value of the taskConfiguration property.
     *
     * @param taskConfiguration
     *     allowed object is
     *     {@link TaskConfiguration }
     *
     */
    public void setTaskConfiguration(TaskConfiguration taskConfiguration) {
        this.taskConfiguration = taskConfiguration;
    }

    /**
     * Gets the value of the deviceDetails property.
     *
     * @return possible object is
     * {@link DeviceDetails }
     */
    public DeviceDetails getDeviceDetails() {
        return deviceDetails;
    }

    /**
     * Sets the value of the deviceDetails property.
     *
     * @param value allowed object is
     *              {@link DeviceDetails }
     */
    public void setDeviceDetails(DeviceDetails value) {
        this.deviceDetails = value;
    }

    /**
     * Gets the value of the policyMonitoring property.
     *
     * @return possible object is
     * {@link DeviceDetails }
     */
    public PolicyMonitoring getPolicyMonitoring() {
        return policyMonitoring;
    }

    /**
     * Sets the value of the policyMonitoring property.
     *
     * @param value allowed object is
     *              {@link DeviceDetails }
     */
    public void setPolicyMonitoring(PolicyMonitoring value) {
        this.policyMonitoring = value;
    }

    /**
     * Gets the value of the features property.
     *
     * @return possible object is
     * {@link Features }
     */
    public Features getFeatures() {
        return features;
    }

    /**
     * Sets the value of the features property.
     *
     * @param value allowed object is
     *              {@link Features }
     */
    public void setFeatures(Features value) {
        this.features = value;
    }

    /**
     * Gets the value of the policies property.
     *
     * @return possible object is
     * {@link PolicyUIConfigurations }
     */
    public PolicyUIConfigurations getPolicyUIConfigurations() {
        return policies;
    }

    /**
     * Sets the value of the features property.
     *
     * @param value allowed object is
     *              {@link PolicyUIConfigurations }
     */
    public void setPolicyUIConfigurations(PolicyUIConfigurations value) {
        this.policies = value;
    }
    /**
     * Gets the value of the provisioningConfig property.
     *
     * @return possible object is
     * {@link ProvisioningConfig }
     */
    public ProvisioningConfig getProvisioningConfig() {
        return provisioningConfig;
    }

    /**
     * Sets the value of the provisioningConfig property.
     *
     * @param value allowed object is
     *              {@link ProvisioningConfig }
     */
    public void setProvisioningConfig(ProvisioningConfig value) {
        this.provisioningConfig = value;
    }

    /**
     * Gets the value of the pushNotificationProvider property.
     *
     * @return possible object is
     * {@link PushNotificationProvider }
     */
    public PushNotificationProvider getPushNotificationProvider() {
        return pushNotificationProvider;
    }

    /**
     * Sets the value of the pushNotificationProvider property.
     *
     * @param value allowed object is
     *              {@link PushNotificationProvider }
     */
    public void setPushNotificationProvider(PushNotificationProvider value) {
        this.pushNotificationProvider = value;
    }

    /**
     * Gets the value of the PullNotificationSubscriberConfig property.
     *
     * @return possible object is
     * {@link PullNotificationSubscriberConfig }
     */
    public PullNotificationSubscriberConfig getPullNotificationSubscriberConfig() {
        return pullNotificationSubscriberConfig;
    }

    /**
     * Sets the value of the PullNotificationSubscriberConfig property.
     *
     * @param value allowed object is
     *              {@link PullNotificationSubscriberConfig }
     */
    public void setPullNotificationSubscriberConfig(PullNotificationSubscriberConfig value) {
        this.pullNotificationSubscriberConfig = value;
    }

    /**
     * Gets the value of the license property.
     *
     * @return possible object is
     * {@link License }
     */
    public License getLicense() {
        return license;
    }

    /**
     * Sets the value of the license property.
     *
     * @param value allowed object is
     *              {@link License }
     */
    public void setLicense(License value) {
        this.license = value;
    }

    /**
     * Gets the value of the dataSource property.
     *
     * @return possible object is
     * {@link DataSource }
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets the value of the dataSource property.
     *
     * @param value allowed object is
     *              {@link DataSource }
     */
    public void setDataSource(DataSource value) {
        this.dataSource = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the authorizationRequired property.
     *
     * @return possible object is
     * {@link DeviceAuthorizationConfig }
     */
    public DeviceAuthorizationConfig getDeviceAuthorizationConfig() {
        return deviceAuthorizationConfig;
    }

    /**
     * Sets the value of the provisioningConfig property.
     *
     * @param value allowed object is
     *              {@link DeviceAuthorizationConfig }
     */
    public void setDeviceAuthorizationConfig(DeviceAuthorizationConfig value) {
        this.deviceAuthorizationConfig = value;
    }

    /**
     * Gets the value of startup operation config
     *
     * @return possible object is
     * {@link List<String>}
     */
    public List<String> getStartupOperations() {
        return startupOperations;
    }

    /**
     * Sets the value for startup operation config
     *
     * @param startupOperations possible object is
     * {@link List<String>}
     */
    public void setStartupOperations(List<String> startupOperations) {
        this.startupOperations = startupOperations;
    }

    /**
     * Gets the value of device enrollment invitation details which has enrollment steps of enrollment types
     * @return device enrollment invitation details
     */
    public DeviceEnrollmentInvitationDetails getDeviceEnrollmentInvitationDetails() {
        return deviceEnrollmentInvitationDetails;
    }

    /**
     * Sets the value of device enrollment invitation details from the relevant device type xml file
     * @param deviceEnrollmentInvitationDetails {@link DeviceEnrollmentInvitationDetails} object
     */
    public void setDeviceEnrollmentInvitationDetails(
            DeviceEnrollmentInvitationDetails deviceEnrollmentInvitationDetails) {
        this.deviceEnrollmentInvitationDetails = deviceEnrollmentInvitationDetails;
    }

    public DeviceTypeMetaDetails getDeviceTypeMetaDetails() {
        return deviceTypeMetaDetails;
    }

    public void setDeviceTypeMetaDetails(DeviceTypeMetaDetails deviceTypeMetaDetails) {
        this.deviceTypeMetaDetails = deviceTypeMetaDetails;
    }
}
