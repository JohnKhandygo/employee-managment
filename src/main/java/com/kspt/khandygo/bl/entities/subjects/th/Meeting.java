package com.kspt.khandygo.bl.entities.subjects.th;

import com.kspt.khandygo.bl.entities.subjects.TimeHolder;
import com.kspt.khandygo.core.SubjectVisitor;
import com.kspt.khandygo.core.entities.Employee;
import java.util.List;

public interface Meeting extends TimeHolder {

  List<Employee> participants();

  @Override
  default <T> T accept(final SubjectVisitor<T> v) {
    return v.visit(this);
  }
}
