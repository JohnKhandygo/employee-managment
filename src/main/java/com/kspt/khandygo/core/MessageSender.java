package com.kspt.khandygo.core;

import com.kspt.khandygo.core.entities.Message;

public interface MessageSender {

  void send(final int recipient, final Message message);

}
