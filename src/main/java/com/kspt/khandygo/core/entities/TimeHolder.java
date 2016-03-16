package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface TimeHolder extends Entity {

  long when();

  long start();

  long duration();

  Employee author();
}
