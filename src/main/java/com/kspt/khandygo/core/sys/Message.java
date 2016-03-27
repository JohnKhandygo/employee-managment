package com.kspt.khandygo.core.sys;

import com.kspt.khandygo.core.entities.Employee;

public interface Message {

  Employee author();

  long origin();

  Object body();
}
