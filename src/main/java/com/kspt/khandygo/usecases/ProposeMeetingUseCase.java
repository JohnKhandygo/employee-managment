package com.kspt.khandygo.usecases;

import com.kspt.khandygo.core.MeetingLocation;

public interface ProposeMeetingUseCase extends SendMessageUseCase {

  long when();

  MeetingLocation where();

}
