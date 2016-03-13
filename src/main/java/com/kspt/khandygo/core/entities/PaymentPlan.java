package com.kspt.khandygo.core.entities;

import com.kspt.khandygo.core.Entity;

public interface PaymentPlan extends Entity {

  Payment next();

  Employee employee();
}
