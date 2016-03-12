package com.kspt.khandygo.core.usecases.meeting;

import com.kspt.khandygo.core.SendMessageUseCaseVisitor;
import com.kspt.khandygo.core.entities.MeetingLocation;
import com.kspt.khandygo.core.usecases.SendMessage;

public interface ProposeMeeting extends SendMessage {

  long when();

  long minutes();

  MeetingLocation where();

  default void accept(final SendMessageUseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
