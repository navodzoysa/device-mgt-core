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

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * <p>Java class for Feature complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * <xs:element name="Feature">
 *   <xs:complexType>
 *     <xs:sequence>
 *       <xs:element name="Name" type="xs:string" />
 *       <xs:element name="Description" type="xs:string" />
 *       <xs:element name="Operation" type="{}Operation" />
 *     </xs:sequence>
 *     <xs:attribute name="type" type="xs:string" />
 *     <xs:attribute name="code" type="xs:string" />
 *   </xs:complexType>
 * </xs:element>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Feature", propOrder = {
    "name",
    "description",
    "tooltip",
    "operation",
    "metaData",
    "confirmationTexts",
    "dangerZoneTooltipTexts"
})
public class Feature {

    @XmlElement(name = "Name", required = true)
    protected String name;

    @XmlElement(name = "Description", required = true)
    protected String description;

    @XmlElement(name = "Tooltip", required = false)
    protected String tooltip;

    @XmlElement(name = "Operation")
    protected Operation operation;

    @XmlAttribute(name = "type", required = true)
    protected String type;

    @XmlAttribute(name = "code")
    protected String code;

    @XmlElementWrapper(name = "MetaData")
    @XmlElement(name = "Property", required = true)
    private List<String> metaData;

    @XmlElement(name = "ConfirmationTexts", required = false)
    private List<String> confirmationTexts;

    @XmlElement(name = "DangerZoneTooltipTexts", required = false)
    private List<String> dangerZoneTooltipTexts;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the tooltip property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Sets the value of the tooltip property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTooltip(String value) {
        this.tooltip = value;
    }


    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link Operation }
     *     
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Operation }
     *     
     */
    public void setOperation(Operation value) {
        this.operation = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    public List<String> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<String> metaData) {
        this.metaData = metaData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getConfirmationTexts() {
        return confirmationTexts;
    }

    public void setConfirmationTexts(List<String> confirmationTexts) {
        this.confirmationTexts = confirmationTexts;
    }

    public List<String> getDangerZoneTooltipTexts() {
        return dangerZoneTooltipTexts;
    }

    public void setDangerZoneTooltipTexts(List<String> dangerZoneTooltipTexts) {
        this.dangerZoneTooltipTexts = dangerZoneTooltipTexts;
    }
}
