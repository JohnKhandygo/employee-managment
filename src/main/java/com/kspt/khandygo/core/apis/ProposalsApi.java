package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Subject.Proposal;

public interface ProposalsApi {

  int propose(final Proposal proposal, final Employee author);

  void approve(final int id, final Employee requester);

  void reject(final int id, final Employee requester);

  void update(final int id, final Proposal proposal, final Employee requester);

  Proposal cancel(final int id, final Employee employee);
}

