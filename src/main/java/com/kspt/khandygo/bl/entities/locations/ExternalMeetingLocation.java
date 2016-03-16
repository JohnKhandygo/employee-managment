package com.kspt.khandygo.bl.entities.locations;

public interface ExternalMeetingLocation extends MeetingLocation {

  String country();

  String city();

  String street();

  int number();

  int building();

  int room();
}
