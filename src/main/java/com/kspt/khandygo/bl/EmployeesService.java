package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.EmployeesApi;
import com.kspt.khandygo.core.entities.Employee;
import static java.util.Objects.requireNonNull;
import javax.inject.Inject;

public class EmployeesService implements EmployeesApi {

  private final Repository<Employee> employees;

  @Inject
  public EmployeesService(final Repository<Employee> employees) {
    this.employees = employees;
  }

  @Override
  public void update(final int id, final Employee e) {
    employees.update(id, e);
  }

  @Override
  public Employee get(final int id) {

    return requireNonNull(employees.get(id));
  }
}
