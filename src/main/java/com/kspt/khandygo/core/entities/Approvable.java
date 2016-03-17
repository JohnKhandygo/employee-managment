package com.kspt.khandygo.core.entities;

public interface Approvable<T extends Approvable> {

  T approveBy(final Employee responsible);
}
