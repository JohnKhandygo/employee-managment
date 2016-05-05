package com.kspt.khandygo.persistence.dao;

import com.kspt.khandygo.core.entities.Employee;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
class EmployeeEntity implements Employee {
  @Id
  private final Integer id;

  @Column(nullable = false)
  private final String login;

  @Column(nullable = false)
  private final String name;

  @Column(nullable = true)
  private final EmployeeEntity manager;

  @Column(nullable = true)
  private final EmployeeEntity paymaster;
}
