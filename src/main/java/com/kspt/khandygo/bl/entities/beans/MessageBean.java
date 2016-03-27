package com.kspt.khandygo.bl.entities.beans;

import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.sys.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class MessageBean implements Message {

  private final Employee author;

  private final long origin;

  private final Object body;
}
