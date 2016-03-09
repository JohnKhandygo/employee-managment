package com.kspt.khandygo.core;

public interface UseCase {

  void accept(final UseCaseVisitor visitor);

}
