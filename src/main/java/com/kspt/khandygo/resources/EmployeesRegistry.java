package com.kspt.khandygo.resources;

import com.google.common.collect.BiMap;
import com.kspt.khandygo.core.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
class EmployeesRegistry {
  private final BiMap<String, Employee> employees;

  @NonNull
  public Employee get(final String session) {
    return employees.get(session);
  }
}
