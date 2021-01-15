package com.example.demo;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoApplication {



	public static String token = "vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD";

	//https://api.discogs.com/users/pierrecool/wants?token=vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD&per_page=100
	//String collection = "https://api.discogs.com/users/pierrecool/collection/folders/0/releases?token=" + token + "&per_page=100";
	public static String wantlist = "https://api.discogs.com/users/pierrecool/wants?token=" + token + "&per_page=500";
	public static String nameTable = "wantlist";


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		searchNewWantsRSS();

	}

	public static void searchNewWantsRSS(){
		try {
			Connection connection = bdd.connection();
			//bdd.createTable(nameTable,connection);

		//	Discogs.getWantlist(connection, wantlist,nameTable);
			Discogs.getXML("https://www.discogs.com/sell/mpmywantsrss?output=rss&user=pierrecool",connection, nameTable, null);

			connection.close();

		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("1 : "+e);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("2 : "+e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("3 : "+e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("4 : "+e);
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("5 : "+e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("6 : "+e);
		}
	}

}


