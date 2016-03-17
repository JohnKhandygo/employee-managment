package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;

public interface AnswerApi {

  void approve(final int id, final Employee responsible);

  void reject(final int id, final Employee responsible);
}
