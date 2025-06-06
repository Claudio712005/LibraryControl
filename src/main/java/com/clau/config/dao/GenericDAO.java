package com.clau.config.dao;

import com.clau.annotation.*;
import com.clau.config.database.DataBaseConfig;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class GenericDAO<T> {

  private final Class<T> clazz;
  private final String tableName;

  public GenericDAO(Class<T> clazz) {
    this.clazz = clazz;
    Table table = clazz.getAnnotation(Table.class);
    if (table == null) {
      throw new RuntimeException("Classe " + clazz.getName() + " não possui @Table");
    }
    this.tableName = table.name();
  }

  public void insert(T entity) {
    List<String> columns = new ArrayList<>();
    List<String> placeholders = new ArrayList<>();
    List<Object> values = new ArrayList<>();

    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) continue;

      field.setAccessible(true);
      Column column = field.getAnnotation(Column.class);
      String columnName = column != null ? column.name() : field.getName();

      columns.add(columnName);
      placeholders.add("?");
      try {
        values.add(field.get(entity));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    String sql = "INSERT INTO " + tableName +
            " (" + String.join(", ", columns) + ") " +
            "VALUES (" + String.join(", ", placeholders) + ")";

    try (Connection conn = DataBaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      for (int i = 0; i < values.size(); i++) {
        stmt.setObject(i + 1, values.get(i));
      }
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao inserir entidade: " + e.getMessage(), e);
    }
  }

  public void deleteById(Long id) {
    String idColumn = getIdColumn();
    String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";

    try (Connection conn = DataBaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setObject(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar: " + e.getMessage(), e);
    }
  }

  public T findById(Long id) {
    String idColumn = getIdColumn();
    String sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";

    try (Connection conn = DataBaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setObject(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return mapResultSet(rs);
      }

    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar por ID: " + e.getMessage(), e);
    }
    return null;
  }

  public List<T> findAll() {
    String sql = "SELECT * FROM " + tableName;
    List<T> result = new ArrayList<>();

    try (Connection conn = DataBaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        result.add(mapResultSet(rs));
      }

    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar todos: " + e.getMessage(), e);
    }

    return result;
  }

  private String getIdColumn() {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) {
        Column column = field.getAnnotation(Column.class);
        return column != null ? column.name() : field.getName();
      }
    }
    throw new RuntimeException("Classe " + clazz.getSimpleName() + " não tem campo com @Id");
  }

  private T mapResultSet(ResultSet rs) throws Exception {
    T obj = clazz.getDeclaredConstructor().newInstance();
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      String columnName = field.isAnnotationPresent(Column.class)
              ? field.getAnnotation(Column.class).name()
              : field.getName();
      Object value = rs.getObject(columnName);
      field.set(obj, value);
    }
    return obj;
  }
}
