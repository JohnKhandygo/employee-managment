package com.kspt.khandygo.core.usecases.chat;

import com.kspt.khandygo.core.usecases.ChatUseCaseVisitor;

public interface CloseChatRoom {
  default void accept(final ChatUseCaseVisitor v) {
    v.visit(this);
  }
}
