package com.kspt.khandygo.persistence;

public interface Gateway<E extends Entry> {

  E get(final int id);

  E save(final Entry toSave);

  E update(final Entry toUpdate);
}
