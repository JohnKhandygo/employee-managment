package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Pending extends Entity {

  Pending rejectBy(final Employee responsible);

  Pending approveBy(final Employee responsible);
}
