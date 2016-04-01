package com.kspt.khandygo.bl.entities.subjects.th;

import com.kspt.khandygo.bl.entities.subjects.TimeHolder;
import com.kspt.khandygo.core.entities.Employee;
import java.util.List;

public interface Meeting extends TimeHolder {

  List<Employee> participants();
}
