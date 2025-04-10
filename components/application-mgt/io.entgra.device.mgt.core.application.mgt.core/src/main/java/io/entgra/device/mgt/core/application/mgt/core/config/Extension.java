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
package io.entgra.device.mgt.core.application.mgt.core.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Represents a extension in the application management configuration.
 */
@XmlRootElement(name = "Extension")
public class Extension {

    private String name;

    private String className;

    private List<Parameter> parameters;

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "ClassName")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @XmlElementWrapper(name = "Parameters")
    @XmlElement(name = "Parameter")
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public boolean equals(Object anotherObj) {
        if (anotherObj instanceof Extension) {
            Extension anExt = (Extension) anotherObj;
            if (anExt.getName().contentEquals(this.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * ApplicationManagement Extensions.
     */
    public enum Name {
        SPApplicationManager,
        ApplicationManager,
        ApplicationReleaseManager,
        CategoryManager,
        ReviewManager,
        LifecycleStateManager,
        PlatformManager,
        VisibilityTypeManager,
        SubscriptionManager,
        VisibilityManager,
        ApplicationStorageManager,
        PlatformStorageManager
    }
}

