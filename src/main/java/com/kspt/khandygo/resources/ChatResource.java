package com.kspt.khandygo.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/1.0/chat")
public interface ChatResource {

  @POST
  CharCreated newOne(
      final @QueryParam("author") int author,
      final @QueryParam("name") String name,
      final @QueryParam("participants") List<Integer> participants,
      final @QueryParam("initial_message") String initialMessage);

  @POST
  @Path("/{chat_id}/invite")
  ParticipantAdded inviteParticipant(
      final @PathParam("chat_id") int chatId,
      final @QueryParam("participant_id") int participantId);

  @POST
  @Path("/{chat_id}")
  MessageSent sendMessage(
      final @PathParam("chat_id") int chatId,
      final @QueryParam("message") String message);

  @POST
  @Path("/{chat_id}/leave")
  ChatLeavedSuccessfully leave(
      final @PathParam("chat_id") int chatId,
      final @QueryParam("participant_id") int participantId);

  @POST
  @Path("/{chat_id}/close")
  ChatClosedSuccessfully close(
      final @PathParam("chat_id") int chatId);
}

interface CharCreated {
  int id();
}

interface ParticipantAdded {
}

interface MessageSent {

}

interface ChatLeavedSuccessfully {

}

interface ChatClosedSuccessfully {

}