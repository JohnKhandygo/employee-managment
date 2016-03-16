package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.EmployeesApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.TimeHolder;

public class EmployeesService implements EmployeesApi {

  private final Repository<Employee> employees;

  private final Repository<TimeHolder> trs;

  public EmployeesService(
      final Repository<Employee> employees,
      final Repository<TimeHolder> trs) {
    this.employees = employees;
    this.trs = trs;
  }

  @Override
  public void commit(final TimeHolder tr) {
    trs.add(tr);
  }

  @Override
  public Employee update(final Employee e) {
    return employees.update(e);
  }

  @Override
  public Employee get(final int id) {
    return employees.get(id);
  }
}
