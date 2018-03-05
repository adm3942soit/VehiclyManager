package com.adonis.data.vehicles;

import com.adonis.data.Audit;
import com.adonis.data.vehicles.img.FotoAlbum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by oksdud on 05.04.2017.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "vehicles", schema = "")
@Getter
@Setter
@NoArgsConstructor
@Cacheable(value = false)
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
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "PRICE_DAY")
    private Double priceDay;
    @Column(name = "PRICE_WEEK")
    private Double priceWeek;
    @Column(name = "PRICE_MONTH")
    private Double priceMonth;

//    @OneToOne(cascade = CascadeType.ALL)
    @Column(name = "FOTO_ALBUM_ID")
    private FotoAlbum fotoAlbum;

}
