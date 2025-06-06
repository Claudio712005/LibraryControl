package com.clau.config.database;

import com.clau.annotation.Column;
import com.clau.annotation.Id;
import com.clau.annotation.Table;
import com.clau.exception.AppDataException;
import com.clau.util.PackageUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.logging.Logger;

public class ValidateModels {

  private DataBaseConfig dataBaseConfig;
  private static final String MODEL_PACKAGE = "com.clau.model";
  private static final Logger logger = Logger.getLogger(ValidateModels.class.getName());

  public ValidateModels() {
    this.dataBaseConfig = new DataBaseConfig();
  }

  public void validarDataModels() {
    PackageUtil.initializeClassesInPackage(MODEL_PACKAGE);
    File[] modelFiles = PackageUtil.getClassFromPackage(MODEL_PACKAGE);

    if (modelFiles == null || modelFiles.length == 0) {
      logger.warning("Nenhum modelo encontrado no pacote 'com.clau.model'.");
    }

    for (File modelFile : modelFiles) {
      String className = MODEL_PACKAGE + "." + modelFile.getName().replace(".class", "");
      try {
        Class<?> clazz = Class.forName(className);

        if (clazz.isAnnotationPresent(Table.class)) {
          try (Connection con = DataBaseConfig.getConnection()) {
            logger.info("Validando model: " + clazz.getName());
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            String tableName = tableAnnotation.name();

            String sqlExistTable = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";

            PreparedStatement statement = con.prepareStatement(sqlExistTable);
            statement.setString(1, tableName);

            if (statement.executeQuery().next()) {
              int count = statement.getResultSet().getInt(1);
              if (count == 0) {
                logger.warning("Tabela %s não existe no banco de dados.".formatted(tableName));
                throw new AppDataException("Tabela %s não existe no banco de dados.".formatted(tableName));
              } else {
                logger.info("Tabela %s existe no banco de dados.".formatted(tableName));

                for (Field field : Arrays.stream(clazz.getDeclaredFields()).filter(
                        m -> m.isAnnotationPresent(Column.class) ||
                                m.isAnnotationPresent(Id.class)
                ).toList(
                )) {
                  Column columnAnnotation = field.getAnnotation(Column.class);

                  String columnName = (columnAnnotation != null) ? columnAnnotation.name() : field.getName();

                  String sqlExistColumn = "SELECT COUNT(*) FROM information_schema.columns WHERE table_name = ? AND column_name = ?";

                  PreparedStatement columnStatement = con.prepareStatement(sqlExistColumn);
                  columnStatement.setString(1, tableName);
                  columnStatement.setString(2, columnName);

                  if (columnStatement.executeQuery().next()) {
                    int columnCount = columnStatement.getResultSet().getInt(1);
                    if (columnCount == 0) {
                      logger.warning("Coluna %s não existe na tabela %s.".formatted(columnName, tableName));
                      throw new AppDataException("Coluna %s não existe na tabela %s.".formatted(columnName, tableName));
                    } else {
                      logger.info("Coluna %s existe na tabela %s.".formatted(columnName, tableName));
                    }
                  } else {
                    logger.warning("Erro ao verificar a existência da coluna %s na tabela %s.".formatted(columnName, tableName));
                    throw new AppDataException("Erro ao verificar a existência da coluna %s na tabela %s.".formatted(columnName, tableName));
                  }
                }
              }
            } else {
              logger.warning("Erro ao verificar a existência da tabela %s.".formatted(tableName));
              throw new AppDataException("Erro ao verificar a existência da tabela %s.".formatted(tableName));
            }

          } catch (Exception e) {
            logger.severe("Erro ao tentar validar model %s : %s".formatted(clazz.getName(), e.getMessage()));
            throw new AppDataException("Erro ao tentar validar model %s : %s".formatted(clazz.getName(), e.getMessage()));
          }
        }

      } catch (Exception e) {
        logger.severe("Erro ao carregar modelo: " + className + " - " + e.getMessage());
      }
    }
  }
}
