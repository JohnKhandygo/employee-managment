package com.kspt.khandygo.core.usecases.employee;

import com.kspt.khandygo.core.SendMessageUseCaseVisitor;
import com.kspt.khandygo.core.usecases.SendMessage;

public interface TrackTime extends SendMessage {

  long when();

  int onBehalfOf();

  long minutes();

  default void accept(final SendMessageUseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
