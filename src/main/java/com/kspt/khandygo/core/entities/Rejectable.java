package com.kspt.khandygo.core.entities;

public interface Rejectable<T extends Rejectable> {

  T rejectBy(final Employee responsible);
}
