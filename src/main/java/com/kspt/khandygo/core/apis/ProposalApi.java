package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject;

public interface ProposalApi {

  int propose(final Employee author, final Subject subject);

  int approve(final int id, final Employee requester);

  void reject(final int id, final Employee requester);
}
