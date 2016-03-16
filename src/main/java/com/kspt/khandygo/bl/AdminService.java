package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.AdminApi;
import com.kspt.khandygo.core.entities.Employee;

public class AdminService implements AdminApi {

  private final Repository<Employee> employeesRepository;

  public AdminService(
      final Repository<Employee> employeesRepository) {
    this.employeesRepository = employeesRepository;
  }

  @Override
  public Employee deactivate(final int id) {
    return employeesRepository.delete(id);
  }

  @Override
  public int add(final Employee e) {
    return employeesRepository.add(e).id();
  }
}
