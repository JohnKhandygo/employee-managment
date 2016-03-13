package com.kspt.khandygo.persist;

import com.google.common.base.Preconditions;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "payments")
class PaymentEntry {

  @Id
  private final Integer id;

  @Column(nullable = false, unique = true)
  private final Long when;

  @Column(nullable = false)
  private final Long amount;

  @Column(nullable = false)
  private final Boolean payed;

  private PaymentEntry(
      final Integer id,
      final Long when,
      final Long amount,
      final Boolean payed) {
    this.id = id;
    this.when = when;
    this.amount = amount;
    this.payed = payed;
  }

  public static PaymentEntry newOne(
      final Long when,
      final Long amount) {
    Preconditions.checkNotNull(when);
    final long nowPlusTenMinutes = Instant.now().toEpochMilli() + TimeUnit.MINUTES.toMillis(10);
    Preconditions.checkState(when > nowPlusTenMinutes);
    Preconditions.checkNotNull(amount);
    Preconditions.checkState(amount > 0);
    return new PaymentEntry(null, when, amount, false);
  }

  public static PaymentEntry existedOne(
      final PaymentEntry existed,
      final Long amount) {
    Preconditions.checkNotNull(existed.id);
    Preconditions.checkNotNull(existed.when);
    final long nowPlusTenMinutes = Instant.now().toEpochMilli() + TimeUnit.MINUTES.toMillis(10);
    Preconditions.checkState(existed.when > nowPlusTenMinutes);
    Preconditions.checkNotNull(amount);
    Preconditions.checkState(amount > 0);
    Preconditions.checkNotNull(existed.payed);
    Preconditions.checkState(!existed.payed);
    return new PaymentEntry(existed.id, existed.when, amount, existed.payed);
  }
}
