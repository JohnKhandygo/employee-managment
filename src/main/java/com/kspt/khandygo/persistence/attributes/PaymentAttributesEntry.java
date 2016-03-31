package com.kspt.khandygo.persistence.attributes;

import com.avaje.ebean.annotation.EnumValue;
import com.kspt.khandygo.persistence.AttributesEntry;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
@Accessors(fluent = true)
@Getter
public class PaymentAttributesEntry extends AttributesEntry {

  private final PaymentType type;

  private final long amount;

  public PaymentAttributesEntry(
      final Integer id,
      final long when,
      final PaymentType type,
      final long amount) {
    super(id, when);
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
