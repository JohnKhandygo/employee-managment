package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.MessageSender;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.TimeReservationApi;
import com.kspt.khandygo.core.entities.TimeReservation;

public class TimeReservationService implements TimeReservationApi {

  private final Repository<TimeReservation> reservations;

  private final MessageSender messageSender;

  public TimeReservationService(
      final Repository<TimeReservation> reservations,
      final MessageSender messageSender) {
    this.reservations = reservations;
    this.messageSender = messageSender;
  }

  @Override
  public TimeReservation reserve(final TimeReservation tr) {
    throw new UnsupportedOperationException("not implemented yet.");
  }

  @Override
  public void accept(final int id) {
    throw new UnsupportedOperationException("not implemented yet.");
  }

  @Override
  public void decline(final int id) {
    throw new UnsupportedOperationException("not implemented yet.");
  }

  @Override
  public void cancel(final int id) {
    reservations.delete(id);
  }
}
