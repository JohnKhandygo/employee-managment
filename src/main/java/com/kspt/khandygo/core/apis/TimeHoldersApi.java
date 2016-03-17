package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.TimeHolder;

public interface TimeHoldersApi {

  TimeHolder track(final TimeHolder th);

  TimeHolder reserve(final TimeHolder th);
}
