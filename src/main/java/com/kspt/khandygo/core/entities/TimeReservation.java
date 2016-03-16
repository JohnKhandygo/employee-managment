package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface TimeReservation extends Entity {

  long when();

  long duration();

  Employee author();
}
