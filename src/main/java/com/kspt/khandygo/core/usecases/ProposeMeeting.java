package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.UseCaseVisitor;
import com.kspt.khandygo.core.entities.MeetingLocation;

public interface ProposeMeeting extends SendMessage {

  long when();

  long minutes();

  MeetingLocation where();

  default void accept(final UseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
