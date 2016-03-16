package com.kspt.khandygo.core.entities;

public interface Meeting extends TimeHolder {

  MeetingLocation where();

  Group group();
}
