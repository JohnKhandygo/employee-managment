package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Approved extends Entity {

  Employee owner();

  Subject subject();


  /*Employee employee();*/

  /*Approved cancel();*/
}
