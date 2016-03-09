package com.kspt.khandygo.core;

import java.util.List;

public interface ExternalStorage<T> {

  T store(final T t);

  int storeAll(final List<T> ts);

  T retrieve(final long id);

  List<T> retrieveAll();
}
