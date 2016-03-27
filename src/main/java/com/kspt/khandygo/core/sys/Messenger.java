package com.kspt.khandygo.core.sys;

import com.kspt.khandygo.core.entities.Employee;
import java.util.Set;

public interface Messenger {

  default void send(final Set<Employee> recipients, final Message message) {
    recipients.stream().forEach(r -> send(r, message));
  }

  void send(final Employee recipient, final Message message);
}
