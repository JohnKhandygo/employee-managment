package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.SendMessageUseCaseVisitor;

public interface TrackTime extends SendMessage {

  long when();

  int onBehalfOf();

  long minutes();

  default void accept(final SendMessageUseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
