package com.kspt.khandygo.bl.entities.subjects.th;

import com.kspt.khandygo.bl.entities.subjects.TimeHolder;
import com.kspt.khandygo.core.SubjectVisitor;

public interface SpentTime extends TimeHolder {

  @Override
  default void accept(final SubjectVisitor v) {
    v.visit(this);
  }
}
