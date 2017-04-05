package com.adonis.data.persons.service;

import com.adonis.data.persons.Person;
import com.adonis.utils.DateUtils;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
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
        List<Person> customers  = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Person.class));
        return customers;
    }

    public Person findByCustomerId(Long custId) {
        if(custId == null) return null;
        String sql = "SELECT * FROM persons WHERE ID = ?";

        Person customer = (Person) jdbcTemplate.queryForObject(
                sql, new Object[]{custId}, new BeanPropertyRowMapper(Person.class));

        return customer;
    }
    public Person findByCustomerLogin(String login) {
        if(Strings.isNullOrEmpty(login)) return null;
        String sql = "SELECT * FROM persons WHERE LOGIN = ?";

        Person customer = (Person) jdbcTemplate.queryForObject(
                sql, new Object[]{login}, new BeanPropertyRowMapper(Person.class));

        return customer;
    }

    public Person findByFirstLastNameEmail(String firstName, String lastName, String email) {
        Person person = (Person) jdbcTemplate.queryForObject(
                "SELECT * FROM persons p WHERE p.FIRST_NAME = ? and p.LAST_NAME = ? and p.EMAIL = ?",
                new Object[]{firstName, lastName, email}, new BeanPropertyRowMapper(Person.class)
        );
        return person;
    }
    public int findTotalCustomer(){

        String sql = "SELECT COUNT(*) FROM persons";

        int total = jdbcTemplate.queryForObject(sql,Integer.class);

        return total;
    }


    public void update(Person customer) {
        if( customer == null ) return;
            jdbcTemplate.update(
                    "UPDATE persons SET FIRST_NAME=?, LAST_NAME=?, EMAIL=? , LOGIN=?, PASSWORD=?, DATE_OF_BIRTH=?, PICTURE=?, NOTES=? " +
                            "WHERE ID=?",
                    customer.getFirstName(), customer.getLastName(), customer.getEmail(),
                    customer.getLogin(), customer.getPassword(), customer.getBirthDate(),
                    customer.getPicture(), customer.getNotes(),
                    customer.getId());
    }
    public void insert(Person customer) {
        if( customer == null ) return;
            jdbcTemplate.update(
                    "INSERT INTO persons (FIRST_NAME, LAST_NAME, EMAIL, LOGIN, PASSWORD," +
                            " PICTURE, NOTES, DATE_OF_BIRTH) VALUES " +
                            "(?,?,?,?,?,?,?,?)",
                    new Object[]{
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getEmail(),
                            customer.getLogin(),
                            customer.getPassword(),
                            customer.getPicture(),
                            customer.getNotes(),
                            customer.getBirthDate()

                    } );
    }

    public Person save(Person customer) {
        jdbcTemplate.update(
                "UPDATE persons SET FIRST_NAME=?, LAST_NAME=?, EMAIL=? , LOGIN=?, PASSWORD=?, DATE_OF_BIRTH=?, PICTURE=?, NOTES=? " +
                        "WHERE ID=?",
                customer.getFirstName(), customer.getLastName(), customer.getEmail(),
                customer.getLogin(), customer.getPassword(), customer.getBirthDate(),
                customer.getPicture(), customer.getNotes(),
                customer.getId());
    return findByCustomerId(customer.getId());
    }

    public void delete(Person customer) {
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

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");

            while ((line = br.readLine()) != null) {
                String[] person = line.split(cvsSplitBy);

                Person entry = new Person();

                entry.setFirstName(person[1]);
                entry.setLastName(person[2]);
                entry.setEmail(person[3]);
    			entry.setBirthDate(DateUtils.convertToDate(person[4]));
                entry.setRemind(Math.random() > 0.5);
                entry.setPicture(person[5]);
                entry.setNotes(person[6]);
                entry.setCreated(new Date());

                try {
                    insert(entry);
                } catch (Exception e) {
                    if(e.getMessage()!=null && e.getMessage().contains("Duplicate entry")){
                       break;
                    }
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
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
