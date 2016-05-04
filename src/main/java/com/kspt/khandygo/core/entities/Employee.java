package com.kspt.khandygo.core.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
public class Employee {

  private final String name;

  private final Employee manager;

  private final Employee paymaster;
}
