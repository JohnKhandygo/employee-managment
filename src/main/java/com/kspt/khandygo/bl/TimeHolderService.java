package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.TimeHolderApi;
import com.kspt.khandygo.core.entities.TimeHolder;
import com.kspt.khandygo.core.sys.MessageSender;

public class TimeHolderService implements TimeHolderApi {

  private final Repository<TimeHolder> reservations;

  private final MessageSender messageSender;

  public TimeHolderService(
      final Repository<TimeHolder> reservations,
      final MessageSender messageSender) {
    this.reservations = reservations;
    this.messageSender = messageSender;
  }

  @Override
  public TimeHolder reserve(final TimeHolder tr) {
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
