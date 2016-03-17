package com.kspt.khandygo.core.entities;

public interface Answer {

  default boolean pending() {
    return !accepted() && !rejected();
  }

  boolean accepted();

  boolean rejected();
}
