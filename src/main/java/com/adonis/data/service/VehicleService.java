package com.adonis.data.service;

import com.adonis.data.vehicles.Vehicle;
import com.adonis.data.vehicles.VehicleType;
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
    public List<VehicleType> findAllTypes() {
        String sql = "SELECT * FROM vehicle_types";
        List<VehicleType> customers = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(VehicleType.class));
        return customers;
    }

    public Vehicle findById(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM vehicles WHERE ID = ?";

        Vehicle vehicle = (Vehicle) jdbcTemplate.queryForObject(
                sql, new Object[]{id}, new BeanPropertyRowMapper(Vehicle.class));

        return vehicle;
    }

    public VehicleType findByIdType(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM vehicle_types WHERE ID = ?";

        VehicleType vehicleType = (VehicleType) jdbcTemplate.queryForObject(
                sql, new Object[]{id}, new BeanPropertyRowMapper(VehicleType.class));

        return vehicleType;
    }

    public Vehicle findLast() {
        String sql = "SELECT * FROM vehicles ORDER BY ID DESC LIMIT 1";

        Vehicle vehicle = null;
        try {
            vehicle = (Vehicle) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new BeanPropertyRowMapper(Vehicle.class));
        } catch (Exception e) {
            return null;
        }

        return vehicle;
    }

    public VehicleType findLastType() {
        String sql = "SELECT * FROM vehicle_types ORDER BY ID DESC LIMIT 1";

        VehicleType vehicleType = null;
        try {
            vehicleType = (VehicleType) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new BeanPropertyRowMapper(VehicleType.class));
        } catch (Exception e) {
            return null;
        }

        return vehicleType;
    }

    public int findTotalVehicle() {

        String sql = "SELECT COUNT(*) FROM vehicles";

        int total = jdbcTemplate.queryForObject(sql, Integer.class);

        return total;
    }

    public int findTotalVehicleType() {

        String sql = "SELECT COUNT(*) FROM vehicle_types";

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
                        "MODEL=?, " +
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
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getStatus(),
                vehicle.getVehicleType(),
                vehicle.getActive(),
                vehicle.getLocation(),
                vehicle.getVinNumber(),
                vehicle.getId());
    }

    public Vehicle insert(Vehicle vehicle) {
        if (vehicle == null) return null;
        try {
            jdbcTemplate.update(
                    "INSERT INTO vehicles " +
                            "(VEHICLE_NMBR, LICENSE_NMBR, MAKE, MODEL, YEAR, STATUS," +
                            " VEHICLE_TYPE, ACTIVE, LOCATION, VIN_NUMBER) VALUES " +
                            "(?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            vehicle.getVehicleNmbr(),
                            vehicle.getLicenseNmbr(),
                            vehicle.getMake(),
                            vehicle.getModel(),
                            vehicle.getYear(),
                            vehicle.getStatus(),
                            vehicle.getVehicleType(),
                            vehicle.getActive(),
                            vehicle.getLocation(),
                            vehicle.getVinNumber(),
                    });
        } catch (Exception e) {
            return null;
        }
     return findLast();

    }
    public VehicleType insertType(VehicleType vehicleType) {
        if (vehicleType == null) return null;
        try {
            jdbcTemplate.update(
                    "INSERT INTO vehicle_types " +
                            "(TYPE, PICTURE)" +
                            " VALUES " +
                            "(?, ?)",
                    new Object[]{
                            vehicleType.getType(),
                            vehicleType.getPicture()
                    });
        } catch (Exception e) {
            return null;
        }
        return findLastType();
    }

    public Vehicle save(Vehicle vehicle) {
        if (vehicle == null) return null;
        try {
            jdbcTemplate.update(
                    "UPDATE vehicles SET " +
                            "VEHICLE_NMBR=?, " +
                            "LICENSE_NMBR=?, " +
                            "MAKE=?, " +
                            "MODEL=?, " +
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
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getStatus(),
                    vehicle.getVehicleType(),
                    vehicle.getActive(),
                    vehicle.getLocation(),
                    vehicle.getVinNumber(),
                    vehicle.getId());
        } catch (Exception e) {
            return null;
        }
        return findById(vehicle.getId());
    }
    public VehicleType save(VehicleType vehicleType) {
        if (vehicleType == null) return null;
        try {
            jdbcTemplate.update(
                    "UPDATE vehicle_types SET " +
                            "TYPE=?, " +
                            "PICTURE=? " +
                            "WHERE ID=?",
                    vehicleType.getType(),
                    vehicleType.getPicture(),
                    vehicleType.getId());
        } catch (Exception e) {
            return null;
        }
        return findByIdType(vehicleType.getId());
    }

    public void delete(Vehicle vehicle) {
        if (vehicle == null) return;
        jdbcTemplate.execute("DELETE FROM vehicles WHERE ID=" + vehicle.getId());
    }

    public void delete(VehicleType vehicleType) {
        if (vehicleType == null) return;
        jdbcTemplate.execute("DELETE FROM vehicle_types WHERE ID=" + vehicleType.getId());
    }

    public void loadData() {

        String csvFile = "Vechicles.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(csvFile);
        } catch (Exception e) {
            return;
        }
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
    public void loadVechicleTypes() {

        String csvFile = "VechycleType.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(csvFile);
        } catch (Exception e) {
            return;
        }
        Reader reader = new InputStreamReader(inputStream);

        try {
            br = new BufferedReader(reader);

            while ((line = br.readLine()) != null) {
                String[] vehicleType = line.split(cvsSplitBy);

                VehicleType entry = new VehicleType();
                entry.setType(vehicleType[1]);
                entry.setPicture(vehicleType[2]);
                try {
                    insertType(entry);
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
