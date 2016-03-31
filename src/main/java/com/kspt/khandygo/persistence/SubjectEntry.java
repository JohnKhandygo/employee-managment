package com.kspt.khandygo.persistence;

import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@MappedSuperclass
@Table(name = "attributes")
@Accessors(fluent = true)
@Getter
public class SubjectEntry {

  @Id
  private final Integer id;

  private final long when;

  public SubjectEntry(final Integer id, final long when) {
    this.id = id;
    this.when = when;
  }
}
