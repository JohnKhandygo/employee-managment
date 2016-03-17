package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Payment extends Entity, Rejectable<Payment> {

  long when();

  long amount();

  boolean received();

  Employee employee();
}
