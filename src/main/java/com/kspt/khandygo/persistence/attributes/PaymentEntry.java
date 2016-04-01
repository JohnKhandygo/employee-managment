package com.kspt.khandygo.persistence.attributes;

import com.avaje.ebean.annotation.EnumValue;
import com.kspt.khandygo.persistence.SubjectEntry;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
@Accessors(fluent = true)
@Getter
public class PaymentEntry extends SubjectEntry {

  private final PaymentType type;

  private final long amount;

  public PaymentEntry(
      final Integer id,
      final boolean deleted,
      final long when,
      final PaymentType type,
      final long amount) {
    super(id, deleted, when);
    this.type = type;
    this.amount = amount;
  }

  public enum PaymentType {
    @EnumValue("R")
    REGULAR,

    @EnumValue("A")
    AWARD
  }
}
