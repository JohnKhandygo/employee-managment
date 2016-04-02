package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.SubjectVisitor;

public interface Subject {

  long when();

  Employee employee();

  <T> T accept(final SubjectVisitor<T> v);
}
