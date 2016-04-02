package com.kspt.khandygo.core;

import com.kspt.khandygo.bl.entities.subjects.payments.Award;
import com.kspt.khandygo.bl.entities.subjects.payments.RegularPayment;
import com.kspt.khandygo.bl.entities.subjects.th.Meeting;
import com.kspt.khandygo.bl.entities.subjects.th.OutOfOffice;
import com.kspt.khandygo.bl.entities.subjects.th.SpentTime;
import com.kspt.khandygo.bl.entities.subjects.th.Vocation;

public interface SubjectVisitor<T> {

  T visit(final RegularPayment subject);

  T visit(final Award subject);

  T visit(final Meeting subject);

  T visit(final OutOfOffice subject);

  T visit(final SpentTime subject);

  T visit(final Vocation subject);
}
