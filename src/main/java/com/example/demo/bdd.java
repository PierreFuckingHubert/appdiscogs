package com.example.demo;

//taper pgAdmin dans finder
// mdp : supercool

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class bdd {

    public static Connection connection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost:5432/Discogs";
            String user = "postgres";
            String passwd = "supercool";

            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static String createTable(String tableName){
        Statement statement;
        Connection connection = connection();
        ResultSet rs = null;

        System.out.println("Name table " + tableName);


        try {
            statement = connection.createStatement();

            //String sql = "CREATE TABLE "+ tableName +"(id INT SERIAL PRIMARY KEY,title VARCHAR (100),artist VARCHAR (100),album VARCHAR (100),lowerprice integer,medianprice integer,higherprice integer,have integer,want integer)");

            String sql = "CREATE TABLE IF NOT EXISTS "+ tableName +" (id SERIAL PRIMARY KEY, category VARCHAR (100), title VARCHAR (100),artist VARCHAR (100),album VARCHAR (100),urldiscogs VARCHAR (200),lowerprice integer,medianprice integer,higherprice integer,have integer,want integer, style VARCHAR (200), note integer, raters VARCHAR (200), date integer)";
            System.out.println(sql);


            statement.executeUpdate(sql);
            statement.close();
            connection.close();
            //state.executeQuery("CREATE TABLE YO (id SERIAL PRIMARY KEY,title VARCHAR (100)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    public static String deleteTable(String tableName){
        Statement statement;
        Connection connection = connection();
        ResultSet rs = null;

        System.out.println("Name table " + tableName);


        try {
            statement = connection.createStatement();

            //String sql = "DELETE TABLE "+ tableName +"(id INT SERIAL PRIMARY KEY,title VARCHAR (100),artist VARCHAR (100),album VARCHAR (100),lowerprice integer,medianprice integer,higherprice integer,have integer,want integer)");

            String sql = "DROP TABLE "+ tableName +"";
            System.out.println(sql);



            statement.executeUpdate(sql);
            statement.close();
            connection.close();
            //state.executeQuery("CREATE TABLE YO (id SERIAL PRIMARY KEY,title VARCHAR (100)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    public static void searchOnEbay(String category){

        Statement state;
        try {

            String desktop = System.getProperty ("user.home") + "/Documents/";

                Path fichier = Paths.get(desktop + "MyFile.txt");
                List<String> ligne = Arrays.asList("");
                Files.write(fichier, ligne, Charset.forName("UTF-8"));



//            Desktop desktop = Desktop.getDesktop();
           // if(file.exists()) desktop.open(file);
            } catch (IOException e1) {
            e1.printStackTrace();
        }


        try {
            state = connection().createStatement();

           // ResultSet lastIdRelease = state.executeQuery("SELECT MAX(ID) from record where category = 'test'");
            ResultSet lastIdRelease = state.executeQuery("SELECT ID FROM record where category = 'test' ORDER BY ID DESC");
            lastIdRelease.next();
            int lastId = lastIdRelease.getInt("id");


            ResultSet firstIdRelease = state.executeQuery("SELECT ID FROM record where category = 'test' ORDER BY ID ASC");
            firstIdRelease.next();
            int firstId = firstIdRelease.getInt("id");


            for(int i = firstId; i <= lastId; i++){
                System.out.println(i);

                ResultSet rs = state.executeQuery("SELECT title, artist, album, higherprice, have, want from record where id ="+ i +" and category = 'test'");
                while (rs.next()) {

                    String search;

                //    if(!rs.getString("title").equals(rs.getString("artist"))){
                //        search = rs.getString("artist")+" "+rs.getString("album");
                //    }else{
                        search = rs.getString("title");
                //    }

                    Record record = new Record();
                    record.setHigherPrice(rs.getInt("higherprice"));
                    record.setHave(rs.getInt("have"));
                    record.setWant(rs.getInt("want"));

                    Ebay.getJSON(search,record);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }





    }

    public static String addRecord(UrlRecord data) {
        System.out.println("UrlRecord :"+ data.url);
        String query;

        if(data.url.indexOf("www.discogs.com") == -1){
            query = "INSERT INTO record (category, title, artist, album, urldiscogs) VALUES ('"+ data.category +"','"+ data.url +"','"+ data.url +"', '"+ data.url +"', '"+ data.url +"')";
        }else{
            Record record = Discogs.getRecord(data.url);
            query = "INSERT INTO record (category, title, artist, album, urldiscogs, lowerprice, medianprice, higherprice, have, want) VALUES ('"+ data.category +"','"+ record.getTitle() +"','"+ record.getArtist() +"', '"+ record.getAlbum() +"','"+ record.getUrlDiscogs() +"','"+ record.getLowerPrice() +"','"+ record.getMedianPrice() +"','"+ record.getHigherPrice() +"','"+ record.getHave() +"','"+ record.getWant() +"')";
        }


        Statement state;

        try {
            state = connection().createStatement();
            state.executeUpdate(query);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Success";
    }


    public static ArrayList<String> getAllTablesNames(){

        ArrayList<String> names = new ArrayList<String>();
        Statement state;

        try {
            state = connection().createStatement();
            ResultSet rs = state.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
            while (rs.next()) {
                names.add(rs.getString(1));
                System.out.println("Table name :"+ rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static ArrayList<String> getCategories(){

        ArrayList<String> names = new ArrayList<String>();
        Statement state;

        try {
            state = connection().createStatement();
            ResultSet rs = state.executeQuery("SELECT DISTINCT category FROM record");
            while (rs.next()) {
                names.add(rs.getString(1));
                System.out.println("Category name :"+ rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }



}

