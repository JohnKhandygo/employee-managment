package com.kspt.khandygo.bl.entities.payments;

import com.kspt.khandygo.core.entities.Approvable;
import com.kspt.khandygo.core.entities.Payment;

public interface Award extends Payment, Approvable<Award> {
}
