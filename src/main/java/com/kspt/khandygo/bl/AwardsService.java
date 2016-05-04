package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Award;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.dao.AwardsDAO;
import com.kspt.khandygo.persistence.dao.EmployeesDAO;
import lombok.AllArgsConstructor;
import javax.inject.Inject;

@AllArgsConstructor(onConstructor = @__({@Inject}))
public class AwardsService {

  private final EmployeesDAO employeesDAO;

  private final AwardsDAO awardsDAO;

  public int propose(
      final Employee requester,
      final long when,
      final long duration,
      final int employeeId) {
    final Employee employee = employeesDAO.get(employeeId);
    Preconditions.checkState(requester.equals(employee.manager()));
    final Award award = Award.newOne(employee, when, duration);
    return awardsDAO.save(award);
  }

  public void approve(final Employee requester, final int id) {
    final Award award = awardsDAO.get(id);
    Preconditions.checkState(requester.equals(award.employee().paymaster()));
    awardsDAO.update(id, award.approve());
  }

  public void reject(final Employee requester, final int id) {
    final Award award = awardsDAO.get(id);
    Preconditions.checkState(requester.equals(award.employee().paymaster()));
    awardsDAO.update(id, award.reject());
  }

  public void cancel(final Employee requester, final int id) {
    final Award award = awardsDAO.get(id);
    Preconditions.checkState(requester.equals(award.employee().manager()));
    awardsDAO.update(id, award.cancel());
  }
}
