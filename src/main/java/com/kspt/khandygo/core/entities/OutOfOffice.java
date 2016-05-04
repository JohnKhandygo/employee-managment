package com.kspt.khandygo.core.entities;

import com.google.common.base.Preconditions;
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
public class OutOfOffice {
  private final Employee employee;

  private final long when;

  private final long duration;

  private final String reason;

  private final boolean cancelled;

  public com.kspt.khandygo.core.entities.OutOfOffice cancel() {
    Preconditions.checkState(!cancelled);
    return new com.kspt.khandygo.core.entities.OutOfOffice(employee, when, duration, reason, true);
  }

  public static com.kspt.khandygo.core.entities.OutOfOffice newOne(
      final Employee employee,
      final long when,
      final long duration,
      final String reason) {
    return new com.kspt.khandygo.core.entities.OutOfOffice(employee, when, duration, reason, false);
  }
}
