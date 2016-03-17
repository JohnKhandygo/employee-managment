package com.kspt.khandygo.core.apis;

import com.kspt.khandygo.core.entities.Payment;
import java.util.List;

public interface PaymentsApi {

  List<Payment> get(final int id, final long since, final long till);

  Payment add(final Payment p);
}
