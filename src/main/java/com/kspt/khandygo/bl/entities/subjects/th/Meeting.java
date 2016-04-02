package com.kspt.khandygo.bl.entities.subjects.th;

import com.kspt.khandygo.bl.entities.subjects.TimeHolder;
import com.kspt.khandygo.core.SubjectVisitor;
import com.kspt.khandygo.core.entities.Employee;
import java.util.List;

public interface Meeting extends TimeHolder {

  List<Employee> participants();

  @Override
  default void accept(final SubjectVisitor v) {
    v.visit(this);
  }
}
