package com.adonis.data.service;

import com.adonis.data.vehicles.Vehicle;
import com.adonis.data.vehicles.VehicleModel;
import com.adonis.data.vehicles.VehicleType;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
        List<Vehicle> vehicles = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Vehicle.class));
        return vehicles;
    }
    public List<Integer> findAllActive() {
        String sql = "SELECT DISTINCT v.ID FROM vehicles v WHERE v.ACTIVE = 1";
        List<Integer> vehicles = jdbcTemplate.query(sql,  new BeanPropertyRowMapper(Vehicle.class));
        return vehicles;
    }
    public List<Integer> findAllNotActive() {
        String sql = "SELECT v.ID FROM vehicles v WHERE v.ACTIVE = 0";
        List<Integer> vehicles = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Vehicle.class));
        return vehicles;
    }

    public List<String> findAllNames() {

        List<Vehicle> vehicles = findAll();
        if(vehicles==null || vehicles.isEmpty()) return Collections.EMPTY_LIST;
        List<String> names = Lists.newArrayList();
        vehicles.forEach(vehicle -> {names.add(vehicle.getVehicleNmbr());});
        return names;
    }

    public List<VehicleType> findAllTypes() {
        String sql = "SELECT * FROM vehicle_types";
        List<VehicleType> customers = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(VehicleType.class));
        return customers;
    }

    public List<String> findAllTypesNames() {
        String sql = "SELECT m.TYPE FROM vehicle_types m ";

        List<String> types = null;
        try {
            types = (List<String>) jdbcTemplate.query(sql,
                    new RowMapper<String>() {
                        public String mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            return rs.getString("type");
                        }
                    });
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }

        return types;
    }
    public List<String> findAllModelNames() {
        String sql = "SELECT m.MODEL FROM vehicle_models m ";

        List<String> models = null;
        try {
            models = (List<String>) jdbcTemplate.query(sql,
                    new RowMapper<String>() {
                        public String mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            return rs.getString("model");
                        }
                    });
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }

        return models;
    }

    public List<VehicleModel> findAllModels() {
        String sql = "SELECT * FROM vehicle_models";
        List<VehicleModel> customers = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(VehicleModel.class));
        return customers;
    }

    public Vehicle findById(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM vehicles WHERE ID = ?";

        Vehicle vehicle = (Vehicle) jdbcTemplate.queryForObject(
                sql, new Object[]{id}, new BeanPropertyRowMapper(Vehicle.class));

        return vehicle;
    }
    public Vehicle findByVehicleNumber(String vehicleNumber) {
        if (Strings.isNullOrEmpty(vehicleNumber)) return null;
        String sql = "SELECT * FROM vehicles WHERE VEHICLE_NMBR = ?";

        Vehicle vehicle = (Vehicle) jdbcTemplate.queryForObject(
                sql, new Object[]{vehicleNumber}, new BeanPropertyRowMapper(Vehicle.class));

        return vehicle;
    }

    public VehicleType findByIdType(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM vehicle_types WHERE ID = ?";

        VehicleType vehicleType = (VehicleType) jdbcTemplate.queryForObject(
                sql, new Object[]{id}, new BeanPropertyRowMapper(VehicleType.class));

        return vehicleType;
    }

    public VehicleModel findByIdModel(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM vehicle_models WHERE ID = ?";

        VehicleModel vehicleModel = (VehicleModel) jdbcTemplate.queryForObject(
                sql, new Object[]{id}, new BeanPropertyRowMapper(VehicleModel.class));

        return vehicleModel;
    }

    public Vehicle findLast() {
        String sql = "SELECT * FROM vehicles ORDER BY ID DESC LIMIT 1";

        Vehicle vehicle = null;
        try {
            vehicle = (Vehicle) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new BeanPropertyRowMapper(Vehicle.class));
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }

        return vehicleType;
    }

    public VehicleModel findLastModel() {
        String sql = "SELECT * FROM vehicle_models ORDER BY ID DESC LIMIT 1";

        VehicleModel vehicleModel = null;
        try {
            vehicleModel = (VehicleModel) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new BeanPropertyRowMapper(VehicleModel.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return vehicleModel;
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
                        "VIN_NUMBER=?, " +
                        "PRICE=?, " +
                        "UPDATED =? " +
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
                vehicle.getPrice(),
                new Date(),
                vehicle.getId());
    }

    public Vehicle insert(Vehicle vehicle) {
        if (vehicle == null) return null;
        try {
            jdbcTemplate.update(
                    "INSERT INTO vehicles " +
                            "(VEHICLE_NMBR, LICENSE_NMBR, MAKE, MODEL, YEAR, STATUS," +
                            " VEHICLE_TYPE, ACTIVE, LOCATION, VIN_NUMBER, PRICE, UPDATED, CREATED ) VALUES " +
                            "(?,?,?,?,?,?,?,?,?, ?, ?, ?, ?)",
                    new Object[]{
                            vehicle.getVehicleNmbr(),
                            vehicle.getLicenseNmbr(),
                            vehicle.getMake(),
                            vehicle.getModel(),
                            vehicle.getYear(),
                            vehicle.getStatus(),
                            vehicle.getVehicleType(),
                            vehicle.getActive()==null?0:1,
                            vehicle.getLocation(),
                            vehicle.getVinNumber(),
                            vehicle.getPrice(),
                            new Date(), new Date()
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return findLast();

    }

    public VehicleType insertType(VehicleType vehicleType) {
        if (vehicleType == null) return null;
        try {
            jdbcTemplate.update(
                    "INSERT INTO vehicle_types " +
                            "(TYPE, PICTURE, UPDATED, CREATED )" +
                            " VALUES " +
                            "(?, ?, ?, ?)",
                    new Object[]{
                            vehicleType.getType(),
                            vehicleType.getPicture(),
                            new Date(), new Date()

                    });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return findLastType();
    }

    public VehicleModel insertModel(VehicleModel vehicleModel) {
        if (vehicleModel == null) return null;
        try {
            jdbcTemplate.update(
                    "INSERT INTO vehicle_models " +
                            "(VEHICLE_TYPE, MODEL, PICTURE, COMMENT, UPDATED, CREATED)" +
                            " VALUES " +
                            "(?, ?, ?, ?, ?,?)",
                    new Object[]{
                            vehicleModel.getVehicleType(),
                            vehicleModel.getModel(),
                            vehicleModel.getPicture(),
                            vehicleModel.getComment(),
                            new Date(), new Date()
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return findLastModel();
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
                            "VIN_NUMBER=?, " +
                            "PRICE=?, " +
                            "UPDATED=? " +
                            "WHERE ID=?",
                    vehicle.getVehicleNmbr(),
                    vehicle.getLicenseNmbr(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getStatus(),
                    vehicle.getVehicleType(),
                    vehicle.getActive()==null?0:1,
                    vehicle.getLocation(),
                    vehicle.getVinNumber(),
                    vehicle.getPrice(),
                    new Date(),
                    vehicle.getId());
        } catch (Exception e) {
            e.printStackTrace();
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
                            "PICTURE=?, " +
                            "UPDATED=? " +
                            "WHERE ID=?",
                    vehicleType.getType(),
                    vehicleType.getPicture(),
                    new Date(),
                    vehicleType.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return findByIdType(vehicleType.getId());
    }

    public VehicleModel save(VehicleModel vehicleModel) {
        if (vehicleModel == null) return null;
        try {
            jdbcTemplate.update(
                    "UPDATE vehicle_types SET " +
                            "VEHICLE_TYPE=?, " +
                            "MODEL=?, " +
                            "PICTURE=?, " +
                            "COMMENT=?, " +
                            "UPDATED=? " +
                            "WHERE ID=?",
                    vehicleModel.getVehicleType(),
                    vehicleModel.getModel(),
                    vehicleModel.getPicture(),
                    vehicleModel.getComment(),
                    new Date(),
                    vehicleModel.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return findByIdModel(vehicleModel.getId());
    }

    public void delete(Vehicle vehicle) {
        if (vehicle == null) return;
        jdbcTemplate.execute("DELETE FROM vehicles WHERE ID=" + vehicle.getId());
    }

    public void delete(VehicleType vehicleType) {
        if (vehicleType == null) return;
        jdbcTemplate.execute("DELETE FROM vehicle_types WHERE ID=" + vehicleType.getId());
    }

    public void delete(VehicleModel vehicleModel) {
        if (vehicleModel == null) return;
        jdbcTemplate.execute("DELETE FROM vehicle_models WHERE ID=" + vehicleModel.getId());
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
        if (inputStream == null) return;
        Reader reader = new InputStreamReader(inputStream);

        try {
            br = new BufferedReader(reader);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");

            while ((line = br.readLine()) != null) {
                String[] vehicle = line.split(cvsSplitBy);

                Vehicle entry = new Vehicle();
                //todo

                entry.setCreated(new Date());
                entry.setUpdated(new Date());

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

    public void loadVechicleTypes(String fileName) {
        String csvFile;
        if(Strings.isNullOrEmpty(fileName))csvFile = "VechycleType.csv";
        else csvFile = fileName;
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
                entry.setCreated(new Date());
                entry.setUpdated(new Date());

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

    public void loadVechicleModels(String fileName) {

        String csvFile;
        if(Strings.isNullOrEmpty(fileName))csvFile = "VechycleModels.csv";
        else  csvFile = fileName;

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
                String[] vehicleModel = line.split(cvsSplitBy);

                VehicleModel entry = new VehicleModel();
                entry.setVehicleType(vehicleModel[1]);
                entry.setModel(vehicleModel[2]);
                entry.setPicture(vehicleModel[3]);
                entry.setCreated(new Date());
                entry.setUpdated(new Date());

                try {
                    insertModel(entry);
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
