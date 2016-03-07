package com.kspt.khandygo.core;

public interface ExternalMeetingLocation extends MeetingLocation {

  String country();

  String city();

  String street();

  int number();

  int building();

  int room();
}
