package com.kspt.khandygo.bl.messages;

import com.kspt.khandygo.core.entities.Message;

public class TrackTimeMessage implements Message {
  private final long origin;

  private final int author;

  private final int onBehalfOf;

  private final long when;

  private final long minutes;

  public TrackTimeMessage(
      final long origin,
      final int author,
      final int onBehalfOf,
      final long when,
      final long minutes) {
    this.origin = origin;
    this.author = author;
    this.onBehalfOf = onBehalfOf;
    this.when = when;
    this.minutes = minutes;
  }

  @Override
  public int author() {
    return author;
  }

  @Override
  public long origin() {
    return origin;
  }
}
