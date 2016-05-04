package com.kspt.khandygo.persistence.dao;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.Gateway;
import lombok.AllArgsConstructor;
import javax.inject.Inject;

@AllArgsConstructor(onConstructor = @__({@Inject}))
public class EmployeesDAO {

  private final Gateway gateway;

  public Employee get(final int id) {
    return gateway.find(Employee.class).where().eq("id", id).unique();
  }
}
