package com.kspt.khandygo.core.entities.approved;

import com.kspt.khandygo.core.entities.Subject;

public interface TimeHolder extends Subject {

  long duration();

  String description();
}
