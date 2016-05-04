package com.kspt.khandygo.core.entities;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Award {

  private final Employee employee;

  private final long when;

  private final long amount;

  private final boolean approved;

  private final boolean rejected;

  private final boolean cancelled;

  public com.kspt.khandygo.core.entities.Award approve() {
    Preconditions.checkState(!approved);
    Preconditions.checkState(!rejected);
    Preconditions.checkState(!cancelled);
    return new com.kspt.khandygo.core.entities.Award(employee, when, amount, true, false, false);
  }

  public com.kspt.khandygo.core.entities.Award reject() {
    Preconditions.checkState(!approved);
    Preconditions.checkState(!rejected);
    Preconditions.checkState(!cancelled);
    return new com.kspt.khandygo.core.entities.Award(employee, when, amount, false, true, false);
  }

  public com.kspt.khandygo.core.entities.Award cancel() {
    Preconditions.checkState(!approved);
    Preconditions.checkState(!rejected);
    Preconditions.checkState(!cancelled);
    return new com.kspt.khandygo.core.entities.Award(employee, when, amount, false, false, true);
  }

  public static com.kspt.khandygo.core.entities.Award newOne(
      final Employee employee,
      final long when,
      final long amount) {
    return new com.kspt.khandygo.core.entities.Award(employee, when, amount, false, false, false);
  }
}
