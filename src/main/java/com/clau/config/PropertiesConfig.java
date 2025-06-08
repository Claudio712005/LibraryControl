package com.clau.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesConfig {

  private static final Logger logger = Logger.getLogger(PropertiesConfig.class.getName());
  private static final String PROPERTIES_FILE = "application.properties";
  private Properties properties;

  public PropertiesConfig(){
    this.properties = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
      properties.load(input);
    } catch (Exception e) {
      logger.severe("Erro ao carregar o arquivo de propriedades: " + PROPERTIES_FILE);
      throw new RuntimeException("Erro ao carregar o arquivo de propriedades: " + PROPERTIES_FILE, e);
    }
  }

  public String getProperty(String key) {
    String properti = properties.getProperty(key);
    if (properti == null) {
      logger.warning("Propriedade não encontrada: " + key);
      return null;
    }

    return properti;
  }
}
