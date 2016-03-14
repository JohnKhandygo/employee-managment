package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Message extends Entity {

  Employee author();

  long origin();

  Object body();
}
