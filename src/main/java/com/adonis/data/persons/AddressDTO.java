package com.adonis.data.persons;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by oksdud on 10.04.2017.
 */
@XmlRootElement(name = "address")
public class AddressDTO {
        @XmlElement(name = "street")
        private String street;
        @XmlElement(name = "zip")
        private String zip;
        @XmlElement(name = "city")
        private String city;
        @XmlElement(name = "country")
        private String country;
}
