package com.kspt.khandygo.bl.locations;

import com.kspt.khandygo.core.entities.MeetingLocation;

public interface ExternalMeetingLocation extends MeetingLocation {

  String country();

  String city();

  String street();

  int number();

  int building();

  int room();
}
