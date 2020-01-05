package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.util.StringUtils;

public class Discogs {

    static String startUrlItem = "https://www.discogs.com";
    static int q;
    static int count = 0;
    static String yearParam = "&year=";
    static int year = 1977;
    static int yearEnd = 1997;

    public Discogs(){
    }

    //obtenir la réponse d'un
    public static void addReleasesToBddFromRequest(Connection conn, String req){

        String stringJ = null;
        req = req+yearParam+year;
        System.out.println(req);



        try {
            URL url = new URL(req);
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


        JSONObject JSON = new JSONObject();
        try {
            JSON = new JSONObject(stringJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray values = null;
        try {

            if((JSON.getJSONObject("pagination").getInt("pages") == JSON.getJSONObject("pagination").getInt("page") && JSON.getJSONObject("pagination").getInt("pages") != 1) || JSON.getJSONObject("pagination").getInt("items") == 0){
                // cas pas d'item ou une seule page)
                System.out.println("pas d'item ou une seule page "+req);
                req = req.replace(yearParam+year, "");
                String reqs[]  = req.split("&page=");
                req = reqs[0];
                year++;
                addReleasesToBddFromRequest(conn,req);

            }else if(year < yearEnd){
                if(JSON.getJSONObject("pagination").getInt("pages") != JSON.getJSONObject("pagination").getInt("page")){
                    // cas normal
                    System.out.println("normal "+req);
                    req = JSON.getJSONObject("pagination").getJSONObject("urls").getString("next");
                    req = req.replace(yearParam+year, "");
                }else{
                    // cas derniere page
                    System.out.println("derniere page "+req);
                    req = req.replace(yearParam+year, "");

                    year++;
                }

                System.out.println(req);
                values = JSON.getJSONArray("results");

                for (int i = 0; i < values.length(); i++) {

                    Object jsonRelease ="null";
                    jsonRelease = values.getJSONObject(i).get("master_url");
                   // System.out.println("jsonRelease : "+ jsonRelease);
                    // si master alors plusieurs release et sans doute des repress

                    System.out.println("Disque "+(count+i+1));


                    if(jsonRelease.toString() == "null"){

                        String urlRelease = values.getJSONObject(i).getString("uri");

                        int year = 0;

                        if(values.getJSONObject(i).has("year") && !values.getJSONObject(i).isNull("year")){
                            year = Integer.parseInt(values.getJSONObject(i).getString("year"));
                            System.out.println("year : "+ year);
                        }


                        int have = values.getJSONObject(i).getJSONObject("community").getInt("have");
                        int want = values.getJSONObject(i).getJSONObject("community").getInt("want");

                        if(want>45 && have<want){

                            Record record = addPricesToRecord(getHtmlFromUrl(startUrlItem+urlRelease));

                            String name = values.getJSONObject(i).getString("title");

                            name = name.replaceAll("--", " ");

                            System.out.println("LA : "+ name);

                            String names[]  = name.split("-");
                            names[1] = names[1].substring(1);
                            names[1] = names[1].replaceAll("'", " ");


                            String titles[] = urlRelease.split("/");
                            String title = titles[1];
                            //title = title.replaceAll("-", "&");

                            int ratio;


                            if(record != null){
                                System.out.println("Url : "+ urlRelease);
                                System.out.println("Artist : "+ names[0]);
                                System.out.println("Album : "+ names[1]);
                                System.out.println("Lower price : "+ record.getLowerPrice());
                                System.out.println("Median price : "+ record.getMedianPrice());
                                System.out.println("Higher price : "+ record.getHigherPrice());
                                System.out.println("Have : "+ have);
                                System.out.println("Want : "+ want);
                                //System.out.println("Ratio : "+ want);
                            }
                            System.out.println("-------------------------------------");


                            if(record != null && (record.getHigherPrice() > 100 || record.getHigherPrice() == -1)){

                                String artist = names[0].replaceAll("'", " ");
                                String album = names[1].replaceAll("'", " ");

                                Statement state;
                                try {
                                    state = conn.createStatement();
                                    state.executeUpdate("INSERT INTO record (category, title, artist, album, lowerprice, medianprice, higherprice, have, want) VALUES ('test','"+ title +"','"+ artist +"', '"+ album +"','"+ record.getLowerPrice() +"','"+ record.getMedianPrice() +"','"+ record.getHigherPrice() +"','"+ have +"','"+ want +"')");
                                    //state.executeUpdate("INSERT INTO record (title, artist, album, lowerprice, medianprice, higherprice, have, want) VALUES ('"+ title +"','"+ names[0] +"', '"+ names[1] +"','"+ record.getLowerPrice() +"','"+ record.getMedianPrice() +"','"+ record.getHigherPrice() +"','"+ have +"','"+ want +"')");

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }
                count = count + 100;
                addReleasesToBddFromRequest(conn,req);
            }else{
                System.out.println("FINI !!!!!!!!!!!!!!!!!");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static Record getRecord(String req){
        String stringJ = null;

        String startreq = "https://api.discogs.com/releases/";

        String[] parts = req.split("/");

        req = startreq + parts[parts.length-1];


        try {
            URL url = new URL(req);
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
            e.printStackTrace();
        }


        System.out.println("stringJ : " + stringJ);


        JSONObject JSON = new JSONObject();
        try {
            JSON = new JSONObject(stringJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("JSON : " + JSON);

        Record record = null;
        try {

            String uri = JSON.getString("uri");
            System.out.println("URI : " + uri);
            System.out.println("Title : " + parts[parts.length-3]);


            record = addPricesToRecord(getHtmlFromUrl(uri));
            record.setTitle(parts[parts.length-3]);
            record.setUrlDiscogs(JSON.getString("uri"));
            record.setHave(JSON.getJSONObject("community").getInt("have"));
            record.setWant(JSON.getJSONObject("community").getInt("want"));

            String artist = JSON.getJSONArray("artists").getJSONObject(0).getString("name").replaceAll("'", " ");
            String[] artists = artist.split("\\(");
            artist = artists[0];
            record.setArtist(artist);

            String album = JSON.getString("title").replaceAll("'", " ");
            record.setAlbum(album);

            System.out.println("Title : " + record.getTitle());
            System.out.println("HAVE : " + record.getHave());
            System.out.println("WANT : " + record.getWant());
            System.out.println("NAME : " + record.getArtist());
            System.out.println("ALbum : " + record.getAlbum());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return record;

    }



    public static String getHtmlFromUrl(String uri) {

        String htmlReleasePage = null;
        try {
            URL url = new URL(uri);

            HttpURLConnection uc = (HttpURLConnection)url.openConnection();

            String string = "your bot 0.1"+(q+1);

            uc.setRequestProperty("User-agent", string);

            InputStream in = null;
            try
            {
                in = uc.getInputStream();
            }
            catch (Exception e)
            {
                System.out.println("ERROR : "+e);

            }

            //java.io.IOException: Server returned HTTP response code: 429 for URL: (if code 429 : check the header for the waiting time, then wait and relauch the request)
            int c = in.read();
            StringBuilder build = new StringBuilder();
            while (c != -1) {
                build.append((char) c);
                c = in.read();
            }
            htmlReleasePage = build.toString();
            uc.disconnect();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return htmlReleasePage;
    }

    public static Record addPricesToRecord(String html){

        String low, med, high = null;

        Document doc = Jsoup.parse(html);
        Element price, higher, median, lower;
        price = doc.select("ul.last").first();
        if(price == null){
            System.out.println("HTML CASSE :(");
            return null;
        }else{

            lower = price.select("li:eq(1)").first().select("h4").first();
            median = price.select("li:eq(2)").first().select("h4").first();
            higher = price.select("li:eq(3)").first().select("h4").first();

            low = lower.nextSibling().toString();
            med = median.nextSibling().toString();
            high = higher.nextSibling().toString();

            if(low.contains(",")){low = low.replaceAll(",", "");}
            if(med.contains(",")){med = med.replaceAll(",", "");}
            if(high.contains(",")){high = high.replaceAll(",", "");}

            low = low.substring(4,low.length());
            med = med.substring(4,med.length());
            high = high.substring(4,high.length());


            Record record = new Record();

            if(high.length() != 0){
                Double l = Double.parseDouble(low);
                Double m = Double.parseDouble(med);
                Double h = Double.parseDouble(high);

                int lowerPrice, medianPrice, higherPrice;
                lowerPrice = l.intValue();
                medianPrice = m.intValue();
                higherPrice = h.intValue();

                record.setLowerPrice(lowerPrice);
                record.setMedianPrice(medianPrice);
                record.setHigherPrice(higherPrice);
            }else{
                record.setLowerPrice(-1);
                record.setMedianPrice(-1);
                record.setHigherPrice(-1);
            }

            return record;
        }
    }

    public static String findHigherPrice2(String html){
        String higherPrice = null;

        String balise = "<h4>Highest:</h4>";

        int begin = (html.indexOf(balise))+37;
        int end = html.indexOf(" ", begin);

        if(end == begin){
            return "Pas de prix";
        }
        else{
            higherPrice = html.substring(begin, end);
            return higherPrice;
        }

    }

}
