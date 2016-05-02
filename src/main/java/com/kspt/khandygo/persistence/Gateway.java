package com.kspt.khandygo.persistence;

import com.kspt.khandygo.persistence.Finder.SelectAllOrSpecifyCondition;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class Gateway {

  private final Finder finder;

  private final Writer writer;

  /*private final SqlServer server;*/

  <T> SelectAllOrSpecifyCondition<T> find(final Class<T> toFind) {
    return finder.find(toFind);
  }

  <T> T save(final T toSave) {
    return writer.save(toSave);
  }

  <T> T update(final T toUpdate) {
    return writer.update(toUpdate);
  }
}