package com.kspt.khandygo.persistence.dao;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.Gateway;
import com.kspt.khandygo.utils.Tuple2;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@Singleton
public class EmployeesDAO {

  private final Gateway gateway;

  public Employee get(final int id) {
    return gateway.find(UserEntity.class).where().eq("id", id).unique();
  }

  public List<Tuple2<Integer, ? extends Employee>> getAllUnderThePatronageOf(
      final Employee manager) {
    Preconditions.checkState(manager instanceof UserEntity);
    final Integer managerId = ((UserEntity) manager).id();
    return gateway.find(UserEntity.class)
        .where()
        .eq("manager_id", managerId)
        .list()
        .stream()
        .map(entity -> new Tuple2<>(entity.id(), entity))
        .collect(toList());
  }
}
