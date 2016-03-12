package com.kspt.khandygo.bl.entities;

import com.google.common.collect.Lists;
import static java.util.stream.Collectors.toList;
import java.util.List;

public class ChatRoom {

  private final int id;

  private final int author;

  private final List<Integer> participants;

  public ChatRoom(final int id, final int author, final List<Integer> participants) {
    this.id = id;
    this.author = author;
    this.participants = participants;
  }

  public int id() {
    return id;
  }

  public int author() {
    return author;
  }

  public List<Integer> participants() {
    return participants;
  }

  public ChatRoom addParticipant(final int participantId) {
    final List<Integer> participantsCopy = Lists.newArrayList(participants);
    participantsCopy.add(participantId);
    return new ChatRoom(id, author, participantsCopy);
  }

  public ChatRoom removeParticipant(final int participantId) {
    final List<Integer> participantsCopy = participants.stream()
        .filter(id -> id != participantId)
        .collect(toList());
    return new ChatRoom(id, author, participantsCopy);
  }
}
