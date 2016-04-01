package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Subject extends Entity {

  int id();

  long when();

  Employee employee();
}
