package com.example.demo;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		String token = "vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD";
		String startUrlItem = "https://www.discogs.com";
		String startUrl = "https://api.discogs.com/database/search?";
		String format = "Vinyl";
		String perPage = "1000";
		String year = "1900";
		String country = "nigeria";
		String genre = "&genre=boogie";
		String style = "&style=boogie";
		String req = startUrl+"country="+country+"&type=release"+"&decade="+year+"&format="+format+"&token="+token+"&per_page="+perPage;

		//req = "https://api.discogs.com/database/search?country=nigeria&type=release&decade=1900&format=Vinyl&token=vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD&per_page=1000";
		//req = "https://api.discogs.com/database/search?country=UK&type=release&style=New+Jack+Swing&genre=Funk+%2F+Soul&format=Vinyl&token=vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD&per_page=1000";
		req = "https://api.discogs.com/database/search?country=UK&type=release&style=Swing&format=Vinyl&token=vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD&per_page=1000";

	//	token=vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD
	//	Consumer Key	OKtRrhlVeNZiduRjUnlr
	//	Consumer Secret	nNgdQzQwoaUwjqNYzeQEiKqyFLNmjRyD
	//	Request Token URL	https://api.discogs.com/oauth/request_token
	//	Authorize URL	https://www.discogs.com/oauth/authorize
	//	Access Token URL	https://api.discogs.com/oauth/access_token

		//http://www.discogs.com/users/ratings/pierrecool
		//http://www.discogs.com/users/pierrecool/wants

		Connection conn = bdd.connection();

		String collection = "https://api.discogs.com/users/pierrecool/collection/folders/0/releases?token="+token+"&per_page=100";
		String wantlist = "https://api.discogs.com/users/pierrecool/wants?token="+token+"&per_page=100";
		//Discogs.getCollection(conn, wantlist);
		//Discogs.triRaters(conn);



		bdd.getAllTablesNames();

		String ebayUrl = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=PierreHu-Discogs-PRD-d5f82ec3d-72388a54&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords=";
		//String filter = "&itemFilter.paramValue=USD";
		//String filter="";

		String filter="&categoryId(0)=176985";


		// Remplis la BDD en fonction d'une url
		//Discogs.addReleasesToBddFromRequest(conn, req);


	}

}
