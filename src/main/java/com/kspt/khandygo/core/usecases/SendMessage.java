package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.SendMessageUseCaseVisitor;
import java.util.List;

public interface SendMessage {

  int author();

  long origin();

  List<Integer> recipients();

  void accept(final SendMessageUseCaseVisitor visitor);
}
