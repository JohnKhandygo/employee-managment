package com.kspt.khandygo.bl.entities.subjects.payments;

import com.kspt.khandygo.bl.entities.subjects.Payment;
import com.kspt.khandygo.core.SubjectVisitor;

public interface Award extends Payment {

  @Override
  default <T> T accept(final SubjectVisitor<T> v) {
    return v.visit(this);
  }
}
