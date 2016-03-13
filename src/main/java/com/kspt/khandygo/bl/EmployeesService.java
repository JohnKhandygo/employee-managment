package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.EmployeesApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Payment;

public class EmployeesService implements EmployeesApi {

  private final Repository<Employee> employeesRepository;

  private final Repository<Payment> paymentsRepository;

  public EmployeesService(
      final Repository<Employee> employeesRepository,
      final Repository<Payment> paymentsRepository) {
    this.employeesRepository = employeesRepository;
    this.paymentsRepository = paymentsRepository;
  }

  @Override
  public void fire(final int id) {
    employeesRepository.delete(id);
  }

  @Override
  public void hire(final Employee e) {
    employeesRepository.add(e);
  }

  @Override
  public void trackTime(final int id, final long amount) {
    throw new UnsupportedOperationException("not implemented for a while.");
  }

  @Override
  public void update(final Employee e) {
    employeesRepository.update(e);
  }

  @Override
  public Employee get(final int id) {
    return employeesRepository.get(id);
  }

  @Override
  public void award(final int id, final long amount) {
    final Employee employee = employeesRepository.get(id);
    final Payment nextPayment = employee.paymentPlan().next();
    final Payment awarded = nextPayment.increaseBy(amount);
    paymentsRepository.update(awarded);
  }
}
