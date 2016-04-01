package com.kspt.khandygo.persistence;

import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "proposals")
@Accessors(fluent = true)
@Getter
public class ProposalEntry extends Entry {

  private final long origin;

  @OneToOne
  private final SubjectEntry subject;

  @ManyToOne
  private final EmployeeEntry author;

  @ManyToOne
  private final EmployeeEntry recipient;

  public ProposalEntry(
      final Integer id,
      final boolean deleted,
      final long origin,
      final SubjectEntry subject,
      final EmployeeEntry author,
      final EmployeeEntry recipient) {
    super(id, deleted);
    this.origin = origin;
    this.subject = subject;
    this.author = author;
    this.recipient = recipient;
  }
}
