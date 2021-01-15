package com.example.demo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;


public class Discogs {


    public Discogs(){
    }


public static void getXML(String req, Connection conn, String nameTable, Date lastTimeDate) throws ParseException, SQLException, IOException, InterruptedException, JSONException {
        

    URL obj = new URL(req);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    //optional default is GET
    con.setRequestMethod("GET");

    //add request header
    con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.0)");
    con.addRequestProperty("cookie", "sid=86a166f5a7e4a81cd1b198770c9e728e; usprivacy=1---; OneTrustWPCCPAGoogleOptOut=false; _ga=GA1.2.1521005215.1583601585; mp_session=0e82a3d94bf415a6718c635d; OptanonAlertBoxClosed=2020-08-15T09:03:28.508Z; eupubconsent-v2=CO4LHCxO4LHCxAcABBENAzCsAP_AAH_AACiQGVtf_X9fb2vj-_5999t0eY1f9_63t-wzjgeNs-8NyZ_X_J4Xr2MyvB34pqYKmR4EunLBAQdlHGHcTQgAwIkVqTLsYk2MizNKJ7JEilMbM2dYGG1vn8XTuZCY70-sf__zv3-_-___6oGUEEmGpfAQJCWMBJNmlUKIEIVxIVAOACihGFo0sNCRwU7I4CPUACABAYgIQIgQYgohZBAAIAAElEQAgAwIBEARAIAAQAjQEIACJAEFgBIGAQACoGhYARRBKBIQYHBUcogQFSLRQTzAAAAA.YAAAAAAAAAAA; _gcl_au=1.1.1238447013.1600707986; shipping-tool-tip={%22display%22:false}; _gid=GA1.2.1275753967.1601700146; _fbp=fb.1.1602183870915.818847076; SKpbjs-unifiedid=%7B%22TDID%22%3A%2289d54fd0-57be-4e3e-b005-426f5460f6a7%22%2C%22TDID_LOOKUP%22%3A%22TRUE%22%2C%22TDID_CREATED_AT%22%3A%222020-09-20T17%3A09%3A41%22%7D; SKpbjs-unifiedid_last=Tue%2C%2020%20Oct%202020%2017%3A09%3A41%20GMT; SKpbjs-id5id=%7B%22ID5ID%22%3A%22ID5-ZHMOHaQRkjBvgQ1dTmDccS3DAvpNdemoZJ9WaT7YLQ%22%2C%22ID5ID_CREATED_AT%22%3A%222020-09-21T17%3A35%3A04Z%22%2C%22ID5_CONSENT%22%3Atrue%2C%22CASCADE_NEEDED%22%3Atrue%2C%22ID5ID_LOOKUP%22%3Atrue%2C%223PIDS%22%3A%5B%5D%7D; SKpbjs-id5id_last=Tue%2C%2020%20Oct%202020%2017%3A09%3A42%20GMT; rlsort=added%2Cdesc; __gads=ID=3ff1bed8a29f47e5:T=1583666485:R:S=ALNI_MZhgMumGGpNV11TJ5k_FAmO2UX7eg; _hjTLDTest=1; _hjid=0faefcc0-90ee-4383-8d4b-4a97f4d350a3; lngtd-sdp=12; ck_username=pierrecool; language2=en; session='ZMOY6ZWAin5nqQ8qC3Qo++4URY=?_expires=MTYwNjg1NDg5MA==&auth_token=IjhjN2ZoY2FMd0VWNkFUWlVXaUxTVjhTOHJQIg==&created_at=IjIwMjAtMTEtMTdUMjA6MzQ6NTAuNjc2MDM3Ig==&idp%3Atoken=ImV5SmhiR2NpT2lKU1V6STFOaUlzSW10cFpDSTZJbkIxWW14cFl6cGtZamRsTnpReU9TMHlZakEzTFRSaVpXTXRZakUyTWkwek16TmpNV000WkdZNE1tUWlMQ0owZVhBaU9pSktWMVFpZlEuZXlKaGRGOW9ZWE5vSWpvaVUyUkpaV2RhYURNeVoyNWtkbXhuTkhCbVUwYzFRU0lzSW1GMVpDSTZXeUl6Tmprd05EVXhOaTAzWlRWa0xUUmhOVEF0WW1aak5DMWxNRGxrWVdRNE9UbG1NR1FpWFN3aVlYVjBhRjkwYVcxbElqb3hOakExTmpRMU1qZzVMQ0psYldGcGJDSTZJbkJwWlhKeVpTNW9kV0psY25Rek9FQm5iV0ZwYkM1amIyMGlMQ0psYldGcGJGOWhZM1JwZG1GMFpXUWlPblJ5ZFdVc0ltVjRjQ0k2TVRZd05UWTBPRGc1TUN3aWFXRjBJam94TmpBMU5qUTFNamt3TENKcGMzTWlPaUpvZEhSd2N6b3ZMMkZqWTI5MWJuUnpMbVJwYzJOdlozTXVZMjl0THlJc0ltcDBhU0k2SWpZMU5XTXpNelEwTFRnelltTXROR1kzT1MwNFpEUTRMV0l3WlRRMFlXWXdZV0l3TmlJc0lteHZZMkZzWlNJNkltVnVJaXdpYm1GdFpTSTZJaUlzSW01dmJtTmxJam9pZUhsck5qVlVTblZJY2pWRlVIZE1iR3R5VjNVaUxDSndjbVZtWlhKeVpXUmZkWE5sY201aGJXVWlPaUp3YVdWeWNtVmpiMjlzSWl3aWNtRjBJam94TmpBMU5qUTFNamczTENKemFXUWlPaUpoWWpJMVkyRXhPUzAxTjJJM0xUUm1NbVF0T1RnellpMDBaamxrTm1ZNFlUTmhOalVpTENKemRXSWlPaUl4TnpJeU5UWTBJaXdpZFhCa1lYUmxaRjloZENJNklqSXdNakF0TVRFdE1UZFVNakE2TWpnNk5UTXJNREE2TURBaUxDSjNaV0p6YVhSbElqb2lhSFIwY0hNNkx5OXpiM1Z1WkdOc2IzVmtMbU52YlM5c2IzSmxiUzFwY0hOMWJTMTVieUlzSW5wdmJtVnBibVp2SWpvaUluMC5pcXpFcGR0YjRVOEJtb0Z6ZTlwVGJsd0podzBOSTlqbHc0anpoVXU5VFpGWG1Ya2NOSDNQXzZOMzVzR0lRNGxkYmNxa3FPeTZRY1dUVFBid1lDTGZaZGZsSkhodExoU0FqX21MeHJRbWNCdjE1ek95V29xSDRKLUkzTHdWVkN3QV93Nmx3MkFCOEtLRmx3YVdaTTRTZVFmOWhLSnRqbmhUeDRoenNnQlI3TWhYXzhWVnN4eTdZTGViT0pVdENHNGR2Q3BoLVRwV0NRQlQ0bkdrYTZvamlpd1F3eld6a3p5QlBXVFBWUTVoUEtzaGRqb0NVRWI4ZUZnTXpmRlN6aTJMbE5BN1lGMjJjZVJjdHhSTWlKQ3NKYVJ4R0lUUk9WcVY0aWZJMzlVdURZREJVWXpGbmltUk1vbXVZR054Q3VJeThiMlM3aEg3YzB5R25GMFF3Q05hRHc4VThxczdWcjNUM0c3aDItOURQb1NVOXpOQ3dCb19tSW5aenZzZF85Ull2cHhGWU9aRmJkOUJzWkhtYkxfOGNJMFFpblVRQzl2aWxoMjZUUDZGLVNuenU5ZWx2MWJtLVhVVV83NGp1dlBSV1BXOVRTN2x2WjZnR3YycFBSWnJqSVd1VlVkYmNnLW1EZURiV1R2VE1KZE1mX0t5SlhzbHJFeFN5c1lmc0t5R0RDU0NYZ0pLTmFUN3dsdDQ1Y3RPdDlkMFBhNTlOTml4WmJGWUZ1M0o1T255elFpcmRPclptZmhYQnk0aUd2ZzNEZUxob0tYZ2lOVG1Jdk9LZnR1eG55R3o2SWN6VVRXQ0YzT1lmUG9DaDZUQ0p5QktEbXlveW9tWHRtUFZXUUI5UndzdmRMS1pMdnJ1bHB0TG9kbzdFTEExamlFUl9Vb3c5cm1WUlU1bHY2VSI='; OptanonConsent=isIABGlobal=false&datestamp=Tue+Nov+17+2020+22%3A15%3A26+GMT%2B0100+(heure+normale+d%E2%80%99Europe+centrale)&version=6.6.0&landingPath=NotLandingPage&groups=C0004%3A1%2CC0003%3A1%2CC0002%3A1%2CC0001%3A1%2CSTACK8%3A0%2Cgad%3A1&hosts=&geolocation=FR%3BIDF&AwaitingReconsent=false&consentId=3e9f3969-2480-4901-a3f5-edfb6a559292&interactionCount=2");
    con.addRequestProperty("Accept","*/*");
    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + req);
    System.out.println("Response Code : " + responseCode);

    InputStream inputStream = con.getErrorStream();
    if (inputStream == null) {
        inputStream = con.getInputStream();
    }

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();

    //print result
    System.out.println(response.toString());

