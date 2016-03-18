package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Proposal extends Entity {

  long origin();

  long when();

  Employee author();

  Employee recipient();

  Approved subject();

  boolean pending();

  Proposal rejectBy(final Employee responsible);

  Proposal approveBy(final Employee responsible);
}
