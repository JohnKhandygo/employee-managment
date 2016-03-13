package com.kspt.khandygo.persist;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "payment_plans")
class PaymentPlanEntry {

  @Id
  private final Integer id;

  @OneToOne
  private final EmployeeEntry employee;

  @OneToMany
  private final List<PaymentEntry> payments;

  private PaymentPlanEntry(
      final Integer id,
      final EmployeeEntry employee,
      final List<PaymentEntry> payments) {
    this.id = id;
    this.employee = employee;
    this.payments = payments;
  }

  public static PaymentPlanEntry newOne(final EmployeeEntry employee) {
    Preconditions.checkNotNull(employee);
    return new PaymentPlanEntry(null, employee, Lists.newArrayList());
  }

  public static PaymentPlanEntry existedOne(
      final Integer id,
      final EmployeeEntry employee,
      final List<PaymentEntry> payments) {
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(employee);
    Preconditions.checkNotNull(payments);
    Preconditions.checkState(!payments.isEmpty());
    return new PaymentPlanEntry(id, employee, payments);
  }
}
