package com.adonis.data.service;

import com.adonis.data.persons.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by oksdud on 10.04.2017.
 */
public class PersonRowMapper implements RowMapper {
    PersonService personService;

    public PersonRowMapper(PersonService personService) {
        this.personService = personService;
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person customer = new Person();
        customer.setId(rs.getLong("ID"));
        customer.setAddress(personService.findByAddressId(rs.getLong("ADDRESS")));
        customer.setFirstName(rs.getString("FIRST_NAME"));
        customer.setLastName(rs.getString("LAST_NAME"));
        customer.setEmail(rs.getString("EMAIL"));
        customer.setGender(rs.getString("GENDER"));
        customer.setLogin(rs.getString("LOGIN"));
        customer.setPassword(rs.getString("PASSWORD"));
        customer.setPhoneNumber(rs.getString("PHONE_NUMBER"));
        customer.setPicture(rs.getString("PICTURE"));
        customer.setNotes(rs.getString("NOTES"));
        customer.setBirthDate(rs.getDate("BIRTH_DATE"));
        customer.setCreated(rs.getDate("CREATED"));
        customer.setUpdated(rs.getDate("UPDATED"));
        customer.setName(rs.getString("NAME"));
        return customer;
    }
}
