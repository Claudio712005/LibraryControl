package com.clau.config.dao;

import com.clau.annotation.*;
import com.clau.config.database.DataBaseConfig;
import com.clau.exception.AppDataException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
      ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);

      String columnName = "";

      if (manyToOne != null) {
        columnName = manyToOne.nameColumn();
      } else {
        columnName = column != null ? column.name() : field.getName();
      }

      columns.add(columnName);
      placeholders.add("?");

      try {
        Object value = field.get(entity);

        if (field.getType().isEnum()) {
          values.add(((Enum<?>) value).name());
        } else if (manyToOne != null) {
          if (value != null) {
            Field foreignKeyField = value.getClass().getDeclaredField(manyToOne.foreignKey());
            foreignKeyField.setAccessible(true);
            values.add(foreignKeyField.get(value));
          } else {
            values.add(null);
          }
        } else {
          values.add(value);
        }
      } catch (IllegalAccessException | NoSuchFieldException e) {
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

  private String buildSelectQueryWithJoins(Long idFilter) {
    StringBuilder sql = new StringBuilder("SELECT ");
    List<String> columns = new ArrayList<>();
    String mainTableAlias = "t";

    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      if (field.isAnnotationPresent(Column.class)) {
        String columnName = field.getAnnotation(Column.class).name();
        columns.add(mainTableAlias + "." + columnName + " AS " + columnName);
      } else if (field.isAnnotationPresent(ManyToOne.class)) {
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if (manyToOne.fetch()) {
          String fkColumn = manyToOne.nameColumn();
          columns.add(mainTableAlias + "." + fkColumn + " AS " + fkColumn);
        }
      }
    }

    sql.append(String.join(", ", columns));
    sql.append(" FROM ").append(tableName).append(" ").append(mainTableAlias);

    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(ManyToOne.class)) {
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if (manyToOne.fetch()) {
          Class<?> foreignClass = field.getType();
          Table foreignTable = foreignClass.getAnnotation(Table.class);
          if (foreignTable != null) {
            String foreignTableName = foreignTable.name();
            String fkColumn = manyToOne.nameColumn();
            String foreignKey = manyToOne.foreignKey();
            sql.append(" LEFT JOIN ").append(foreignTableName)
                    .append(" ON ").append(mainTableAlias).append(".").append(fkColumn)
                    .append(" = ").append(foreignTableName).append(".").append(foreignKey);
          }
        }
      }
    }

    if (idFilter != null) {
      String idColumn = getIdColumn();
      sql.append(" WHERE ").append(mainTableAlias).append(".").append(idColumn).append(" = ").append(idFilter);
    }

    return sql.toString();
  }

  public T findById(Long id) {
    String sql = buildSelectQueryWithJoins(id);
    try (Connection conn = DataBaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

      if (rs.next()) {
        return mapResultSet(rs);
      }
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar por ID com JOIN: " + e.getMessage(), e);
    }
    return null;
  }

  public List<T> findAll() {
    String sql = buildSelectQueryWithJoins(null);
    List<T> result = new ArrayList<>();

    try (Connection conn = DataBaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        result.add(mapResultSet(rs));
      }

    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar todos com JOINs: " + e.getMessage(), e);
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

      if (field.isAnnotationPresent(ManyToOne.class)) {
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        String columnName = manyToOne.nameColumn();
        Object foreignKeyValue = rs.getObject(columnName);

        if (foreignKeyValue != null && manyToOne.fetch()) {
          Class<?> foreignClass = field.getType();
          GenericDAO<?> foreignDAO = new GenericDAO<>(foreignClass);
          Object foreignObject = foreignDAO.findById(Long.parseLong(foreignKeyValue.toString()));
          field.set(obj, foreignObject);
        }

      } else if (field.isAnnotationPresent(Column.class)) {
        String columnName = field.getAnnotation(Column.class).name();
        Object value = rs.getObject(columnName);

        if (field.getType().isEnum() && value instanceof String) {
          Method fromNome = field.getType().getMethod("fromNome", String.class);
          Object enumValue = fromNome.invoke(null, (String) value);
          field.set(obj, enumValue);
        } else {
          field.set(obj, value);
        }
      }
    }
    return obj;
  }

  public ResultSet executeQuery(String sql, Object... params) {
    try {
      Connection conn = DataBaseConfig.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
      for (int i = 0; i < params.length; i++) {
        stmt.setObject(i + 1, params[i]);
      }
      return stmt.executeQuery();
    } catch (SQLException e) {
      throw new AppDataException("Erro ao executar query: " + e.getMessage(), e);
    }
  }
}
