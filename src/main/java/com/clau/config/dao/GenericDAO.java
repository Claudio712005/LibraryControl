package com.clau.config.dao;

import com.clau.annotation.*;
import com.clau.config.database.DataBaseConfig;
import com.clau.exception.AppDataException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.time.LocalDateTime;
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

  public void save(T entity) {
    List<String> columns = new ArrayList<>();
    List<Object> values = new ArrayList<>();
    Object idValue = null;
    String idColumn = null;

    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) {
        field.setAccessible(true);
        try {
          idValue = field.get(entity);
          Column column = field.getAnnotation(Column.class);
          idColumn = column != null ? column.name() : field.getName();
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Erro ao acessar o campo ID", e);
        }
        break;
      }
    }

    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(OneToMany.class)) continue;

      field.setAccessible(true);

      String columnName = null;
      Column column = field.getAnnotation(Column.class);
      ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);

      if (manyToOne != null) {
        columnName = manyToOne.nameColumn();
      } else if (column != null) {
        columnName = column.name();
      } else {
        columnName = field.getName();
      }

      try {
        Object value = field.get(entity);

        if (field.isAnnotationPresent(Id.class)) continue;

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

        columns.add(columnName);
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new RuntimeException(e);
      }
    }

    String sql;

    if (idValue != null) {
      List<String> updateSet = new ArrayList<>();
      for (String col : columns) {
        updateSet.add(col + " = ?");
      }

      sql = "UPDATE " + tableName + " SET " + String.join(", ", updateSet) + " WHERE " + idColumn + " = ?";
    } else {
      String placeholders = String.join(", ", Collections.nCopies(columns.size(), "?"));
      sql = "INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";
    }

    try (Connection conn = DataBaseConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      for (int i = 0; i < values.size(); i++) {
        stmt.setObject(i + 1, values.get(i));
      }

      if (idValue != null) {
        stmt.setObject(values.size() + 1, idValue);
      }

      stmt.executeUpdate();

      if (idValue == null) {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            Object generatedId = generatedKeys.getObject(1);
            for (Field field : clazz.getDeclaredFields()) {
              if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);

                Class<?> fieldType = field.getType();

                if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                  if (generatedId instanceof Number) {
                    field.set(entity, ((Number) generatedId).longValue());
                  }
                } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                  if (generatedId instanceof Number) {
                    field.set(entity, ((Number) generatedId).intValue());
                  }
                } else {
                  field.set(entity, generatedId);
                }

                break;
              }
            }

          }
        }
      }
    } catch (SQLException | IllegalAccessException e) {
      throw new RuntimeException("Erro ao salvar entidade: " + e.getMessage(), e);
    }
  }

  public void delete(T entity) {
    try {
      Object idValue = getIdValue(entity);
      if (idValue == null) {
        throw new RuntimeException("Entidade não possui ID definido");
      }
      deleteById((Long) idValue);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao remover entidade: " + e.getMessage(), e);
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
        return mapResultSet(rs, conn, false);
      }
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar por ID com JOIN: " + e.getMessage(), e);
    }
    return null;
  }

  public T findById(Long id, Connection existingConnection, boolean isChieldEntity) {
    String sql = buildSelectQueryWithJoins(id);
    try (PreparedStatement stmt = existingConnection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

      if (rs.next()) {
        return mapResultSet(rs, existingConnection, isChieldEntity);
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
        result.add(mapResultSet(rs, conn, false));
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

  private Object getIdValue(T obj) throws Exception {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) {
        field.setAccessible(true);
        return field.get(obj);
      }
    }
    throw new RuntimeException("Campo @Id não encontrado na classe " + clazz.getName());
  }

  public T mapResultSet(ResultSet rs) throws Exception {
    return mapResultSet(rs, DataBaseConfig.getConnection(), false);
  }

  public List<T> mapResultSetList(ResultSet rs) throws Exception {
    List<T> list = new ArrayList<>();
    while (rs.next()) {
      list.add(mapResultSet(rs, DataBaseConfig.getConnection(), false));
    }
    return list;
  }

  private T mapResultSet(ResultSet rs, Connection conn, boolean isChieldEntity) throws Exception {
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
          Object foreignObject = foreignDAO.findById(Long.parseLong(foreignKeyValue.toString()), conn, true);
          field.set(obj, foreignObject);
        }

      } else if (field.isAnnotationPresent(Column.class)) {
        String columnName = field.getAnnotation(Column.class).name();
        Object value = rs.getObject(columnName);

        if (field.getType().isEnum() && value instanceof String) {
          Method fromNome = field.getType().getMethod("fromNome", String.class);
          Object enumValue = fromNome.invoke(null, (String) value);
          field.set(obj, enumValue);
        } else if (field.getType().equals(LocalDateTime.class)) {
          if (value instanceof Timestamp) {
            field.set(obj, ((Timestamp) value).toLocalDateTime());
          } else if (value instanceof String) {
            field.set(obj, LocalDateTime.parse((String) value));
          } else {
            continue;
          }
        } else {
          field.set(obj, value);
        }
      } else if (field.isAnnotationPresent(OneToMany.class) && !isChieldEntity) {
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (oneToMany.fetch()) {
          ParameterizedType listType = (ParameterizedType) field.getGenericType();
          Class<?> childClass = (Class<?>) listType.getActualTypeArguments()[0];

          Table childTable = childClass.getAnnotation(Table.class);
          if (childTable != null) {
            String childTableName = childTable.name();
            String mappedBy = oneToMany.mappedBy();

            Object parentIdValue = getIdValue(obj);

            String childSql = "SELECT * FROM " + childTableName + " WHERE " + mappedBy + " = ?";

            List<Object> children = new ArrayList<>();

            try (PreparedStatement childStmt = conn.prepareStatement(childSql)) {
              childStmt.setObject(1, parentIdValue);

              try (ResultSet childRs = childStmt.executeQuery()) {
                GenericDAO<?> childDAO = new GenericDAO<>(childClass);
                while (childRs.next()) {
                  Object childObj = childDAO.mapResultSet(childRs, conn, true);
                  children.add(childObj);
                }
              }
            }

            field.set(obj, children);
          }
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
