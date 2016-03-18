package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Proposal;

public interface ProposalApi {

  Proposal propose(final Proposal proposal);

  void approve(final int id, final Employee requester);

  void reject(final int id, final Employee requester);
}
