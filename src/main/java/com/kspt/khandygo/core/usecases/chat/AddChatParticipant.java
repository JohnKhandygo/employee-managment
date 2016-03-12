package com.kspt.khandygo.core.usecases.chat;

import com.kspt.khandygo.core.usecases.ChatUseCaseVisitor;

public interface AddChatParticipant {

  int participantId();

  default void accept(final ChatUseCaseVisitor v) {
    v.visit(this);
  }

  int chatId();
}
