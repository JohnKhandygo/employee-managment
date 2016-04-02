package com.kspt.khandygo.bl.entities.subjects.th;

import com.kspt.khandygo.bl.entities.subjects.TimeHolder;
import com.kspt.khandygo.core.SubjectVisitor;

public interface Vocation extends TimeHolder {

  @Override
  default <T> T accept(final SubjectVisitor<T> v) {
    return v.visit(this);
  }
}
