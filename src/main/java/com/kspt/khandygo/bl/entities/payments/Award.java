package com.kspt.khandygo.bl.entities.payments;

import com.kspt.khandygo.core.entities.Payment;

public interface Award extends Payment {
  boolean approved();

  Award approve();

  Award cancel();
}
