package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.TimeReservation;

public interface EmployeesApi {

  void commit(final TimeReservation tr);

  Employee update(final Employee e);

  Employee get(final int id);
}
