package com.kspt.khandygo.core.entities;

public interface Approvable<T extends Answer> {

  T approveBy(final Employee responsible);
}
