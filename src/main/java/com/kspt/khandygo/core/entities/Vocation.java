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
public class Vocation {
  private final Employee employee;

  private final long when;

  private final long duration;

  private final boolean approved;

  private final boolean rejected;

  private final boolean cancelled;

  public com.kspt.khandygo.core.entities.Vocation approve() {
    Preconditions.checkState(!approved);
    Preconditions.checkState(!rejected);
    Preconditions.checkState(!cancelled);
    return new com.kspt.khandygo.core.entities.Vocation(employee, when, duration, true, false,
        false);
  }

  public com.kspt.khandygo.core.entities.Vocation reject() {
    Preconditions.checkState(!approved);
    Preconditions.checkState(!rejected);
    Preconditions.checkState(!cancelled);
    return new com.kspt.khandygo.core.entities.Vocation(employee, when, duration, false, true,
        false);
  }

  public com.kspt.khandygo.core.entities.Vocation cancel() {
    Preconditions.checkState(!approved);
    Preconditions.checkState(!rejected);
    Preconditions.checkState(!cancelled);
    return new com.kspt.khandygo.core.entities.Vocation(employee, when, duration, false, false,
        true);
  }

  public static com.kspt.khandygo.core.entities.Vocation newOne(
      final Employee employee,
      final long when,
      final long duration) {
    return new com.kspt.khandygo.core.entities.Vocation(employee, when, duration, false, false,
        false);
  }
}
