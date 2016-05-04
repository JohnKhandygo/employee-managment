package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.OutOfOffice;
import com.kspt.khandygo.persistence.dao.EmployeesDAO;
import com.kspt.khandygo.persistence.dao.OutOfOfficesDAO;
import lombok.AllArgsConstructor;
import javax.inject.Inject;

@AllArgsConstructor(onConstructor = @__({@Inject}))
public class OutOfOfficesService {

  private final EmployeesDAO employeesDAO;

  private final OutOfOfficesDAO outOfOfficesDAO;

  public int create(
      final Employee requester,
      final long when,
      final long duration,
      final String reason,
      final int employeeId) {
    final Employee employee = employeesDAO.get(employeeId);
    Preconditions.checkState(requester.equals(employee));
    final OutOfOffice outOfOffice = OutOfOffice.newOne(employee, when, duration, reason);
    return outOfOfficesDAO.save(outOfOffice);
  }

  public void update(
      final Employee requester,
      final int id,
      final long when,
      final long duration,
      final String reason) {
    final OutOfOffice outOfOffice = outOfOfficesDAO.get(id);
    Preconditions.checkState(requester.equals(outOfOffice.employee()));
    final OutOfOffice model = OutOfOffice.newOne(outOfOffice.employee(), when, duration, reason);
    outOfOfficesDAO.update(id, model);
  }

  public void cancel(final Employee requester, final int id) {
    final OutOfOffice outOfOffice = outOfOfficesDAO.get(id);
    Preconditions.checkState(requester.equals(outOfOffice.employee()));
    final OutOfOffice cancelledVocation = outOfOffice.cancel();
    outOfOfficesDAO.update(id, cancelledVocation);
  }
}
