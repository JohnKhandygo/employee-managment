package com.kspt.khandygo.core.entities.approved;

import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Employee;

public interface Payment extends Approved {

  long when();

  long amount();

  boolean received();

  Employee employee();
}
