package com.kspt.khandygo.core.entities;

public interface Award extends Payment {
  boolean approved();

  Award approve();

  Award cancel();
}
