package com.kspt.khandygo.core.entities;

public interface Proposal extends Thing {

  long when();

  Employee recipient();
}