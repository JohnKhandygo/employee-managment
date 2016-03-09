package com.kspt.khandygo.core.usecases;

public interface TrackTimeUseCase extends SendMessageUseCase {

  long when();

  int onBehalfOf();

  int minutes();
}
