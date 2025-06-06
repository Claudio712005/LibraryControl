package com.clau.config.database;

import com.clau.config.PropertiesConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DataBaseConfig {

  private static Logger logger = Logger.getLogger(DataBaseConfig.class.getName());

  private PropertiesConfig propertiesConfig;

  private static String USER_DB;
  private static String PASSWORD_DB;
  private static String URL_DB;

  public DataBaseConfig(){
    this.propertiesConfig = new PropertiesConfig();

    USER_DB = propertiesConfig.getProperty("db.user");
    PASSWORD_DB = propertiesConfig.getProperty("db.password");
    URL_DB = propertiesConfig.getProperty("db.url");
  }

  public static Connection getConnection() {
    try {
      logger.config("Tentando conectar ao banco de dados com URL: " + URL_DB);
      return DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
    } catch (SQLException e) {
      logger.severe("Erro ao conectar ao banco de dados: " + e.getMessage());
      throw new RuntimeException("Erro ao conectar ao banco de dados", e);
    }
  }
}
