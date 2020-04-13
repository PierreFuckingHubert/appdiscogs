package com.example.demo;

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
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Discogs {

    static String startUrlItem = "https://www.discogs.com";
    static int q;
    static int count;
    static String yearParam = "&year=";
    static int year;
    static int yearEnd;

    public Discogs(){
    }


    public static JSONObject getJSON(String req){
        System.out.println("req : "+req);

        String stringJ = null;
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

        if(stringJ != null){
            try {
                JSON = new JSONObject(stringJ);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return JSON;
    }

    public static void addReleasesToBddFromRequest(Connection conn, SearchObj searchObj, boolean isRated){

        year = 1988;
        yearEnd = 1997;
        count = 0;

        int ratersNumber = 170;
        String[][] listTopRaters = new String[ratersNumber][ratersNumber];
        //String listTopRaters[][] = {{"chrisbonato", "exte82", "Intercourse" , "lonerg","graeme_w","leolyxxx","wdjc","timetodiy","zc8","mostfaded","TheGuyWithSunglasses","daveambassador","denlekke","dmp","Silent_Chris","PHAROAH-FUNKYSAT","ZuluArt","Vivi0","onlyaudiophile "},{"2","1","3","1","2","2","3","2","1","1","1","2","1","1","1","1","1","1","2"}};

        if(isRated){
            Statement state;
            try {

                state = conn.createStatement();
                ResultSet rs = state.executeQuery("SELECT * FROM raters ORDER BY point DESC LIMIT '"+ratersNumber+"'");

                int i = 0;
                while(rs.next()){
                    listTopRaters[0][i] = rs.getString("name");
                    listTopRaters[1][i] = "1";
                    i++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        addReleasesToBdd(conn,searchObj, isRated, listTopRaters);

    }


    public static void addReleasesToBdd(Connection conn, SearchObj searchObj, boolean isRated, String[][] listTopRaters){

        String req = searchObj.request;
        req = req+yearParam+year;

        searchObj.setRequest(req);
        System.out.println(req);

        JSONObject JSON = getJSON(req);



        JSONArray values = null;
        try {

            if((JSON.getJSONObject("pagination").getInt("pages") == JSON.getJSONObject("pagination").getInt("page") && JSON.getJSONObject("pagination").getInt("pages") != 1) || JSON.getJSONObject("pagination").getInt("items") == 0){
                // cas pas d'item ou une seule page)
                System.out.println("pas d'item ou une seule page "+req);
                req = req.replace(yearParam+year, "");
                String reqs[]  = req.split("&page=");
                req = reqs[0];
                year++;
                searchObj.setRequest(req);
                addReleasesToBdd(conn, searchObj, isRated, listTopRaters);

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


                    if(jsonRelease.toString() == "null" || isRated){

                        System.out.println("json record : "+ values.getJSONObject(i));
                        String urlRelease = values.getJSONObject(i).getString("uri");


                        int year = 0;

                        if(values.getJSONObject(i).has("year") && !values.getJSONObject(i).isNull("year")){
                            year = Integer.parseInt(values.getJSONObject(i).getString("year"));
                        }


                        int have = values.getJSONObject(i).getJSONObject("community").getInt("have");
                        int want = values.getJSONObject(i).getJSONObject("community").getInt("want");



                        if(isRated && ((want - have ) > 5) && have < 80){
                            String idRelease = values.getJSONObject(i).getString("id");

                            RateObj rateObj = ratedRecord(conn, getHtmlFromUrl(startUrlItem+"/release/stats/"+idRelease),listTopRaters);


                            if(rateObj.getRate() > 0){

                                urlRelease = "https://www.discogs.com"+urlRelease;
                                String style = "";


                                for(int k = 0; k< values.getJSONObject(i).getJSONArray("genre").length(); k ++){
                                    style = style + values.getJSONObject(i).getJSONArray("genre").get(k).toString()+ ",";
                                }
                                for(int l = 0; l< values.getJSONObject(i).getJSONArray("style").length(); l ++){
                                    style = style + values.getJSONObject(i).getJSONArray("style").get(l).toString()+ ",";
                                }

                                System.out.println("rateObj.getRate() : " + rateObj.getRate());
                                System.out.println("rateObj.getRaters() : " + rateObj.getRaters());

                                Statement state;
                                try {
                                    state = conn.createStatement();
                                    state.executeUpdate("INSERT INTO raterecords3 (category, title, have, want, style, note, raters) VALUES ('test','" + urlRelease + "','" + have + "','" + want + "','" + style + "','" + rateObj.getRate() + "','" + rateObj.getRaters() + "')");

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }


                        }else if(!isRated){

                            if (want > 45 && have < want) {

                                Record record = addPricesToRecord(getHtmlFromUrl(startUrlItem + urlRelease));


                                String name = values.getJSONObject(i).getString("title");

                                name = name.replaceAll("--", " ");

                                String names[] = name.split("-");
                                names[1] = names[1].substring(1);
                                names[1] = names[1].replaceAll("'", " ");


                                String titles[] = urlRelease.split("/");
                                String title = titles[1];
                                //title = title.replaceAll("-", "&");

                                int ratio;


                                if (record != null) {
                                    System.out.println("Url : " + urlRelease);
                                    System.out.println("Artist : " + names[0]);
                                    System.out.println("Album : " + names[1]);
                                    System.out.println("Lower price : " + record.getLowerprice());
                                    System.out.println("Median price : " + record.getMedianprice());
                                    System.out.println("Higher price : " + record.getHigherprice());
                                    System.out.println("Have : " + have);
                                    System.out.println("Want : " + want);
                                    //System.out.println("Ratio : "+ want);
                                }
                                System.out.println("-------------------------------------");


                                if (record != null && (record.getHigherprice() > 100 || record.getHigherprice() == -1)) {

                                    String artist = names[0].replaceAll("'", " ");
                                    String album = names[1].replaceAll("'", " ");

                                    Statement state;
                                    try {
                                        state = conn.createStatement();
                                        state.executeUpdate("INSERT INTO record (category, title, artist, album, lowerprice, medianprice, higherprice, have, want) VALUES ('test','" + title + "','" + artist + "', '" + album + "','" + record.getLowerprice() + "','" + record.getMedianprice() + "','" + record.getHigherprice() + "','" + have + "','" + want + "')");
                                        //state.executeUpdate("INSERT INTO record (title, artist, album, lowerprice, medianprice, higherprice, have, want) VALUES ('"+ title +"','"+ names[0] +"', '"+ names[1] +"','"+ record.getLowerPrice() +"','"+ record.getMedianPrice() +"','"+ record.getHigherPrice() +"','"+ have +"','"+ want +"')");

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                }
                count = count + 100;
                searchObj.setRequest(req);
                addReleasesToBdd(conn,searchObj, isRated, listTopRaters);
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

        JSONObject JSON = getJSON(req);

        System.out.println("JSON : " + JSON);

        Record record = null;
        try {

            String uri = JSON.getString("uri");
            System.out.println("URI : " + uri);
            System.out.println("Title : " + parts[parts.length-3]);


            record = addPricesToRecord(getHtmlFromUrl(uri));
            record.setTitle(parts[parts.length-3]);
            record.setUrldiscogs(JSON.getString("uri"));
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



    public static RateObj ratedRecord(Connection conn, String html, String[][] listTopRaters){


        Document doc = Jsoup.parse(html);
        Element element = doc.select(".release_stats_group").first();

        RateObj rateObj = new RateObj(0, "");

        if(element != null){
            String el = doc.select(".release_stats_group").first().toString();
            boolean haveRatings = el.contains("Ratings");
            if(haveRatings){
                for(int i=0; i< listTopRaters[0].length; i++){
                    if(el.contains(listTopRaters[0][i])){
                        rateObj.setRate(rateObj.getRate() + Integer.parseInt(listTopRaters[1][i]));
                        rateObj.setRaters(rateObj.getRaters()+listTopRaters[0][i]+", ");
                    }
                }
            }
        }

        return rateObj;
    }

    public static String getCollection(Connection conn, String req){
        String stringJ = null;
        System.out.println("req : "+req);

        JSONObject JSON = getJSON(req);

        System.out.println("JSON : "+JSON);

        try {
            //JSONArray values = JSON.getJSONArray("releases");
            JSONArray values = JSON.getJSONArray("wants");
            for (int i = 0; i < values.length(); i++) {
                String url = "https://www.discogs.com/release/stats/"+values.getJSONObject(i).get("id");

                Document doc = Jsoup.parse(getHtmlFromUrl(url));

                Element element = doc.select(".release_stats_group").first();

                if(element != null){
                    String el = doc.select(".release_stats_group").first().toString();
                    if(el.contains("Ratings")){
                        int size = doc.select(".release_stats_group").first().select(".linked_username").size();
                        for (int j = 0; j < size; j++) {
                            String name = doc.select(".release_stats_group").first().select(".linked_username").get(j).html();
                            System.out.println("name : "+j+" : "+name);
                            Statement state;
                            try {


                                state = conn.createStatement();
                                ResultSet rs = state.executeQuery("SELECT * FROM raters WHERE name = '"+name+"'");

                                if(rs.next()){
                                    int point = rs.getInt(3)+1;
                                    state.executeUpdate("UPDATE raters SET point = '"+point+"' WHERE name = '"+rs.getString(2)+"'");
                                }else{
                                    state.executeUpdate("INSERT INTO raters (name,point) VALUES ('" + name + "','" + 2 + "')");
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

            }
            req = JSON.getJSONObject("pagination").getJSONObject("urls").getString("next");
            System.out.println("next page : "+req);
            getCollection(conn, req);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return "";
    }

    public static void triRaters(Connection conn){
        Statement state;
        try {

            state = conn.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM raters ORDER BY point DESC LIMIT 300");
            while(rs.next()){

                String name = rs.getString("name");
                String reqUSer = "https://api.discogs.com/users/"+name+"?token=vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD";
                JSONObject user = getJSON(reqUSer);

                if(user.length() == 0){
                    try {
                        Thread.sleep(20000) ;
                        user = getJSON(reqUSer);
                    }  catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(user.getInt("releases_rated")>10000){
                        state = conn.createStatement();
                        System.out.println("releases_rated : "+name+"  =>  "+user.getInt("releases_rated"));
                        state.executeUpdate("DELETE FROM raters WHERE name = '"+name+"'");
                    }
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
        e.printStackTrace();
    }
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

                record.setLowerprice(lowerPrice);
                record.setMedianprice(medianPrice);
                record.setHigherprice(higherPrice);
            }else{
                record.setLowerprice(-1);
                record.setMedianprice(-1);
                record.setHigherprice(-1);
            }

            return record;
        }
    }

}
