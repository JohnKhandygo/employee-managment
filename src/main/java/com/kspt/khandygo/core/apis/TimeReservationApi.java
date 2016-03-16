package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.TimeReservation;

public interface TimeReservationApi {

  TimeReservation reserve(final TimeReservation tr);

  void accept(final int id);

  void decline(final int id);

  void cancel(final int id);
}
