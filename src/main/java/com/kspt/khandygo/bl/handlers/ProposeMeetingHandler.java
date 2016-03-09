package com.kspt.khandygo.bl.handlers;

import com.kspt.khandygo.bl.messages.MeetingProposal;
import com.kspt.khandygo.core.ExternalStorage;
import com.kspt.khandygo.core.MessageSender;
import com.kspt.khandygo.core.entities.Message;
import com.kspt.khandygo.core.usecases.ProposeMeeting;

public class ProposeMeetingHandler {

  private final MessageSender messageSender;

  private final ExternalStorage<Message> messages;

  public ProposeMeetingHandler(
      final MessageSender messageSender,
      final ExternalStorage<Message> messages) {
    this.messageSender = messageSender;
    this.messages = messages;
  }

  public void handle(final ProposeMeeting pm) {
    final Message m = new MeetingProposal(
        pm.origin(),
        pm.author(),
        pm.when(),
        pm.minutes(),
        pm.where(),
        "You have been invited to the meeting.");
    messageSender.send(m);
    messages.save(m);
  }
}
