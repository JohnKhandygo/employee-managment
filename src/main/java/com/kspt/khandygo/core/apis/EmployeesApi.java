package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;

public interface EmployeesApi {

  Employee update(final Employee e);

  Employee get(final int id);
}
