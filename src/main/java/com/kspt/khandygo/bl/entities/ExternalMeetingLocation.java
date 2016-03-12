package com.kspt.khandygo.bl.entities;

import com.kspt.khandygo.core.entities.MeetingLocation;

public interface ExternalMeetingLocation extends MeetingLocation {

  String country();

  String city();

  String street();

  int number();

  int building();

  int room();
}
