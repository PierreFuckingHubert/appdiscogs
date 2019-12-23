package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ebay {

    public static void getJSON(String search, int higherprice){

        String stringJ = null;
        String Urlsearch = search;

        String ebayUrl = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=PierreHu-Discogs-PRD-d5f82ec3d-72388a54&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords=";

        //category vinyl dans ebay
        String filter="&categoryId(0)=176985";

        Urlsearch = Urlsearch.replaceAll("-", "%20");
        Urlsearch = Urlsearch.replaceAll(" ", "%20");
        Urlsearch = Urlsearch.replaceAll("&", "%20");
        Urlsearch = ebayUrl+Urlsearch+filter;

        System.out.println("Urlsearch : "+Urlsearch);


        String desktop = System.getProperty ("user.home") + "/Documents/";

        Path fichier = Paths.get(desktop + "MyFile.txt");

        try {
            URL url = new URL(Urlsearch);

            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            int c = in.read();
            StringBuilder build = new StringBuilder();
            while (c != -1) {
                build.append((char) c);
                c = in.read();
            }
            stringJ = build.toString();

        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

        }

       // System.out.println("stringJ : " + stringJ);


        JSONObject JSON = new JSONObject();
        try {
            JSON = new JSONObject(stringJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray values = null;
        try {

            values = JSON.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");

            if(values.length()<5) {
                for (int i = 0; i < values.length(); i++) {

                    String url = (String) values.getJSONObject(i).getJSONArray("viewItemURL").get(0);

                    if (!url.contains("reissue")) {
                        String p = (String) values.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("__value__");
                        //Double price = p;
                        Double price = Double.parseDouble(p);

                        //if (((higherprice > 50) || (higherprice == -1)) && (((higherprice / price) > 2) || ((higherprice / price) < 0)) || higherprice == 0) {
                        if (true) {

                        System.out.println(url);
                            System.out.println(price);
                            System.out.println(values.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("@currencyId"));
                            System.out.println("HigherPrice : " + higherprice);
                            System.out.println("==================================");

                            List<String> lignes = new ArrayList<>();

                            lignes.add("Recherche : " + search);
                            lignes.add(url);
                            lignes.add("Prix : " + price.toString());
                            lignes.add("==================================");

                            try {
                                Files.write(fichier, lignes, Charset.forName("UTF-8"), StandardOpenOption.APPEND);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }


    }
}
