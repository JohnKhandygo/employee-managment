package com.kspt.khandygo.core;

import java.util.List;

public interface ExternalStorage<T> {

  T save(final T t);

  T update(final T t);

  T find(final long id);

  List<T> findAll();
}
