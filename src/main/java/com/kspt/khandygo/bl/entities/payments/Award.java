package com.kspt.khandygo.bl.entities.payments;

import com.kspt.khandygo.core.entities.Approved;
import com.kspt.khandygo.core.entities.Payment;
import com.kspt.khandygo.core.entities.Pending;

public interface Award extends Payment, Pending, Approved {
}
