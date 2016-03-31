package com.kspt.khandygo.persistence;

import com.kspt.khandygo.core.entities.Employee;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
@Accessors(fluent = true)
@Getter
class EmployeeEntry {

  @Id
  private final Integer id;

  private final String name;

  @ManyToOne
  private final Employee manager;

  @ManyToOne
  private final Employee paymaster;

  public EmployeeEntry(
      final Integer id,
      final String name,
      final Employee manager,
      final Employee paymaster) {
    this.id = id;
    this.name = name;
    this.manager = manager;
    this.paymaster = paymaster;
  }
}