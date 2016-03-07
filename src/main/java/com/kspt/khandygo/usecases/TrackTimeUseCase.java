package com.kspt.khandygo.usecases;

public interface TrackTimeUseCase extends SendMessageUseCase {

  long when();

  int onBehalfOf();

  int minutes();
}
