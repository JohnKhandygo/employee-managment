package com.kspt.khandygo.persistence;

import com.kspt.khandygo.core.entities.Employee;
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
@Table(name = "approved")
@Accessors(fluent = true)
@Getter
public class ApprovedEntry {

  @Id
  private final Integer id;

  @OneToOne
  private final AttributesEntry attributes;

  @ManyToOne
  private final Employee owner;

  private final Employee employee;

  public ApprovedEntry(
      final Integer id,
      final AttributesEntry attributes,
      final Employee owner,
      final Employee employee) {
    this.id = id;
    this.attributes = attributes;
    this.owner = owner;
    this.employee = employee;
  }
}