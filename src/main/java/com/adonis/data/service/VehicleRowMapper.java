package com.adonis.data.service;

import com.adonis.data.persons.Person;
import com.adonis.data.vehicles.Vehicle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by oksdud on 10.04.2017.
 */
public class VehicleRowMapper implements RowMapper {
    VehicleService vehicleService;

    public VehicleRowMapper(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getLong("ID"));
        vehicle.setFotoAlbum(vehicleService.findByFotoAlbumId(rs.getLong("FOTO_ALBUM_ID")));
        vehicle.setActive(rs.getBoolean("ACTIVE"));
        vehicle.setLocation(rs.getString("LOCATION"));
        vehicle.setLicenseNmbr(rs.getString("LICENSE_NMBR"));
        vehicle.setMake(rs.getString("MAKE"));
        vehicle.setModel(rs.getString("MODEL"));
        vehicle.setPrice(rs.getDouble("PRICE"));
        vehicle.setPriceDay(rs.getDouble("PRICE_DAY"));
        vehicle.setPriceWeek(rs.getDouble("PRICE_WEEK"));
        vehicle.setPriceMonth(rs.getDouble("PRICE_MONTH"));
        vehicle.setStatus(rs.getString("STATUS"));
        vehicle.setVehicleNmbr(rs.getString("VEHICLE_NMBR"));
        vehicle.setVehicleType(rs.getString("VEHICLE_TYPE"));
        vehicle.setVinNumber(rs.getString("VIN_NMBR"));
        vehicle.setYear(rs.getLong("YEAR"));
        vehicle.setCreated(rs.getTimestamp("CREATED"));
        vehicle.setUpdated(rs.getTimestamp("UPDATED"));
        return vehicle;
    }
}
