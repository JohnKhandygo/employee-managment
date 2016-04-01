package com.kspt.khandygo.core;

public interface Repository<T> {

  int add(final T t);

  T get(final int id);

  void update(final int id, final T t);

  T delete(final int id);
}