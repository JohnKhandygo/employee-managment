package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Approved extends Entity {

  Subject subject();

  Employee owner();

  Employee employee();

  /*Approved cancel();*/
}
