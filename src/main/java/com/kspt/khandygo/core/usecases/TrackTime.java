package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.UseCaseVisitor;

public interface TrackTime extends SendMessage {

  long when();

  int onBehalfOf();

  int minutes();

  default void accept(final UseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
