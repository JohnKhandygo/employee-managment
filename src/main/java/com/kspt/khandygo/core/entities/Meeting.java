package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface Meeting extends Entity {

  long when();

  MeetingLocation where();

  Employee author();

  Group group();
}
