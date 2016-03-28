package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.bl.entities.payments.Award;
import com.kspt.khandygo.core.entities.approved.Payment;

public interface PaymentsApi {

  /*List<Payment> get(final int id, final long since, final long till);*/

  Payment add(final Payment p);

  Payment award(final Award award);
}
