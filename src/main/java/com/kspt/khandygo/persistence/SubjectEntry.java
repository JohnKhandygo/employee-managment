package com.kspt.khandygo.persistence;

import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@MappedSuperclass
@Table(name = "subjects")
@Accessors(fluent = true)
@Getter
public class SubjectEntry extends Entry {

  private final long when;

  public SubjectEntry(final Integer id, final boolean deleted, final long when) {
    super(id, deleted);
    this.when = when;
  }
}
