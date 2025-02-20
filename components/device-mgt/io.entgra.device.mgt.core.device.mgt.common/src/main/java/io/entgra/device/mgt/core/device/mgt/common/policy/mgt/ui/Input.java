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

package io.entgra.device.mgt.core.device.mgt.common.policy.mgt.ui;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Input")
public class Input {

    private String type;
    private String placeholderValue;
    private List<Rule> rules;
    private String apiUrl;
    private String arrayPath;
    private String dataKey;

    @XmlElement(name = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name = "Placeholder")
    public String getPlaceholderValue() {
        return placeholderValue;
    }

    public void setPlaceholderValue(String placeholderValue) {
        this.placeholderValue = placeholderValue;
    }

    @XmlElementWrapper(name = "Rules")
    @XmlElement(name = "Rule")
    public List<Rule> getRules() { return rules; }

    public void setRules(List<Rule> rules) { this.rules = rules; }

    @XmlElement(name = "Url")
    public String getUrl() {
        return apiUrl;
    }

    public void setUrl(String url) {
        this.apiUrl = url;
    }

    @XmlElement(name = "ArrayPath")
    public String getArrayPath() {
        return arrayPath;
    }

    public void setArrayPath(String arrayPath) {
        this.arrayPath = arrayPath;
    }

    @XmlElement(name = "DataKey")
    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }
}
