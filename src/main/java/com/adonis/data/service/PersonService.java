package com.adonis.data.service;

import com.adonis.data.persons.Address;
import com.adonis.data.persons.Person;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by oksdud on 29.03.2017.
 */
@Component
public class PersonService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Person> findAll() {
        String sql = "SELECT * FROM persons";
        List<Person> customers = null;
        try {
            customers = jdbcTemplate.query(sql,
                    new PersonRowMapper(this));
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return customers;
    }

    public List<String> findAllNames() {
        List<Person> customers = findAll();
        if (customers == null || customers.isEmpty()) return Collections.EMPTY_LIST;
        List<String> names = Lists.newArrayList();
        customers.forEach(person -> {
            names.add(person.getName());//person.getFirstName() + " " + person.getLastName()
        });
        return names;
    }

    public Address findByAddressId(Long id) {
        if (id == null) return null;
        String sql = "SELECT * FROM address WHERE ID = ?";
        Address address = null;
        try {
            address = (Address) jdbcTemplate.queryForObject(
                    sql, new Object[]{id}, new BeanPropertyRowMapper(Address.class));
        } catch (Exception e) {
            return null;
        }

        return address;


    }

    public Person findByCustomerId(Long custId) {
        if (custId == null) return null;
        String sql = "SELECT * FROM persons WHERE ID = ?";

        Person customer = null;
        try {
            customer = (Person) jdbcTemplate.queryForObject(
                    sql, new Object[]{custId}, new PersonRowMapper(this));
        } catch (Exception e) {
            return null;
        }

        return customer;
    }

    public Person findByCustomerLogin(String login) {
        if (Strings.isNullOrEmpty(login)) return null;
        if (findTotalCustomer() == 0) return null;

        String sql = "SELECT * FROM persons WHERE LOGIN = ?";

        Person customer = null;
        try {
            customer = (Person) jdbcTemplate.queryForObject(
                    sql, new Object[]{login}, new PersonRowMapper(this));
        } catch (Exception e) {
            return null;
        }

        return customer;
    }

    public Person findByFirstLastNameEmail(String firstName, String lastName, String email) {
        Person person = null;
        try {
            person = (Person) jdbcTemplate.queryForObject(
                    "SELECT * FROM persons p WHERE p.FIRST_NAME = ? AND p.LAST_NAME = ? AND p.EMAIL = ?",
                    new Object[]{firstName, lastName, email}, new PersonRowMapper(this)
            );
        } catch (Exception e) {
            return null;
        }
        return person;
    }

    public int findTotalCustomer() {

        String sql = "SELECT COUNT(*) FROM persons";

        int total = jdbcTemplate.queryForObject(sql, Integer.class);

        return total;
    }


    public void update(Person customer) {
        if (customer == null) return;
        jdbcTemplate.update(
                "UPDATE persons SET FIRST_NAME=?, LAST_NAME=?, EMAIL=? , LOGIN=?, PASSWORD=?, " +
                        "BIRTH_DATE=?, PICTURE=?, NOTES=?, NAME=?, UPDATED=? " +
                        "WHERE ID=?",
                customer.getFirstName(), customer.getLastName(), customer.getEmail(),
                customer.getLogin(), customer.getPassword(), customer.getBirthDate(),
                customer.getPicture(), customer.getNotes(), customer.getFirstName().trim()+" "+customer.getLastName().trim(),
                new Date(),
                customer.getId());
    }

