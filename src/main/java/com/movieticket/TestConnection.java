package com.movieticket;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection connection = DBConnection.getConnection();

        if (connection != null) {

            System.out.println("Connection test successful!");

            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            System.out.println("Connection test failed!");
        }
    }
}