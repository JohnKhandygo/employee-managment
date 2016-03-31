package com.kspt.khandygo.bl.entities.th;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.approved.TimeHolder;
import java.util.List;

public interface Meeting extends TimeHolder {

  /*Employee author();

  MeetingLocation where();*/

  List<Employee> participants();
}
