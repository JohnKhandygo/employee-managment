package com.kspt.khandygo.persistence;

public interface Entry {

  Entry id(final int id);

  Entry markAsDeleted();

  int id();

  boolean deleted();
}
