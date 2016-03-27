package com.kspt.khandygo.core;

public interface Repository<E extends Entity> {

  E update(final E e);

  E add(final E e);

  E get(final int id);

  E delete(final int id);

  boolean contains(final int id);
}
