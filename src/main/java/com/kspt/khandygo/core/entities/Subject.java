package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.SubjectVisitor;

public interface Subject {

  int id();

  long when();

  Employee employee();

  void accept(final SubjectVisitor v);
}
