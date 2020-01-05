package com.example.demo;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.sql.Connection;
import java.util.ArrayList;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RestController
public class MachinController {
    @RequestMapping("/hello")
    public @ResponseBody String home(){
        return "Hello Worlddddee!";
    }

    @RequestMapping("/tablesNames")
    public @ResponseBody String serviceGetTablesNames(){
        JSONArray jsArray = new JSONArray(bdd.getAllTablesNames());
        return jsArray.toString();
    }

    @RequestMapping("/categories")
    public @ResponseBody String serviceGetCategories(){
        JSONArray jsArray = new JSONArray(bdd.getCategories());
        return jsArray.toString();
    }

    @GetMapping("/createTable2")
    String serviceGetTablesNames2(){
        JSONArray jsArray = new JSONArray(bdd.getAllTablesNames());
        return jsArray.toString();
    }

    @PostMapping("/createTable")
    public String createTable(@RequestBody String name) {
        return bdd.createTable(name);
    }

    @PostMapping("/deleteTable")
    public String deleteTable(@RequestBody String name) {
        return bdd.deleteTable(name);
    }

    @PostMapping("/addRecord")
    public String addRecord(@RequestBody UrlRecord data) { return bdd.addRecord(data); }


    @PostMapping("/searchOnEbay")
    public String searchOnEbay(@RequestBody String category) {
        System.out.println("category :"+ category);
        bdd.searchOnEbay(category);
        System.out.println("FINI!!!!!!!!!!!!!");
        return "ok";
    }

    @PostMapping("/addReleasesToBdd")
    public String addReleasesToBdd(@RequestBody String request) {
        System.out.println("addReleases java :");
        Connection conn = bdd.connection();
        Discogs.addReleasesToBddFromRequest(conn, request);
        return "ok";
    }









}
