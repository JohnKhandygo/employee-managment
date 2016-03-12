package com.kspt.khandygo.core.usecases.chat;

import com.kspt.khandygo.core.usecases.ChatUseCaseVisitor;
import java.util.List;

public interface CreateChatRoom {

  int author();

  List<Integer> participants();

  String initialMessage();

  default void accept(final ChatUseCaseVisitor v) {
    v.visit(this);
  }
}
