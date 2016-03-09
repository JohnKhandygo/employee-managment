package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.UseCaseVisitor;

public interface SendChatMessage extends SendMessage {

  String text();

  default void accept(final UseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
