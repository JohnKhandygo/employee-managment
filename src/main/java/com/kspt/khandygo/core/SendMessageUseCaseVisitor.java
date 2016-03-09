package com.kspt.khandygo.core;

import com.kspt.khandygo.core.usecases.ProposeMeeting;
import com.kspt.khandygo.core.usecases.SendChatMessage;
import com.kspt.khandygo.core.usecases.TrackTime;

public interface SendMessageUseCaseVisitor {

  void visit(final ProposeMeeting u);

  void visit(final SendChatMessage u);

  void visit(final TrackTime u);
}
