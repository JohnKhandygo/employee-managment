package com.kspt.khandygo.core.usecases.chat;

import com.kspt.khandygo.core.SendMessageUseCaseVisitor;
import com.kspt.khandygo.core.usecases.SendMessage;

public interface SendChatMessage extends SendMessage {

  String text();

  default void accept(final SendMessageUseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
