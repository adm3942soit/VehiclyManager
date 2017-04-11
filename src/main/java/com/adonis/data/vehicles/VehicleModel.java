package com.adonis.data.vehicles;

import com.adonis.data.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by oksdud on 07.04.2017.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "vehicle_types", schema = "")
@Getter
@Setter
@NoArgsConstructor
public class VehicleModel extends Audit{

    @Column(name = "VEHICLE_TYPE")
    private String vehicleType;

    @Column(name = "MODEL", length = 100)
    private String model;

    @Column(name = "PICTURE", length = 200)
    private String picture;

    @Column(name = "COMMENT", length = 100)
    private String comment;

}
