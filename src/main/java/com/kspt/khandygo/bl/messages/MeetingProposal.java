package com.kspt.khandygo.bl.messages;

import com.kspt.khandygo.core.entities.MeetingLocation;
import com.kspt.khandygo.core.entities.Message;

public class MeetingProposal implements Message {

  private final long origin;

  private final int author;

  private final long when;

  private final long minutes;

  private final MeetingLocation where;

  private final String text;

  public MeetingProposal(
      final long origin,
      final int author,
      final long when,
      final long minutes,
      final MeetingLocation where,
      final String text) {
    this.origin = origin;
    this.author = author;
    this.when = when;
    this.minutes = minutes;
    this.where = where;
    this.text = text;
  }

  @Override
  public long origin() {
    return origin;
  }

  @Override
  public int author() {
    return author;
  }

  public long when() {
    return when;
  }

  public long minutes() {
    return minutes;
  }

  public MeetingLocation where() {
    return where;
  }

  public String text() {
    return text;
  }
}