    public Address findLastAddress() {
        String sql = "SELECT * FROM address ORDER BY ID DESC LIMIT 1";
        Address address;
        try {
            address = (Address) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new BeanPropertyRowMapper(Address.class));
        } catch (Exception e) {
            return null;
        }
        return address;
    }

    public Person findLast() {
        String sql = "SELECT * FROM persons ORDER BY ID DESC LIMIT 1";

        Person person = null;
        try {
            person = (Person) jdbcTemplate.queryForObject(
                    sql, new Object[]{}, new PersonRowMapper(this));
        } catch (Exception e) {
            return null;
        }

        return person;
    }

    public Address insert(Address address) {
        if (address == null) return null;
        try {
            jdbcTemplate.update(
                    "INSERT INTO address (STREET, ZIP, CITY, COUNTRY, CREATED, UPDATED)" +
                            "  VALUES " +
                            "(?,?,?,?, ?, ?)",
                    new Object[]{
                            address.getStreet(),
                            address.getZip(),
                            address.getCity(),
                            address.getCountry(),
                            new Date(), new Date()
                    });
        } catch (DataAccessException e) {
            return null;
        }
        return findLastAddress();
    }

    public Person insert(Person customer) {
        if (customer == null) return null;
        Address address = null;
        try {
            if (customer.getAddress() != null) address = insert(customer.getAddress());
            jdbcTemplate.update(
                    "INSERT INTO persons (FIRST_NAME, LAST_NAME, EMAIL, LOGIN, PASSWORD," +
                            " PICTURE, NOTES, BIRTH_DATE, GENDER, PHONE_NUMBER, ADDRESS, NAME, CREATED, UPDATED) VALUES " +
                            "(?,?,?,?,?,?,?,?, ?, ?,?, ?, ?, ?)",
                    new Object[]{
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getEmail(),
                            customer.getLogin(),
                            customer.getPassword(),
                            customer.getPicture(),
                            customer.getNotes(),
                            customer.getBirthDate(),
                            customer.getGender(),
                            customer.getPhoneNumber(),
                            address != null ? address.getId() : null,
                            customer.getFirstName().trim()+" "+customer.getLastName().trim(),
                            new Date(), new Date()

                    });
        } catch (DataAccessException e) {
            return null;
        }
        return findLast();
    }

    public Person save(Person customer) {
        if (customer == null) return null;
        Address address = null;
        try {
            if (customer.getAddress() != null) address = insert(customer.getAddress());
            jdbcTemplate.update(
                    "UPDATE persons SET FIRST_NAME=?, LAST_NAME=?, EMAIL=? , LOGIN=?, PASSWORD=?, " +
                            "BIRTH_DATE=?, PICTURE=?, NOTES=?, GENDER=?, PHONE_NUMBER=?, ADDRESS=?, NAME=?, UPDATED=? " +//
                            "WHERE ID=?",
                    new Object[]{
                    customer.getFirstName(), customer.getLastName(), customer.getEmail(),
                    customer.getLogin(), customer.getPassword(), customer.getBirthDate(),
                    customer.getPicture(), customer.getNotes(), customer.getGender(), customer.getPhoneNumber(),
                    address != null ? address.getId() : null,
                    customer.getFirstName().trim()+" "+customer.getLastName().trim(),
                    new Date(),
                    customer.getId()}//, Person.class
            );
        } catch (DataAccessException e) {
            return null;
        }
        return findByCustomerId(customer.getId());
    }

    public void delete(Person customer) {
        if (customer == null) return;
        jdbcTemplate.execute("DELETE FROM persons WHERE ID=" + customer.getId());
    }


    public void loadData() {

        String csvFile = "Persons.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFile);
        Reader reader = new InputStreamReader(inputStream);

        try {
            br = new BufferedReader(reader);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");

            while ((line = br.readLine()) != null) {
                String[] person = line.split(cvsSplitBy);

                Person entry = new Person();

                entry.setFirstName(person[1]);
                entry.setLastName(person[2]);
                entry.setEmail(person[3]);
                try {
                    entry.setBirthDate(sdf.parse(person[4]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                entry.setRemind(Math.random() > 0.5);
                if (person.length == 6) entry.setPicture(person[5]);
                if (person.length == 7) entry.setNotes(person[6]);
                entry.setAddress(new Address());
                entry.setPhoneNumber("");
                entry.setGender("");
                entry.setName(entry.getFirstName()+" "+entry.getLastName());
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

}
