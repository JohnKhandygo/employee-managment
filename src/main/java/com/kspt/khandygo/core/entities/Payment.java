package com.kspt.khandygo.core.entities;

public interface Payment extends Approved {

  long when();

  long amount();

  boolean received();

  Employee employee();
}
