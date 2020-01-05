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
import org.springframework.util.StringUtils;

public class Ebay {

    public static void getJSON(String search, Record record){

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
       //     uc.disconnect();

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

        System.out.println("stringJ : " + stringJ);
        if(stringJ != null) {

            JSONObject JSON = new JSONObject();
            try {
                JSON = new JSONObject(stringJ);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray values = null;
            try {

                values = JSON.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
                int nbItems = Integer.parseInt(JSON.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getString("@count"));

                if (nbItems < 4) {
                    for (int i = 0; i < values.length(); i++) {

                        String url = (String) values.getJSONObject(i).getJSONArray("viewItemURL").get(0);
                        //StringUtils.countOccurrencesOf(url.toLowerCase(), "new")
                        if (!url.toLowerCase().contains("reissue") && !url.toLowerCase().contains("new")) {
                            String p = (String) values.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("__value__");
                            Double price = Double.parseDouble(p);

                            if (((record.getHigherPrice() > 50) || (record.getHigherPrice()  == -1)) && (((record.getHigherPrice()  / price) > 2) || ((record.getHigherPrice()  / price) < 0)) || record.getHigherPrice()  == 0) {
                             //   if (true) {

                                System.out.println(url);
                                System.out.println(price);
                                System.out.println(values.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("@currencyId"));
                                System.out.println("HigherPrice : " + record.getHigherPrice() );
                                System.out.println("==================================");

                                List<String> lignes = new ArrayList<>();

                                String listingType = (String) values.getJSONObject(i).getJSONArray("listingInfo").getJSONObject(0).getJSONArray("listingType").get(0);


                                lignes.add("Type sell : " + listingType);
                                lignes.add("Recherche : " + search);
                                lignes.add(url);
                                lignes.add("HigherPrice : " + record.getHigherPrice() );
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
}
