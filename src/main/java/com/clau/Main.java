package com.clau;

import com.clau.config.database.ValidateModels;
import com.clau.config.server.Router;
import com.clau.config.server.RouteScanner;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

    ValidateModels validateModels = new ValidateModels();

    validateModels.validarDataModels();

    RouteScanner.scanPackage("com.clau.controller");

    Router.getInstance().applyRoutes(server);

    server.setExecutor(null);
    server.start();
    System.out.println("Servidor rodando em http://localhost:8080/");
  }
}
