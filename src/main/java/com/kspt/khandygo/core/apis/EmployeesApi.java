package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;

public interface EmployeesApi {

  void update(final int id, final Employee e);

  Employee get(final int id);
}
