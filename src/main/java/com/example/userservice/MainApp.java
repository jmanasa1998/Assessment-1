package com.example.userservice;

import io.vertx.core.Vertx;

import java.util.concurrent.ConcurrentHashMap;

public class MainApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        UserService userService = new InMemoryUserService(new ConcurrentHashMap<>());
        vertx.deployVerticle(new UserVerticle(userService));
    }
}
