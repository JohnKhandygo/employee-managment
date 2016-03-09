package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.SendMessageUseCaseVisitor;

public interface SendChatMessage extends SendMessage {

  String text();

  default void accept(final SendMessageUseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
