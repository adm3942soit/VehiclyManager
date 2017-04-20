package com.adonis.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by oksdud on 19.04.2017.
 */
public class DatabaseUtils {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String databaseName = "carmanager";
    static final String USER = "root";
    static final String PASS = "root";
    static final String DB_URL = "jdbc:mysql://localhost/?user=" + USER + "&password=" + PASS;
    static final String createDatabaseSql = "CREATE DATABASE " + databaseName;


    public static boolean createDatabase() {
        //STEP 1: Register JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //STEP 2: Open a connection
        System.out.println("Connecting to database...");

        try(
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
        ) {
            //STEP 3: Execute a query
            System.out.println("Creating database...");
            stmt.executeUpdate(createDatabaseSql);
            System.out.println("Database created successfully...");

            stmt.execute(FileReader.readFromFileFromResources("address.sql"));
            System.out.println("Table address created successfully...");
            stmt.execute(FileReader.readFromFileFromResources("persons.sql"));
            System.out.println("Table persons created successfully...");
            stmt.execute(FileReader.readFromFileFromResources("types.sql"));
            System.out.println("Table types created successfully...");
            stmt.execute(FileReader.readFromFileFromResources("models.sql"));
            System.out.println("Table models created successfully...");
            stmt.execute(FileReader.readFromFileFromResources("vehicles.sql"));
            System.out.println("Table vehicles created successfully...");
            stmt.execute(FileReader.readFromFileFromResources("renta_history.sql"));
            System.out.println("Table renta_history created successfully...");

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
