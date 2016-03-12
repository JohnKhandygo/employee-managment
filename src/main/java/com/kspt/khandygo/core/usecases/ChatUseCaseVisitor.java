package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.usecases.chat.AddChatParticipant;
import com.kspt.khandygo.core.usecases.chat.CloseChatRoom;
import com.kspt.khandygo.core.usecases.chat.CreateChatRoom;
import com.kspt.khandygo.core.usecases.chat.LeaveChatRoom;
import com.kspt.khandygo.core.usecases.chat.SendChatMessage;

public interface ChatUseCaseVisitor {

  void visit(final AddChatParticipant u);

  void visit(final CloseChatRoom u);

  void visit(final CreateChatRoom u);

  void visit(final LeaveChatRoom u);

  void visit(final SendChatMessage u);
}
