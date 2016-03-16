package com.kspt.khandygo.bl.entities.th;

import com.kspt.khandygo.bl.entities.locations.MeetingLocation;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.TimeHolder;
import java.util.List;

public interface Meeting extends TimeHolder {

  MeetingLocation where();

  List<Employee> participants();
}
