package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.UseCase;
import com.kspt.khandygo.core.UseCaseVisitor;
import com.kspt.khandygo.core.entities.Employee;

public interface UpdateEmployeeDataUseCase extends UseCase {

  long origin();

  Employee info();

  default void accept(final UseCaseVisitor visitor) {
    visitor.visit(this);
  }
}
