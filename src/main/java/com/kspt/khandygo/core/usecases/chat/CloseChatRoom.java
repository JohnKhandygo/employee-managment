package com.kspt.khandygo.core.usecases.chat;

import com.kspt.khandygo.core.usecases.ChatUseCaseVisitor;

public interface CloseChatRoom {

  int chatId();

  default void accept(final ChatUseCaseVisitor v) {
    v.visit(this);
  }
}
