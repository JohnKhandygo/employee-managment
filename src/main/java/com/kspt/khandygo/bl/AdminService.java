package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.sys.AdminApi;

public class AdminService implements AdminApi {

  private final Repository<Employee> repository;

  public AdminService(
      final Repository<Employee> repository) {
    this.repository = repository;
  }

  @Override
  public Employee deactivate(final int id) {
    return repository.delete(id);
  }

  @Override
  public int add(final Employee e) {
    return repository.add(e);
  }
}
