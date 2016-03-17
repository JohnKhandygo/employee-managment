package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Approved extends Entity {

  Approved cancelBy(final Employee owner);
}
