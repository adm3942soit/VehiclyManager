package com.adonis.data.persons;

import com.adonis.data.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by oksdud on 10.04.2017.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "address", schema = "")
@Getter
@Setter
@NoArgsConstructor
@Cacheable(value = false)
public class Address extends Audit{
        @Column(name = "STREET", nullable = false)
        private String street;
        @Column(name = "ZIP", nullable = false)
        private String zip;
        @Column(name = "CITY", nullable = false)
        private String city;
        @Column(name = "COUNTRY", nullable = false)
        private String country;
}
