package com.kspt.khandygo.core;

import com.kspt.khandygo.core.usecases.ProposeMeeting;
import com.kspt.khandygo.core.usecases.SendChatMessage;
import com.kspt.khandygo.core.usecases.SendNotification;
import com.kspt.khandygo.core.usecases.TrackTime;
import com.kspt.khandygo.core.usecases.UpdateEmployeeData;

public interface UseCaseVisitor {

  void visit(final ProposeMeeting useCase);

  void visit(final SendChatMessage useCase);

  void visit(final SendNotification useCase);

  void visit(final TrackTime useCase);

  void visit(final UpdateEmployeeData useCase);
}
