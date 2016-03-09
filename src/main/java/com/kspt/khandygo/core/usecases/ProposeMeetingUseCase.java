package com.kspt.khandygo.core.usecases;

import com.kspt.khandygo.core.entities.MeetingLocation;

public interface ProposeMeetingUseCase extends SendMessageUseCase {

  long when();

  MeetingLocation where();

}
