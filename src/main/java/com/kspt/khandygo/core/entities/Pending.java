package com.kspt.khandygo.core.entities;

public interface Pending {
  default boolean pending() {
    return !accepted() && !rejected();
  }

  boolean accepted();

  boolean rejected();

  Pending rejectBy(final Employee responsible);

  Pending approveBy(final Employee responsible);
}
