package com.adonis.data.vehicles;

import com.adonis.data.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by oksdud on 05.04.2017.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "vehicles", schema = "")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle extends Audit {
    @Column(name = "VEHICLE_NMBR", length = 30)
    private String vehicleNmbr;
    @Column(name = "LICENSE_NMBR", length = 100)
    private String licenseNmbr;
    @Column(name = "MAKE", length = 100)
    private String make;
    @Column(name = "MODEL", length = 100)
    private String model;
    @Column(name = "YEAR")
    private Long year;
    @Column(name = "STATUS", length = 100)
    private String status;
    @Column(name = "VEHICLE_TYPE", length = 100)
    private String vehicleType;
    @Column(name = "ACTIVE")
    private Boolean active;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "VIN_NMBR")
    private String vinNumber;

}
