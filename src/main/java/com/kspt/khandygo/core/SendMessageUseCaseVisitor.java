package com.kspt.khandygo.core;

import com.kspt.khandygo.core.usecases.chat.SendChatMessage;
import com.kspt.khandygo.core.usecases.employee.TrackTime;
import com.kspt.khandygo.core.usecases.meeting.ProposeMeeting;

public interface SendMessageUseCaseVisitor {

  void visit(final ProposeMeeting u);

  void visit(final SendChatMessage u);

  void visit(final TrackTime u);
}
