package com.kspt.khandygo.bl;

import com.kspt.khandygo.bl.messages.ChatMessage;
import com.kspt.khandygo.bl.messages.MeetingProposal;
import com.kspt.khandygo.bl.messages.TrackTimeMessage;
import com.kspt.khandygo.core.ExternalStorage;
import com.kspt.khandygo.core.MessageSender;
import com.kspt.khandygo.core.SendMessageUseCaseVisitor;
import com.kspt.khandygo.core.entities.Message;
import com.kspt.khandygo.core.usecases.ProposeMeeting;
import com.kspt.khandygo.core.usecases.SendChatMessage;
import com.kspt.khandygo.core.usecases.TrackTime;

public class SendMessageUseCaseHandler implements SendMessageUseCaseVisitor {

  private final MessageSender messageSender;

  private final ExternalStorage<Message> messages;

  public SendMessageUseCaseHandler(
      final MessageSender messageSender,
      final ExternalStorage<Message> messages) {
    this.messageSender = messageSender;
    this.messages = messages;
  }

  @Override
  public void visit(final ProposeMeeting u) {
    final Message m = new MeetingProposal(
        u.origin(),
        u.author(),
        u.when(),
        u.minutes(),
        u.where(),
        "You have been invited to the meeting.");
    sendAdnSaveMessage(m);
  }

  private void sendAdnSaveMessage(final Message m) {
    messageSender.send(m);
    messages.save(m);
  }

  @Override
  public void visit(final SendChatMessage u) {
    final Message m = new ChatMessage(u.origin(), u.author(), u.text());
    messageSender.send(m);
  }

  @Override
  public void visit(final TrackTime u) {
    final Message m = new TrackTimeMessage(
        u.origin(),
        u.author(),
        u.onBehalfOf(),
        u.when(),
        u.minutes());
    sendAdnSaveMessage(m);
  }
}
