package com.kspt.khandygo.core.entities;

public interface Employee {

  String name();

  Employee manager();

  Employee paymaster();
}
