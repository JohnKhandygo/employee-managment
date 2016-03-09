package com.kspt.khandygo.core;

import com.kspt.khandygo.core.usecases.ProposeMeetingUseCase;
import com.kspt.khandygo.core.usecases.SendChatMessageUseCase;
import com.kspt.khandygo.core.usecases.SendNotificationUseCase;
import com.kspt.khandygo.core.usecases.TrackTimeUseCase;
import com.kspt.khandygo.core.usecases.UpdateEmployeeDataUseCase;

public interface UseCaseVisitor {

  void visit(final ProposeMeetingUseCase useCase);

  void visit(final SendChatMessageUseCase useCase);

  void visit(final SendNotificationUseCase useCase);

  void visit(final TrackTimeUseCase useCase);

  void visit(final UpdateEmployeeDataUseCase useCase);
}
