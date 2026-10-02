package com.movieticket;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/movie_booking";

    private static final String USER =
            System.getenv("MOVIE_DB_USER");

    private static final String PASSWORD =
            System.getenv("MOVIE_DB_PASSWORD");

    public static Connection getConnection() {

        try {
            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database connected successfully!");

            return connection;

        } catch (Exception e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();

            return null;
        }
    }
}