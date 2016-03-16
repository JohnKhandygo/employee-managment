package com.kspt.khandygo.bl.messages;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.sys.Message;
import com.kspt.khandygo.core.sys.Messenger;

public class MessengerDecorator implements Messenger {

  private final Messenger delegate;

  public MessengerDecorator(final Messenger delegate) {
    this.delegate = delegate;
  }

  @Override
  public void send(final Employee recipient, final Message message) {
    delegate.send(recipient, message);
  }
}
