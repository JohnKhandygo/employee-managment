package com.kspt.khandygo.core.sys;

public interface MessageSender {

  void send(final int recipient, final Message message);

}
