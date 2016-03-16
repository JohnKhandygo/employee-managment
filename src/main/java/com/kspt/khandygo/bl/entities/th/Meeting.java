package com.kspt.khandygo.bl.entities.th;

import com.kspt.khandygo.bl.entities.locations.MeetingLocation;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.TimeHolder;

public interface Meeting extends TimeHolder {

  Employee author();

  MeetingLocation where();

  //TODO separate Meeting as TimeHolder and Meeting as proposal.
  //List<Employee> participants();
}
