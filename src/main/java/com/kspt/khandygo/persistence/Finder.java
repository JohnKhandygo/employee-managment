package com.kspt.khandygo.persistence;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import static com.kspt.khandygo.persistence.PersistenceUtils.*;
import static java.lang.String.format;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor(onConstructor = @__({@Inject}))
public class Finder {
  private final SQLServer server;

  private <T> T tryFindTheOnlyOne(final Class<T> clazz, final String condition) {
    final ResultSet selected = server.select(queryForClassWithCondition(clazz, condition));
    try {
      Verify.verify(selected.next(), "There is no entries satisfying condition.");
      final T mapped = map(selected, clazz);
      Verify.verify(!selected.next(), "Expected unique instance,  but got more.");
      return mapped;
    } catch (SQLException e) {
      throw new RuntimeException(
          format(
              "Error while fetching unique data for class %s %s.",
              clazz.getSimpleName(), condition));
    }
  }

  private <T> String queryForClassWithCondition(final Class<T> clazz, final String condition) {
    return format("SELECT * FROM %s %s", tableNameForClass(clazz), condition);
  }

  private <T> T map(final ResultSet rs, final Class<T> clazz)
  throws SQLException {
    final Constructor<?> constructor = findTheOnlyOneConstructorOf(clazz);
    final Object[] args = extractArgsForConstructor(rs, constructor);
    try {
      return (T) constructor.newInstance(args);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(
          format(
              "Cannot create instance of type %s using constructor %s.",
              clazz, constructor));
    }
  }

  private Object[] extractArgsForConstructor(
      final ResultSet rs,
      final Constructor<?> constructor) {
    final Parameter[] parameters = constructor.getParameters();
    final Object[] args = new Object[parameters.length];
    for (int i = 0; i < parameters.length; ++i) {
      Preconditions.checkState(parameters[i].isNamePresent(),
          "Parameter name not present. Please use -parameters options for javac.");
      args[i] = extractArgForConstructorParameter(rs, parameters[i]);
    }
    return args;
  }

  private Object extractArgForConstructorParameter(final ResultSet rs, final Parameter parameter) {
    final Class<?> parameterType = parameter.getType();
    if (isEntity(parameterType)) {
      return find(parameterType)
          .where()
          .eq("id", extractNonNullValueFor(rs, referenceKeyColumnName(parameter.getName())))
          .unique();
    } else {
      final Object extracted = extractValueFor(rs, parameter.getName());
      if (!parameter.getType().isAssignableFrom(extracted.getClass())) {
        if (extracted instanceof Number) {
          if (parameter.getType().equals(Integer.class)) {
            return ((Number) extracted).intValue();
          } else {
            throw new RuntimeException(
                format(
                    "Cannot cast %s of type %s to parameter %s of type %s",
                    extracted,
                    extracted.getClass().getSimpleName(),
                    parameter.getName(),
                    parameter.getType().getSimpleName()));
          }
        } else {
          throw new RuntimeException(
              format(
                  "Cannot cast %s of type %s to parameter %s of type %s",
                  extracted,
                  extracted.getClass().getSimpleName(),
                  parameter.getName(),
                  parameter.getType().getSimpleName()));
        }
      }
      return extracted;
    }
  }

  public <T> SelectAllOrSpecifyCondition<T> find(final Class<T> clazz) {
    Preconditions.checkState(clazz.isAnnotationPresent(Table.class),
        "Class, you want to search not annotated with Table.");
    return new SelectAllOrSpecifyCondition<>(clazz);
  }

  private <T> List<T> tryFindAll(final Class<T> clazz, final String condition) {
    final ResultSet selected = server.select(queryForClassWithCondition(clazz, condition));
    final List<T> mapped = Lists.newArrayList();
    try {
      while (selected.next()) mapped.add(map(selected, clazz));
    } catch (SQLException e) {
      throw new RuntimeException(
          format(
              "Error while fetching all data for class %s %s.",
              clazz.getSimpleName(), condition));
    }
    return mapped;
  }

  @AllArgsConstructor
  public class SelectAllOrSpecifyCondition<T> {
    private final Class<T> clazz;

    public ConditionBuilder where() {
      return new ConditionBuilder("WHERE ");
    }

    public List<T> all() {
      return tryFindAll(clazz, "");
    }

    @AllArgsConstructor
    public class ConditionBuilder {

      private final String conditionString;

      public ConditionBuilder eq(final String parameter, final Object o) {
        return new ConditionBuilder(format("%s %s=%s", conditionString, parameter, o));
      }

      public ConditionBuilder and() {
        return new ConditionBuilder(format("%s AND", conditionString));
      }

      public ConditionBuilder or() {
        return new ConditionBuilder(format("%s OR", conditionString));
      }

      public T unique() {
        return tryFindTheOnlyOne(clazz, conditionString);
      }

      public List<T> list() {
        return tryFindAll(clazz, conditionString);
      }
    }
  }
}
