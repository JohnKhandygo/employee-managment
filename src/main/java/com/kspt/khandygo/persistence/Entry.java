package com.kspt.khandygo.persistence;

public interface Entry {

  Entry id(final long id);

  Entry markAsDeleted();

  long id();

  boolean deleted();
}
