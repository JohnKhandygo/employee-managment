package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Meeting;

public interface MeetingsApi {

  void accept(final int id);

  void decline(final int id);

  void propose(final Meeting meeting);

  void cancel(final int id);
}
