package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Proposal extends Entity {

  long origin();

  Employee author();

  Subject subject();

  /*Employee recipient();*/

  /*boolean pending();

  Proposal reject();

  Proposal approve();*/
}
