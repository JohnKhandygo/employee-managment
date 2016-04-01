package com.kspt.khandygo.persistence;

import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Accessors(fluent = true)
@Getter
public class Entry {

  @Id
  private final Integer id;

  private final boolean deleted;

  public Entry(final Integer id, final boolean deleted) {
    this.id = id;
    this.deleted = deleted;
  }
}
