package com.adonis.data.vehicles;

import com.adonis.data.AbstractEntity;
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
@Table(name = "vehicle_types", schema = "")
@Getter
@Setter
@NoArgsConstructor
public class VehicleType extends AbstractEntity {
    @Column(name = "TYPE", length = 50, unique = true)
    private String type;
    @Column(name = "PICTURE", length = 200)
    private String picture;

}
