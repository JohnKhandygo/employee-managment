package com.kspt.khandygo.persistence;

import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@MappedSuperclass
@Table(name = "proposals")
@Accessors(fluent = true)
@Getter
public class ProposalEntry {

  @Id
  private final Integer id;

  private final long origin;

  @OneToOne
  private final AttributesEntry attributes;

  @ManyToOne
  private final EmployeeEntry author;

  @ManyToOne
  private final EmployeeEntry recipient;

  public ProposalEntry(
      final Integer id,
      final long origin,
      final AttributesEntry attributes,
      final EmployeeEntry author,
      final EmployeeEntry recipient) {
    this.id = id;
    this.origin = origin;
    this.attributes = attributes;
    this.author = author;
    this.recipient = recipient;
  }
}
