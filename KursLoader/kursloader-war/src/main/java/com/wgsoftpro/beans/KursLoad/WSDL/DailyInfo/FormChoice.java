
package com.wgsoftpro.beans.KursLoad.WSDL.DailyInfo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for formChoice.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="formChoice">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="qualified"/>
 *     &lt;enumeration value="unqualified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "formChoice", namespace = "http://www.w3.org/2001/XMLSchema")
@XmlEnum
public enum FormChoice {

    @XmlEnumValue("qualified")
    QUALIFIED("qualified"),
    @XmlEnumValue("unqualified")
    UNQUALIFIED("unqualified");
    private final String value;

    FormChoice(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FormChoice fromValue(String v) {
        for (FormChoice c: FormChoice.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
