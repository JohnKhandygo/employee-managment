package com.kspt.khandygo.bl.entities.subjects.payments;

import com.kspt.khandygo.bl.entities.subjects.Payment;
import com.kspt.khandygo.core.SubjectVisitor;

public interface Award extends Payment {

  @Override
  default void accept(final SubjectVisitor v) {
    v.visit(this);
  }
}
