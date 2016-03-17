package com.kspt.khandygo.core.entities;

public interface Rejectable<T extends Answer> {

  T rejectBy(final Employee responsible);
}
