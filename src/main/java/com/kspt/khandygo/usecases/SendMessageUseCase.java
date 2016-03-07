package com.kspt.khandygo.usecases;

import java.util.List;

public interface SendMessageUseCase {

  int author();

  long origin();

  List<Integer> recipients();

}
