package com.kspt.khandygo.bl.messages.decorators;

import com.kspt.khandygo.bl.messages.MessengerDecorator;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.sys.Message;
import com.kspt.khandygo.core.sys.Messenger;

public class BookkeepingMessenger extends MessengerDecorator {

  private final Repository<Message> messages;

  public BookkeepingMessenger(
      final Messenger delegate,
      final Repository<Message> messages) {
    super(delegate);
    this.messages = messages;
  }

  @Override
  public void send(final Employee recipient, final Message message) {
    super.send(recipient, message);
    messages.add(message);
  }
}
