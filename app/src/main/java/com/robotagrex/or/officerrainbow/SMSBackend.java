package com.robotagrex.or.officerrainbow;

import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.*;

public class SMSBackend {
    public static void main(String[] args) {
        get("/", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                return "Hello, World!";
            }
        });
    }
}