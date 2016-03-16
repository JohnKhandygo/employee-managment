package com.kspt.khandygo.core.sys;

import com.kspt.khandygo.core.entities.Employee;

public interface AdminApi {

  Employee deactivate(final int id);

  int add(final Employee e);
}
