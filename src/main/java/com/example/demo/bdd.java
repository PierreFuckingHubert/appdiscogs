package com.example.demo;

//taper pgAdmin dans finder
// mdp : supercool

import java.sql.*;

public class bdd {

    public static Connection connection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost:5432/Discogs";
            String user = "postgres";
            String passwd = "supercool";

            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");

        return conn;
    }

    public static void createTable(String tableName, Connection connection) throws SQLException {

        System.out.println("Name table " + tableName);

            Statement statement = connection.createStatement();

            String sql = "DELETE FROM "+tableName+"";
            statement.executeUpdate(sql);
            System.out.println(sql);

            sql = "CREATE TABLE IF NOT EXISTS "+ tableName +" (id SERIAL PRIMARY KEY, idrelease integer, artist VARCHAR (200), album VARCHAR (200),urldiscogs VARCHAR (200), urlapi VARCHAR (200), price integer, nbsale integer, noteprice integer, exc VARCHAR(20))";
            System.out.println(sql);
            statement.executeUpdate(sql);

            statement.close();
    }

}

