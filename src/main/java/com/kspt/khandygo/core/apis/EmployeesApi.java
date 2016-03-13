package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;

public interface EmployeesApi {

  void fire(final int id);

  void hire(final Employee e);

  void trackTime(final int id, final long amount);

  void update(final Employee e);

  Employee get(final int id);

  void award(final int id, final long amount);
}
