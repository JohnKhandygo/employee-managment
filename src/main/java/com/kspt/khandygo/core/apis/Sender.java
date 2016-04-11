package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import java.util.List;

interface Sender {
  void sendToAll(final Object object, final List<Employee> recipients);
}