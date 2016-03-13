package com.kspt.khandygo.persist;

import com.google.common.base.Preconditions;
import com.kspt.khandygo.core.entities.Employee;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
class EmployeeEntry {
  @Id
  private final Integer id;

  @Column(nullable = false, unique = true)
  private final String name;

  @ManyToOne
  private final Employee manager;

  @ManyToOne
  private final Employee paymaster;

  @OneToOne
  private final PaymentPlanEntry paymentPlan;

  private EmployeeEntry(
      final Integer id,
      final String name,
      final Employee manager,
      final Employee paymaster,
      final PaymentPlanEntry paymentPlan) {
    this.id = id;
    this.name = name;
    this.manager = manager;
    this.paymaster = paymaster;
    this.paymentPlan = paymentPlan;
  }

  public static EmployeeEntry newOne(
      final String name,
      final Employee manager,
      final Employee paymaster,
      final PaymentPlanEntry paymentPlan) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(paymaster);
    Preconditions.checkNotNull(paymentPlan);
    return new EmployeeEntry(null, name, manager, paymaster, paymentPlan);
  }

  public static EmployeeEntry existedOne(
      final Integer id,
      final String name,
      final Employee manager,
      final Employee paymaster,
      final PaymentPlanEntry paymentPlan) {
    Preconditions.checkNotNull(id);
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(paymaster);
    Preconditions.checkNotNull(paymentPlan);
    return new EmployeeEntry(id, name, manager, paymaster, paymentPlan);
  }
}
