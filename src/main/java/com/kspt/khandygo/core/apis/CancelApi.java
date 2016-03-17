package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;

public interface CancelApi {

  void cancel(final int id, final Employee owner);
}
