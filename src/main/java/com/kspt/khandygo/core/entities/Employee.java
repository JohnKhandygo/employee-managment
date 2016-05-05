package com.kspt.khandygo.core.entities;

/*@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString*/
public interface Employee {

  /*private final String name;

  private final Employee manager;

  private final Employee paymaster;*/

  String name();

  Employee manager();

  Employee paymaster();
}
