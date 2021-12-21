package tr.com.obss.googlecalendarservice.service;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Value;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.EntryPoint;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.com.obss.googlecalendarservice.dto.CalendarEventDTO;
import tr.com.obss.googlecalendarservice.dto.DeleteEventDTO;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.exception.GoogleBadRequestException;
import tr.com.obss.googlecalendarservice.mapper.GoogleMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarService {
  private final GoogleAuthService authService;
  private final GoogleAccountService accountService;
  private final GoogleMapper mapper;
  @Value("${google-calendar.default-time-zone}")
  private String timeZone;

  private final String CONFERENCE_TYPE = "hangoutsMeet";

  public CalendarEventDTO scheduleEvent(CalendarEventDTO calendarEvent) {
    try {
      GoogleAccountDTO googleAccountDTO = accountService.getGoogleAccount();
      Calendar client =
          authService.buildCalendarClient(googleAccountDTO, calendarEvent.getCreator());
      Event event = mapper.toEvent(calendarEvent);
      event =
          client
              .events()
              .insert(calendarEvent.getCreator(), event)
              .setSendUpdates("all")
              .setConferenceDataVersion(1)
              .setQuotaUser(calendarEvent.getCreator())
              .execute();
      calendarEvent.setMeetingUrl(event.getHangoutLink());
      calendarEvent.setEventId(event.getId());
      log.info("Event sucessfully created",event);
      return calendarEvent;

    } catch (IOException e) {
      log.error(e.getMessage(),calendarEvent);
      throw new GoogleBadRequestException("Failed to schedule google event",Collections.singleton("googleCalendar"));
    }
  }

  private void setAttendees(CalendarEventDTO calendarEvent, Event event) {
    List<EventAttendee> attendees =
        calendarEvent.getEventAttendees().stream()
            .map(attendee -> new EventAttendee().setEmail(attendee))
            .collect(Collectors.toList());
    event.setAttendees(attendees);
  }

  public CalendarEventDTO updateEvent(CalendarEventDTO calendarEvent) {

    GoogleAccountDTO googleAccountDTO = accountService.getGoogleAccount();

    Calendar client =
        authService.buildCalendarClient(googleAccountDTO, calendarEvent.getCreator());
    try {
      Event event =
          client.events().get(calendarEvent.getCreator(), calendarEvent.getEventId()).execute();
      event.setSummary(calendarEvent.getSummary());
      convertAndSetDates(calendarEvent, event);
      event.setDescription(calendarEvent.getDescription());
      setAttendees(calendarEvent, event);
      Event.Creator creator = new Event.Creator();
      creator.setEmail(calendarEvent.getCreator());
      event.setCreator(creator);
      client
          .events()
          .update(calendarEvent.getCreator(), calendarEvent.getEventId(), event)
          .setSendUpdates("all")
          .setConferenceDataVersion(1)
          .setQuotaUser(calendarEvent.getCreator())
          .execute();
      log.info("Event sucessfully updated",event);

      return calendarEvent;
    } catch (IOException e) {
      log.error("Failed to create google event", e,calendarEvent);
      throw new GoogleBadRequestException("Failed to update google event",Collections.singleton("googleCalendar"));
    }
  }
  public String deleteEvent(DeleteEventDTO deleteEventDTO){
    GoogleAccountDTO googleAccountDTO = accountService.getGoogleAccount();

    Calendar client =
            authService.buildCalendarClient(googleAccountDTO, deleteEventDTO.getCreator());
    try {
        client.events().delete(deleteEventDTO.getCreator(), deleteEventDTO.getEventId())
                .setSendUpdates("all")
                .setQuotaUser(deleteEventDTO.getCreator())
                .execute();
        return deleteEventDTO.getEventId();
    } catch (IOException e) {
      log.error("Failed to delete google event", e,deleteEventDTO);
      throw new GoogleBadRequestException("Failed to delete google event",Collections.singleton("googleCalendar"));
    }
  }

  private void convertAndSetDates(CalendarEventDTO calendarEvent, Event event) {
    EventDateTime start =
        new EventDateTime()
            .setDateTime(new DateTime(calendarEvent.getStart()))
            .setTimeZone(timeZone);
    event.setStart(start);
    EventDateTime end =
        new EventDateTime().setDateTime(new DateTime(calendarEvent.getEnd())).setTimeZone(timeZone);
    event.setEnd(end);
  }

  private ConferenceData createConference() {
    ConferenceData conferenceData = new ConferenceData();
    // TODO try password
//    EntryPoint entryPoint = new EntryPoint();
//    entryPoint.setPassword("123");
//    conferenceData.setEntryPoints(Collections.singletonList(entryPoint));
    CreateConferenceRequest conferenceRequest = new CreateConferenceRequest();
    conferenceRequest.setRequestId(UUID.randomUUID().toString());
    ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey();
    conferenceSolutionKey.setType(CONFERENCE_TYPE);
    conferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);
    conferenceData.setCreateRequest(conferenceRequest);
    return conferenceData;
  }
}
