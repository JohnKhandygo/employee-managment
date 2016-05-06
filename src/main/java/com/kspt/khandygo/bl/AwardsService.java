package com.kspt.khandygo.bl;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Award;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.dao.AwardsDAO;
import com.kspt.khandygo.persistence.dao.EmployeesDAO;
import com.kspt.khandygo.utils.Tuple2;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@Singleton
public class AwardsService {

  private final EmployeesDAO employeesDAO;

  private final AwardsDAO awardsDAO;

  public List<Tuple2<Integer, Award>> approvedFor(final int employeeId) {
    return awardsDAO.approvedFor(employeeId);
  }

  public List<Tuple2<Integer, Award>> pendingInboxFor(final int employeeId) {
    return employeesDAO.getAllMasteredBy(employeeId).stream()
        .map(t2 -> t2._1)
        .map(awardsDAO::pendingFor)
        .flatMap(List::stream)
        .sorted(comparingLong(t2 -> t2._2.when()))
        .collect(toList());
  }

  public List<Tuple2<Integer, Award>> pendingOutboxFor(final int employeeId) {
    return employeesDAO.getAllUnderThePatronageOf(employeeId).stream()
        .map(t2 -> t2._1)
        .map(awardsDAO::pendingFor)
        .flatMap(List::stream)
        .sorted(comparingLong(t2 -> t2._2.when()))
        .collect(toList());
  }

  public Award get(final Employee requester, final int awardId) {
    final Award award = awardsDAO.get(awardId);
    Preconditions.checkState(requester.equals(award.employee()));
    return award;
  }

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