//    URL url = new URL(req);
//    HttpURLConnection uc = (HttpURLConnection)url.openConnection();
//    uc.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.0)");
//    //uc.addRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
//    uc.addRequestProperty("Accept","*/*");
//    uc.addRequestProperty("cookie", "sid=86a166f5a7e4a81cd1b198770c9e728e; usprivacy=1---; OneTrustWPCCPAGoogleOptOut=false; _ga=GA1.2.1521005215.1583601585; mp_session=0e82a3d94bf415a6718c635d; OptanonAlertBoxClosed=2020-08-15T09:03:28.508Z; eupubconsent-v2=CO4LHCxO4LHCxAcABBENAzCsAP_AAH_AACiQGVtf_X9fb2vj-_5999t0eY1f9_63t-wzjgeNs-8NyZ_X_J4Xr2MyvB34pqYKmR4EunLBAQdlHGHcTQgAwIkVqTLsYk2MizNKJ7JEilMbM2dYGG1vn8XTuZCY70-sf__zv3-_-___6oGUEEmGpfAQJCWMBJNmlUKIEIVxIVAOACihGFo0sNCRwU7I4CPUACABAYgIQIgQYgohZBAAIAAElEQAgAwIBEARAIAAQAjQEIACJAEFgBIGAQACoGhYARRBKBIQYHBUcogQFSLRQTzAAAAA.YAAAAAAAAAAA; _gcl_au=1.1.1238447013.1600707986; shipping-tool-tip={%22display%22:false}; _gid=GA1.2.1275753967.1601700146; _fbp=fb.1.1602183870915.818847076; SKpbjs-unifiedid=%7B%22TDID%22%3A%2289d54fd0-57be-4e3e-b005-426f5460f6a7%22%2C%22TDID_LOOKUP%22%3A%22TRUE%22%2C%22TDID_CREATED_AT%22%3A%222020-09-20T17%3A09%3A41%22%7D; SKpbjs-unifiedid_last=Tue%2C%2020%20Oct%202020%2017%3A09%3A41%20GMT; SKpbjs-id5id=%7B%22ID5ID%22%3A%22ID5-ZHMOHaQRkjBvgQ1dTmDccS3DAvpNdemoZJ9WaT7YLQ%22%2C%22ID5ID_CREATED_AT%22%3A%222020-09-21T17%3A35%3A04Z%22%2C%22ID5_CONSENT%22%3Atrue%2C%22CASCADE_NEEDED%22%3Atrue%2C%22ID5ID_LOOKUP%22%3Atrue%2C%223PIDS%22%3A%5B%5D%7D; SKpbjs-id5id_last=Tue%2C%2020%20Oct%202020%2017%3A09%3A42%20GMT; rlsort=added%2Cdesc; __gads=ID=3ff1bed8a29f47e5:T=1583666485:R:S=ALNI_MZhgMumGGpNV11TJ5k_FAmO2UX7eg; _hjTLDTest=1; _hjid=0faefcc0-90ee-4383-8d4b-4a97f4d350a3; lngtd-sdp=12; ck_username=pierrecool; language2=en; session='ZMOY6ZWAin5nqQ8qC3Qo++4URY=?_expires=MTYwNjg1NDg5MA==&auth_token=IjhjN2ZoY2FMd0VWNkFUWlVXaUxTVjhTOHJQIg==&created_at=IjIwMjAtMTEtMTdUMjA6MzQ6NTAuNjc2MDM3Ig==&idp%3Atoken=ImV5SmhiR2NpT2lKU1V6STFOaUlzSW10cFpDSTZJbkIxWW14cFl6cGtZamRsTnpReU9TMHlZakEzTFRSaVpXTXRZakUyTWkwek16TmpNV000WkdZNE1tUWlMQ0owZVhBaU9pSktWMVFpZlEuZXlKaGRGOW9ZWE5vSWpvaVUyUkpaV2RhYURNeVoyNWtkbXhuTkhCbVUwYzFRU0lzSW1GMVpDSTZXeUl6Tmprd05EVXhOaTAzWlRWa0xUUmhOVEF0WW1aak5DMWxNRGxrWVdRNE9UbG1NR1FpWFN3aVlYVjBhRjkwYVcxbElqb3hOakExTmpRMU1qZzVMQ0psYldGcGJDSTZJbkJwWlhKeVpTNW9kV0psY25Rek9FQm5iV0ZwYkM1amIyMGlMQ0psYldGcGJGOWhZM1JwZG1GMFpXUWlPblJ5ZFdVc0ltVjRjQ0k2TVRZd05UWTBPRGc1TUN3aWFXRjBJam94TmpBMU5qUTFNamt3TENKcGMzTWlPaUpvZEhSd2N6b3ZMMkZqWTI5MWJuUnpMbVJwYzJOdlozTXVZMjl0THlJc0ltcDBhU0k2SWpZMU5XTXpNelEwTFRnelltTXROR1kzT1MwNFpEUTRMV0l3WlRRMFlXWXdZV0l3TmlJc0lteHZZMkZzWlNJNkltVnVJaXdpYm1GdFpTSTZJaUlzSW01dmJtTmxJam9pZUhsck5qVlVTblZJY2pWRlVIZE1iR3R5VjNVaUxDSndjbVZtWlhKeVpXUmZkWE5sY201aGJXVWlPaUp3YVdWeWNtVmpiMjlzSWl3aWNtRjBJam94TmpBMU5qUTFNamczTENKemFXUWlPaUpoWWpJMVkyRXhPUzAxTjJJM0xUUm1NbVF0T1RnellpMDBaamxrTm1ZNFlUTmhOalVpTENKemRXSWlPaUl4TnpJeU5UWTBJaXdpZFhCa1lYUmxaRjloZENJNklqSXdNakF0TVRFdE1UZFVNakE2TWpnNk5UTXJNREE2TURBaUxDSjNaV0p6YVhSbElqb2lhSFIwY0hNNkx5OXpiM1Z1WkdOc2IzVmtMbU52YlM5c2IzSmxiUzFwY0hOMWJTMTVieUlzSW5wdmJtVnBibVp2SWpvaUluMC5pcXpFcGR0YjRVOEJtb0Z6ZTlwVGJsd0podzBOSTlqbHc0anpoVXU5VFpGWG1Ya2NOSDNQXzZOMzVzR0lRNGxkYmNxa3FPeTZRY1dUVFBid1lDTGZaZGZsSkhodExoU0FqX21MeHJRbWNCdjE1ek95V29xSDRKLUkzTHdWVkN3QV93Nmx3MkFCOEtLRmx3YVdaTTRTZVFmOWhLSnRqbmhUeDRoenNnQlI3TWhYXzhWVnN4eTdZTGViT0pVdENHNGR2Q3BoLVRwV0NRQlQ0bkdrYTZvamlpd1F3eld6a3p5QlBXVFBWUTVoUEtzaGRqb0NVRWI4ZUZnTXpmRlN6aTJMbE5BN1lGMjJjZVJjdHhSTWlKQ3NKYVJ4R0lUUk9WcVY0aWZJMzlVdURZREJVWXpGbmltUk1vbXVZR054Q3VJeThiMlM3aEg3YzB5R25GMFF3Q05hRHc4VThxczdWcjNUM0c3aDItOURQb1NVOXpOQ3dCb19tSW5aenZzZF85Ull2cHhGWU9aRmJkOUJzWkhtYkxfOGNJMFFpblVRQzl2aWxoMjZUUDZGLVNuenU5ZWx2MWJtLVhVVV83NGp1dlBSV1BXOVRTN2x2WjZnR3YycFBSWnJqSVd1VlVkYmNnLW1EZURiV1R2VE1KZE1mX0t5SlhzbHJFeFN5c1lmc0t5R0RDU0NYZ0pLTmFUN3dsdDQ1Y3RPdDlkMFBhNTlOTml4WmJGWUZ1M0o1T255elFpcmRPclptZmhYQnk0aUd2ZzNEZUxob0tYZ2lOVG1Jdk9LZnR1eG55R3o2SWN6VVRXQ0YzT1lmUG9DaDZUQ0p5QktEbXlveW9tWHRtUFZXUUI5UndzdmRMS1pMdnJ1bHB0TG9kbzdFTEExamlFUl9Vb3c5cm1WUlU1bHY2VSI='; OptanonConsent=isIABGlobal=false&datestamp=Tue+Nov+17+2020+22%3A15%3A26+GMT%2B0100+(heure+normale+d%E2%80%99Europe+centrale)&version=6.6.0&landingPath=NotLandingPage&groups=C0004%3A1%2CC0003%3A1%2CC0002%3A1%2CC0001%3A1%2CSTACK8%3A0%2Cgad%3A1&hosts=&geolocation=FR%3BIDF&AwaitingReconsent=false&consentId=3e9f3969-2480-4901-a3f5-edfb6a559292&interactionCount=2");
//    uc.setDoOutput(false);
//
//    InputStream in = uc.getInputStream();
//
//    int c = in.read();
//    StringBuilder build = new StringBuilder();
//    while (c != -1) {
//        build.append((char) c);
//        c = in.read();
//    }
//    String XML = build.toString();
//    in.close();




    Document doc = convertStringToXMLDocument( "ok" );

    String mostRecentItemTimeTab[] = doc.getElementsByTagName("updated").item(doc.getElementsByTagName("updated").getLength()-1).getTextContent().split("-");
    String mostRecentItemTime = mostRecentItemTimeTab[0]+"-"+mostRecentItemTimeTab[1]+"-"+mostRecentItemTimeTab[2];

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Date mostRecentItemTimeDate = dateFormat.parse(mostRecentItemTime);

    if(lastTimeDate == null ){
        lastTimeDate = mostRecentItemTimeDate;
    }

    System.out.println("Date de la dernière mise en vente : "+mostRecentItemTimeDate);

    if(!lastTimeDate.equals(mostRecentItemTimeDate) && lastTimeDate.compareTo(mostRecentItemTimeDate)<0){

        System.out.println("Flux RSS changé ");

        for(int i = 1; i< doc.getElementsByTagName("entry").getLength();i++){

             String timeUpdateTab[] = doc.getElementsByTagName("updated").item(i+1).getTextContent().split("-");
             String timeUpdate = timeUpdateTab[0]+"-"+timeUpdateTab[1]+"-"+timeUpdateTab[2];
             Date timeUpdateDate = dateFormat.parse(timeUpdate);

             if(timeUpdateDate.compareTo(lastTimeDate)>0){

                 String newSale = doc.getElementsByTagName("title").item(i+1).getTextContent();
                 System.out.println("Flux RSS changé par l'ajout de : "+newSale);

                 lastTimeDate = mostRecentItemTimeDate;

                 Statement state = conn.createStatement();
                 ResultSet rs = state.executeQuery("SELECT * FROM "+nameTable+"");

                 while(rs.next()){
                     String nameAlbumXml = newSale.split("-")[1].replaceAll("'"," ").replaceAll("\\*","");
                     String nameAlbumBdd = rs.getString("album");
                     String linkSale = doc.getElementsByTagName("id").item(i+1).getTextContent();
                     int priceRelease = parseInt(doc.getElementsByTagName("summary").item(i).getTextContent().split("-")[0].split(" ")[1].split("\\.")[0]);


                     if(nameAlbumXml.contains(nameAlbumBdd) && rs.getInt("noteprice")!= -1 && (rs.getInt("noteprice")==0 || priceRelease <= (rs.getInt("noteprice")))){

                         System.out.println("priceRelease : "+priceRelease);
                         System.out.println("noteprice : "+rs.getInt("noteprice"));
                         System.out.println("id : "+rs.getInt("id"));

                         String idItemSale[] = linkSale.split("/");
                         //JSONObject JSONitemSale = getJSON("https://api.discogs.com/marketplace/listings/"+idItemSale[5]);

                        // System.out.println("condition : "+JSONitemSale.getString("condition"));
                            String condition = "(VG)";

                         if(!checkCondition(condition)){
                             sendMail(newSale,priceRelease,linkSale);
                         }

                     }
                 }
             }
         }
     }

    TimeUnit.SECONDS.sleep(5);
    getXML(req,conn, nameTable, lastTimeDate);
    }

    private static boolean checkCondition(String condition){

        boolean isBadCondition = false;
        String[] matches = new String[] {"(G+)", "(G)", "(P)", "(F)"};
        for (String s : matches)
        {
            if (condition.contains(s))
            {
                isBadCondition = true;
            }
        }
        return isBadCondition;
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public static JSONObject getJSON(String req){
        System.out.println("req : "+req);

        String stringJ = null;
        try {
            URL url = new URL(req);
            URLConnection uc = url.openConnection();
            //uc.setRequestProperty("Content-Type", "application/json; utf-8");
            uc.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream in = uc.getInputStream();
            int c = in.read();
            StringBuilder build = new StringBuilder();
            while (c != -1) {
                build.append((char) c);
                c = in.read();
            }
            stringJ = build.toString();
            in.close();
        } catch (MalformedURLException e) {

            System.out.println("ERROR :"+ e);

            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR : "+ e);

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




    public static void getWantlist(Connection conn, String req, String nameTable) {
        String stringJ = null;
        System.out.println("req : "+req);

        JSONObject JSON = getJSON(req);

        try {
            //JSONArray values = JSON.getJSONArray("releases");
            JSONArray values = JSON.getJSONArray("wants");
            for (int i = 0; i < values.length(); i++) {


                if(values.getJSONObject(i).get("rating").toString().equals("5")){

                    String apiUrl = values.getJSONObject(i).getJSONObject("basic_information").getString("resource_url");
                    System.out.println("apiUrl : "+apiUrl);

                    int notePrice = 0;
                    if(!values.getJSONObject(i).get("notes").toString().equals("")){
                        notePrice = parseInt(values.getJSONObject(i).get("notes").toString());
                    }
                    int idRelease = values.getJSONObject(i).getJSONObject("basic_information").getInt("id");
                    String urlDiscogs = "https://www.discogs.com/release/"+idRelease;
                    String album = values.getJSONObject(i).getJSONObject("basic_information").getString("title").replaceAll("'", " ");

                    String artist;
                    if(!values.getJSONObject(i).getJSONObject("basic_information").getJSONArray("artists").getJSONObject(0).getString("anv").equals("")){
                        artist = values.getJSONObject(i).getJSONObject("basic_information").getJSONArray("artists").getJSONObject(0).getString("anv").replaceAll("'", " ");
                    }else{
                        artist = values.getJSONObject(i).getJSONObject("basic_information").getJSONArray("artists").getJSONObject(0).getString("name").replaceAll("'", " ");
                    }

                    int price = 0;
                    int nbSale = 0;

//                    JSONObject JSONrelease = getJSON(apiUrl);
//                    String urlDiscogs = JSONrelease.getString("uri");
//                    int nbSale = JSONrelease.getInt("num_for_sale");
//                    System.out.println("lowest_price : "+ JSONrelease.get("lowest_price"));
//                    if(!JSONrelease.get("lowest_price").toString().equals("null")){
//                        price = JSONrelease.getInt("lowest_price");
//                    }
//                    System.out.println("JSONrelease : "+JSONrelease);
//                    TimeUnit.SECONDS.sleep(3);
                    Statement state;
                    try {
                        state = conn.createStatement();

                        state.executeUpdate("INSERT INTO "+nameTable+" (idrelease, artist, album, urldiscogs, urlapi ,price, nbsale, noteprice, exc) VALUES ('" + idRelease+ "','" + artist + "','" + album + "','" + urlDiscogs + "','" + apiUrl + "','" + price + "','" + nbSale + "','" + notePrice + "','"+ "" +"')");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            }

            req = JSON.getJSONObject("pagination").getJSONObject("urls").getString("next");
            System.out.println("next page : "+req);
            getWantlist(conn, req, nameTable);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void sendMail(String nameRelease, int priceRelease, String linkSale) {
        // Recipient's email ID needs to be mentioned.
        String to = "pierre.hubert.discogs@outlook.fr";

        // Sender's email ID needs to be mentioned
        String from = "pierre.hubert.discogs@outlook.fr";

        // Assuming you are sending email from through gmails smtp
        //String host = "smtp.gmail.com";
        String host = "smtp-mail.outlook.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);

        //port 465 pour gmail
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable","true");

        // properties.put("mail.smtp.ssl.enable", "true");
        // properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("pierre.hubert.discogs@outlook.fr", "supercool38");
                // return new PasswordAuthentication("pierre.hubert38@gmail.com", "mmxmcknytlcxwqzr");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(nameRelease+" : "+priceRelease);
            message.setText(linkSale);

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}

