package com.kspt.khandygo.bl.entities.th;

import com.kspt.khandygo.core.entities.TimeHolder;

public interface Vocation extends TimeHolder {
  boolean accepted();

  boolean canceled();

  Vocation accept();

  Vocation decline();
}
