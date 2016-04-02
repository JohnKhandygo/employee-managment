package com.kspt.khandygo.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Approved {

  private final Employee owner;

  private final Subject subject;
}
