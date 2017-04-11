package com.adonis.data.service;

import com.adonis.data.renta.RentaHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by oksdud on 29.03.2017.
 */
@Component
public class RentaHistoryService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RentaHistory> findAll() {
        String sql = "SELECT * FROM renta_history";
        List<RentaHistory> customers = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RentaHistory.class));
        return customers;
    }

    public RentaHistory findById(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM renta_history WHERE ID = ?";

        RentaHistory vehicle = (RentaHistory) jdbcTemplate.queryForObject(
                sql, new Object[]{id}, new BeanPropertyRowMapper(RentaHistory.class));

        return vehicle;
    }

    public RentaHistory findLast() {
        String sql = "SELECT * FROM renta_history ORDER BY ID DESC LIMIT 1";

        RentaHistory vehicle = null;
        try {
            vehicle = (RentaHistory) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new BeanPropertyRowMapper(RentaHistory.class));
        } catch (Exception e) {
            return null;
        }

        return vehicle;
    }

    public int findTotalRentaHistory() {

        String sql = "SELECT COUNT(*) FROM renta_history";

        int total = jdbcTemplate.queryForObject(sql, Integer.class);

        return total;
    }

    public RentaHistory insert(RentaHistory vehicle) {
        if (vehicle == null) return null;
        try {
            jdbcTemplate.update(
                    "INSERT INTO renta_history " +
                            "(PERSON, VEHICLE, PRICE, SUMMA, FROM, TO, PAID, UPDATED, CREATED ) VALUES " +
                            "(?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            vehicle.getNamePerson(),
                            vehicle.getNameVehicle(),
                            vehicle.getPrice(),
                            vehicle.getSumma(),
                            vehicle.getFrom(),
                            vehicle.getTo(),
                            vehicle.getPaid(),
                            new Date(), new Date()
                    });
        } catch (Exception e) {
            return null;
        }
        return findLast();

    }


    public RentaHistory save(RentaHistory vehicle) {
        if (vehicle == null) return null;
        try {
            jdbcTemplate.update(
                    "UPDATE renta_history SET " +
                            "PERSON=?, " +
                            "VEHICLE=?, " +
                            "PRICE=?, " +
                            "SUMMA=?, " +
                            "FROM=?, " +
                            "TO=?, " +
                            "PAID=?, " +
                            "UPDATED=? " +
                            "WHERE ID=?",
                    vehicle.getNamePerson(),
                    vehicle.getNameVehicle(),
                    vehicle.getPrice(),
                    vehicle.getSumma(),
                    vehicle.getFrom(),
                    vehicle.getTo(),
                    vehicle.getPaid(),
                    new Date(),
                    vehicle.getId());
        } catch (Exception e) {
            return null;
        }
        return findById(vehicle.getId());
    }


    public void delete(RentaHistory vehicle) {
        if (vehicle == null) return;
        jdbcTemplate.execute("DELETE FROM renta_history WHERE ID=" + vehicle.getId());
    }

}
