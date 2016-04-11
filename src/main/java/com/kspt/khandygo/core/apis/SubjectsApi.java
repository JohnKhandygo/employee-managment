package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;

public interface SubjectsApi {

  int add(final Subject subject, final Employee author);

  Subject cancel(final int id, final Employee requester);
}

