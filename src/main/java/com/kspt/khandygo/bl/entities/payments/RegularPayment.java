package com.kspt.khandygo.bl.entities.payments;

import com.kspt.khandygo.core.entities.Payment;

public interface RegularPayment extends Payment {
  boolean received();
}
