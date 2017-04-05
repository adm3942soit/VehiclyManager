package com.adonis.data.service;

import com.adonis.data.vehicles.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by oksdud on 29.03.2017.
 */
@Component
public class VehicleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Vehicle> findAll() {
        String sql = "SELECT * FROM vehicles";
        List<Vehicle> customers = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Vehicle.class));
        return customers;
    }

    public Vehicle findById(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM vehicles WHERE ID = ?";

        Vehicle vehicle = (Vehicle) jdbcTemplate.queryForObject(
                sql, new Object[]{id}, new BeanPropertyRowMapper(Vehicle.class));

        return vehicle;
    }


    public int findTotalVehicle() {

        String sql = "SELECT COUNT(*) FROM vehicles";

        int total = jdbcTemplate.queryForObject(sql, Integer.class);

        return total;
    }


    public void update(Vehicle vehicle) {
        if (vehicle == null) return;
        jdbcTemplate.update(
                "UPDATE vehicles SET " +
                        "VEHICLE_NMBR=?, " +
                        "LICENSE_NMBR=?, " +
                        "MAKE=?, " +
                        "YEAR=?, " +
                        "STATUS=?, " +
                        "VEHICLE_TYPE=?, " +
                        "ACTIVE=?, " +
                        "LOCATION=?, " +
                        "VIN_NUMBER=? " +
                        "WHERE ID=?",
                vehicle.getVehicleNmbr(),
                vehicle.getLicenseNmbr(),
                vehicle.getMake(),
                vehicle.getYear(),
                vehicle.getStatus(),
                vehicle.getVehicleType(),
                vehicle.getActive(),
                vehicle.getLocation(),
                vehicle.getVinNumber(),
                vehicle.getId());
    }

    public void insert(Vehicle vehicle) {
        if (vehicle == null) return ;
        jdbcTemplate.update(
                "INSERT INTO vehicles " +
                        "(VEHICLE_NMBR, LICENSE_NMBR, MAKE, YEAR, STATUS," +
                        " VEHICLE_TYPE, ACTIVE, LOCATION, VIN_NUMBER) VALUES " +
                        "(?,?,?,?,?,?,?,?,?)",
                new Object[]{
                        vehicle.getVehicleNmbr(),
                        vehicle.getLicenseNmbr(),
                        vehicle.getMake(),
                        vehicle.getYear(),
                        vehicle.getStatus(),
                        vehicle.getVehicleType(),
                        vehicle.getActive(),
                        vehicle.getLocation(),
                        vehicle.getVinNumber(),
                });

    }

    public Vehicle save(Vehicle vehicle) {
        if (vehicle == null) return null;
        jdbcTemplate.update(
                "UPDATE vehicles SET " +
                        "VEHICLE_NMBR=?, " +
                        "LICENSE_NMBR=?, " +
                        "MAKE=?, " +
                        "YEAR=?, " +
                        "STATUS=?, " +
                        "VEHICLE_TYPE=?, " +
                        "ACTIVE=?, " +
                        "LOCATION=?, " +
                        "VIN_NUMBER=? " +
                        "WHERE ID=?",
                vehicle.getVehicleNmbr(),
                vehicle.getLicenseNmbr(),
                vehicle.getMake(),
                vehicle.getYear(),
                vehicle.getStatus(),
                vehicle.getVehicleType(),
                vehicle.getActive(),
                vehicle.getLocation(),
                vehicle.getVinNumber(),
                vehicle.getId());
        return findById(vehicle.getId());
    }

    public void delete(Vehicle vehicle) {
        if (vehicle == null) return;
        jdbcTemplate.execute("DELETE FROM vehicles WHERE ID=" + vehicle.getId());
    }


    public void loadData() {

        String csvFile = "Vechicles.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFile);
        Reader reader = new InputStreamReader(inputStream);

        try {
            br = new BufferedReader(reader);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");

            while ((line = br.readLine()) != null) {
                String[] vehicle = line.split(cvsSplitBy);

                Vehicle entry = new Vehicle();
                 //todo


                try {
                    insert(entry);
                } catch (Exception e) {
                    if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                        break;
                    }
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
