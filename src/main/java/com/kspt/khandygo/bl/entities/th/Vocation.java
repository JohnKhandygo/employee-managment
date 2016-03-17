package com.kspt.khandygo.bl.entities.th;

import com.kspt.khandygo.core.entities.Answer;
import com.kspt.khandygo.core.entities.Approvable;
import com.kspt.khandygo.core.entities.Rejectable;
import com.kspt.khandygo.core.entities.TimeHolder;

public interface Vocation extends TimeHolder, Answer, Approvable<Vocation>, Rejectable<Vocation> {
}
