package com.kspt.khandygo.bl.messages;

import com.kspt.khandygo.core.entities.Message;

public class ChatMessage implements Message {
  private final long origin;

  private final int author;

  private final String text;

  public ChatMessage(final long origin, final int author, final String text) {
    this.origin = origin;
    this.author = author;
    this.text = text;
  }

  @Override
  public int author() {
    return author;
  }

  @Override
  public long origin() {
    return origin;
  }

  public String text() {
    return text;
  }
}
