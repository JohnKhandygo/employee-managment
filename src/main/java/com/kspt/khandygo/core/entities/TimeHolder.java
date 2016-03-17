package com.kspt.khandygo.core.entities;

public interface TimeHolder extends Approved {

  long when();

  long start();

  long duration();

  Employee employee();
}
