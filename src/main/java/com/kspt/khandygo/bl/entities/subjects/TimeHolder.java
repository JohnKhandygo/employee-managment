package com.kspt.khandygo.bl.entities.subjects;

import com.kspt.khandygo.core.entities.Subject;

public interface TimeHolder extends Subject {

  long duration();

  String description();
}
