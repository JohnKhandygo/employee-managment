package com.kspt.khandygo.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Proposal {

  private final long origin;

  private final Employee author;

  private final Subject subject;
}
