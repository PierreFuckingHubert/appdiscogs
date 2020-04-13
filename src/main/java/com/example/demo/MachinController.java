package com.example.demo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.sql.Connection;

import org.json.JSONArray;

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

    @RequestMapping(value = "/records")
    public String serviceGetRecords(@RequestParam(value = "tablename") String tablename){
        JSONArray jsArray = new JSONArray(bdd.getRecords(tablename));

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
    public String addReleasesToBdd(@RequestBody SearchObj searchObj) {
        System.out.println("addReleases java :");
        Connection conn = bdd.connection();
        Discogs.addReleasesToBddFromRequest(conn, searchObj, false);
        return "ok";
    }

    @PostMapping("/addRatedReleasesToBdd")
    public String addRatedReleasesToBdd(@RequestBody SearchObj searchObj) {
        System.out.println("addRatedReleasesToBdd :");
        Connection conn = bdd.connection();
        Discogs.addReleasesToBddFromRequest(conn, searchObj, true);
        return "ok";
    }



}
