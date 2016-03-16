package com.kspt.khandygo.core.entities;

public interface Meeting extends TimeReservation {

  MeetingLocation where();

  Group group();
}
