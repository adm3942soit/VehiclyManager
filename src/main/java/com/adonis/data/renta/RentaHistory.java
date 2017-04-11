package com.adonis.data.renta;

import com.adonis.data.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by oksdud on 11.04.2017.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "renta_history", schema = "")
@Getter
@Setter
@NoArgsConstructor
public class RentaHistory extends Audit{
    @Column(name = "PERSON")
    private String namePerson;
    @Column(name = "VEHICLE")
    private String nameVehicle;
    @Column(name = "FROM")
    private Timestamp from;
    @Column(name = "TO")
    private Timestamp to;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "SUMMA")
    private Double summa;
    @Column(name = "PAID")
    private Boolean paid;

}
