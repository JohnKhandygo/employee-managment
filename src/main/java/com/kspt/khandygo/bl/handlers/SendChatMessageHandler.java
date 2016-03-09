package com.kspt.khandygo.bl.handlers;

import com.kspt.khandygo.bl.messages.ChatMessage;
import com.kspt.khandygo.core.ExternalStorage;
import com.kspt.khandygo.core.MessageSender;
import com.kspt.khandygo.core.entities.Message;
import com.kspt.khandygo.core.usecases.SendChatMessage;

public class SendChatMessageHandler {
  private final MessageSender messageSender;

  private final ExternalStorage<Message> messages;

  public SendChatMessageHandler(
      final MessageSender messageSender,
      final ExternalStorage<Message> messages) {
    this.messageSender = messageSender;
    this.messages = messages;
  }

  public void handle(final SendChatMessage u) {
    final ChatMessage m = new ChatMessage(u.origin(), u.author(), u.text());
    messageSender.send(m);
    messages.save(m);
  }
}
