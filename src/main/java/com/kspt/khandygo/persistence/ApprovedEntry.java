package com.kspt.khandygo.persistence;

import com.kspt.khandygo.core.entities.Employee;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "approved")
@Accessors(fluent = true)
@Getter
public class ApprovedEntry extends Entry {

  @OneToOne
  private final SubjectEntry subject;

  @ManyToOne
  private final Employee owner;

  private final Employee employee;

  public ApprovedEntry(
      final Integer id,
      final boolean deleted,
      final SubjectEntry subject,
      final Employee owner,
      final Employee employee) {
    super(id, deleted);
    this.subject = subject;
    this.owner = owner;
    this.employee = employee;
  }
}