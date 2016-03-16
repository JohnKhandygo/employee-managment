package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.TimeHolder;

public interface EmployeesApi {

  void commit(final TimeHolder tr);

  Employee update(final Employee e);

  Employee get(final int id);
}
