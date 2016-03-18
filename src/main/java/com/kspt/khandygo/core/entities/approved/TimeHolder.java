package com.kspt.khandygo.core.entities.approved;

import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Employee;

public interface TimeHolder extends Approved {

  long when();

  long duration();

  Employee employee();
}
