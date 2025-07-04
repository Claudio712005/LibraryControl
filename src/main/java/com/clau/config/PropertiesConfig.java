package com.clau.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesConfig {

  private static final Logger logger = Logger.getLogger(PropertiesConfig.class.getName());
  private static final String PROPERTIES_FILE = "application.properties";
  private Properties properties;

  public PropertiesConfig() {
    this.properties = new Properties();

    try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
      properties.load(input);
    } catch (Exception e) {
      logger.severe("Erro ao carregar o arquivo de propriedades: " + PROPERTIES_FILE);
      throw new RuntimeException("Erro ao carregar o arquivo de propriedades: " + PROPERTIES_FILE, e);
    }
  }

  public String getProperty(String key) {
    String value = properties.getProperty(key);
    if (value == null) {
      logger.warning("Propriedade não encontrada: " + key);
      return null;
    }

    Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
    Matcher matcher = pattern.matcher(value);
    StringBuffer sb = new StringBuffer();

    while (matcher.find()) {
      String envVar = matcher.group(1);
      String envValue = System.getenv(envVar);
      if (envValue == null) {
        logger.warning("Variável de ambiente não encontrada: " + envVar);
        envValue = "";
      }
      matcher.appendReplacement(sb, envValue.replace("\\", "\\\\").replace("$", "\\$"));
    }
    matcher.appendTail(sb);

    return sb.toString();
  }
}
