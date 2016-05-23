package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.persistence.dao.EmployeesDAO;
import com.kspt.khandygo.utils.Tuple2;
import lombok.AllArgsConstructor;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@Singleton
public class EmployeesService {

  private final EmployeesDAO employeesDAO;

  public List<Tuple2<Integer, ? extends Employee>> getAllUnderThePatronageOf(
      final Employee manager) {
    return employeesDAO.getAllUnderThePatronageOf(manager);
  }
}
