package com.kspt.khandygo.bl;

import com.kspt.khandygo.core.MessageSender;
import com.kspt.khandygo.core.Repository;
import com.kspt.khandygo.core.apis.MeetingsApi;
import com.kspt.khandygo.core.entities.Employee;
import com.kspt.khandygo.core.entities.Meeting;
import com.kspt.khandygo.core.entities.Message;
import java.time.Instant;

public class MeetingsService implements MeetingsApi {

  private final Repository<Meeting> meetingsRepository;

  private final MessageSender messageSender;

  public MeetingsService(
      final Repository<Meeting> meetingsRepository,
      final MessageSender messageSender) {
    this.meetingsRepository = meetingsRepository;
    this.messageSender = messageSender;
  }

  @Override
  public void accept(final int id) {
    throw new UnsupportedOperationException("not implemented yet.");
  }

  @Override
  public void decline(final int id) {
    throw new UnsupportedOperationException("not implemented yet.");
  }

  @Override
  public void propose(final Meeting meeting) {
    meetingsRepository.add(meeting);
    messageSender.send(meeting.group().id(), messageForMeeting(meeting));
  }

  private Message messageForMeeting(final Meeting meeting) {
    return new Message() {
      @Override
      public Employee author() {
        return meeting.author();
      }

      @Override
      public long origin() {
        return Instant.now().toEpochMilli();
      }

      @Override
      public Object body() {
        return meeting;
      }

      @Override
      public int id() {
        return -1;
      }
    };
  }

  @Override
  public void cancel(final int id) {
    meetingsRepository.delete(id);
  }
}
