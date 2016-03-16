package com.kspt.khandygo.core.sys;

import com.kspt.khandygo.core.Entity;
import com.kspt.khandygo.core.entities.Employee;

public interface Message extends Entity {

  Employee author();

  long origin();

  Object body();
}
