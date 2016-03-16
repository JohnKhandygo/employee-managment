package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.TimeHolder;

public interface TimeHoldersApi {

  TimeHolder reserve(final TimeHolder tr);

  void accept(final int id);

  void decline(final int id);

  void cancel(final int id);
}
