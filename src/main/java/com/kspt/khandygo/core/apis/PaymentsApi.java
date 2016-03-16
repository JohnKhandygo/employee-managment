package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Payment;

public interface PaymentsApi {

  Payment add(final Payment p);

  void accept(final int id);

  void decline(final int id);

  void cancel(final int id);
}
