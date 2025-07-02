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
package io.entgra.device.mgt.core.device.mgt.core.config.ui;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents the Application Management Configuration.
 */
@XmlRootElement(name = "UIConfiguration")
public class UIConfiguration {

    private AppRegistration appRegistration;
    private List<String> scopes;
    private boolean isSsoEnable;
    private int sessionTimeOut;
    private int loginCacheCapacity;
    private Billing billing;
    private HubspotChat hubspotChat;
    private List<String> tenantContextEnabledApps;

    private DeviceInfoConfigurations deviceInfoConfigurations;

    private DeviceStatusConfigurations deviceStatusConfigurations;

    @XmlElement(name = "AppRegistration", required = true)
    public AppRegistration getAppRegistration() {
        return appRegistration;
    }

    public void setAppRegistration(AppRegistration appRegistration) {
        this.appRegistration = appRegistration;
    }

    @XmlElementWrapper(name = "Scopes")
    @XmlElement(name = "Scope")
    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    @XmlElement(name = "EnableSSO")
    public boolean isSsoEnable() {
        return isSsoEnable;
    }

    public void setSsoEnable(boolean ssoEnable) {
        isSsoEnable = ssoEnable;
    }

    @XmlElement(name = "HubspotChat", required = true)
    public HubspotChat getHubspotChat() {
        return hubspotChat;
    }

    public void setHubspotChat(HubspotChat hubspotChat) {
        this.hubspotChat = hubspotChat;
    }

    @XmlElement(name = "Billing", required = true)
    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    @XmlElement(name = "SessionTimeOut")
    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    @XmlElement(name = "LoginCacheCapacity")
    public int getLoginCacheCapacity() {
        return loginCacheCapacity;
    }

    public void setLoginCacheCapacity(int loginCacheCapacity) {
        this.loginCacheCapacity = loginCacheCapacity;
    }

    @XmlElement(name = "DeviceInfoConfigurations", required = true)
    public DeviceInfoConfigurations getDeviceInfoConfigurations() {
        return deviceInfoConfigurations;
    }

    public void setDeviceInfoConfigurations(DeviceInfoConfigurations deviceInfoConfigurations) {
        this.deviceInfoConfigurations = deviceInfoConfigurations;
    }

    @XmlElement(name = "DeviceStatusConfigurations", required = true)
    public DeviceStatusConfigurations getDeviceStatusConfigurations() {
        return deviceStatusConfigurations;
    }

    public void setDeviceStatusConfigurations(DeviceStatusConfigurations deviceStatusConfigurations) {
        this.deviceStatusConfigurations = deviceStatusConfigurations;
    }

    @XmlElementWrapper(name = "TenantContextEnabledApps")
    @XmlElement(name = "AppName")
    public List<String> getTenantContextEnabledApps() {
        return tenantContextEnabledApps;
    }

    public void setTenantContextEnabledApps(List<String> tenantContextEnabledApps) {
        this.tenantContextEnabledApps = tenantContextEnabledApps;
    }
}
