package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Payment extends Entity {

  long when();

  long amount();

  Payment increaseBy(final long by);
}
