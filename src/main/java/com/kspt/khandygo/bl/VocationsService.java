package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Vocation;
import com.kspt.khandygo.persistence.dao.EmployeesDAO;
import com.kspt.khandygo.persistence.dao.VocationsDAO;
import lombok.AllArgsConstructor;
import javax.inject.Inject;

@AllArgsConstructor(onConstructor = @__({@Inject}))
public class VocationsService {

  private final EmployeesDAO employeesDAO;

  private final VocationsDAO vocationsDAO;

  public int propose(
      final Employee requester,
      final long when,
      final long duration,
      final int employeeId) {
    final Employee employee = employeesDAO.get(employeeId);
    Preconditions.checkState(requester.equals(employee));
    final Vocation vocation = Vocation.newOne(employee, when, duration);
    return vocationsDAO.save(vocation);
  }

  public void approve(final Employee requester, final int id) {
    final Vocation vocation = vocationsDAO.get(id);
    Preconditions.checkState(requester.equals(vocation.employee().manager()));
    final Vocation approvedVocation = vocation.approve();
    vocationsDAO.update(id, approvedVocation);
  }

  public void reject(final Employee requester, final int id) {
    final Vocation vocation = vocationsDAO.get(id);
    Preconditions.checkState(requester.equals(vocation.employee().manager()));
    final Vocation rejectedVocation = vocation.reject();
    vocationsDAO.update(id, rejectedVocation);
  }

  public void cancel(final Employee requester, final int id) {
    final Vocation vocation = vocationsDAO.get(id);
    Preconditions.checkState(requester.equals(vocation.employee()));
    final Vocation cancelledVocation = vocation.cancel();
    vocationsDAO.update(id, cancelledVocation);
  }
}