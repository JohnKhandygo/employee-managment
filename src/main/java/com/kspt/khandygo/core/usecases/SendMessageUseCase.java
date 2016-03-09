package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.UseCase;
import java.util.List;

public interface SendMessageUseCase extends UseCase {

  int author();

  long origin();

  List<Integer> recipients();
}
