package tr.com.obss.googlecalendarservice.mapper;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Value;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.googlecalendarservice.dto.CalendarEventDTO;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.dto.GoogleSettingsDTO;
import tr.com.obss.googlecalendarservice.entity.GoogleAccount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Slf4j
public class GoogleMapperDecorator implements GoogleMapper {
  private GoogleMapper delegate;
  @Value("${google-calendar.default-time-zone}")
  private String timeZone;

  @Autowired
  @Qualifier("delegate")
  public void setDelegate(GoogleMapper delegate) {
    this.delegate = delegate;
  }

  @Override
  public GoogleAccountDTO toGoogleAccountDTO(GoogleAccount googleAccount) {
    return delegate.toGoogleAccountDTO(googleAccount);
  }

  @Override
  public GoogleAccount toEntity(GoogleAccountDTO googleAccountDTO) {
    GoogleAccount googleAccount = delegate.toEntity(googleAccountDTO);
    if (googleAccount.getId() == null) {
      googleAccount.setId(UUID.randomUUID().toString());
    }
    return googleAccount;
  }

  @Override
  public Event toEvent(CalendarEventDTO calendarEvent) {
    Event event = delegate.toEvent(calendarEvent);
    if(calendarEvent.isCreateMeeting()){
      setConferenceData(event,calendarEvent.getGoogleSettings());
    }
    Event.Creator creator = new Event.Creator();
    creator.setEmail(calendarEvent.getCreator());
    event.setCreator(creator);
    setAttendees(calendarEvent, event);
    convertAndSetDates(calendarEvent,event);
    if(calendarEvent.getMeetingUrl() != null && !calendarEvent.getMeetingUrl().isEmpty()) {
      event.setHangoutLink(calendarEvent.getMeetingUrl());
    }
    return event;
  }
  private void setAttendees(CalendarEventDTO calendarEvent, Event event) {
    List<EventAttendee> attendees =
            calendarEvent.getEventAttendees().stream()
                    .map(attendee -> new EventAttendee().setEmail(attendee))
                    .collect(Collectors.toList());
    event.setAttendees(attendees);
  }

  private void setConferenceData(Event event, GoogleSettingsDTO googleSettings) {
    ConferenceData conferenceData = new ConferenceData();
    if(googleSettings != null){
    event.setGuestsCanInviteOthers(googleSettings.isGuestsCanInviteOthers());
    event.setGuestsCanModify(googleSettings.isGuestsCanModify());
    }
    CreateConferenceRequest conferenceRequest = new CreateConferenceRequest();
    conferenceRequest.setRequestId(UUID.randomUUID().toString());
    ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey();
    conferenceSolutionKey.setType("hangoutsMeet");
    conferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);
    conferenceData.setCreateRequest(conferenceRequest);
    event.setConferenceData(conferenceData);
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
  @Override
  public List<GoogleAccountDTO> toDTOList(List<GoogleAccount> accounts) {
    return delegate.toDTOList(accounts);
  }

  @Override
  public void updateGoogleAccount(GoogleAccountDTO googleAccountDTO, GoogleAccount entity) {
      delegate.updateGoogleAccount(googleAccountDTO, entity);
  }

  public GoogleAccount toEntityWithFile(GoogleAccountDTO googleAccountDTO, MultipartFile file) {
    GoogleAccount googleAccount = delegate.toEntity(googleAccountDTO);
    if(googleAccount.getId() == null){
      googleAccount.setId(UUID.randomUUID().toString());
    }
    googleAccount.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
    googleAccount.setContentType(file.getContentType());
    googleAccount.setSize(file.getSize());
    try {
      googleAccount.setData(file.getBytes());
    } catch (IOException e) {
      log.error(e.getLocalizedMessage());
      throw new RuntimeException("Failed to read file");
    }
    return googleAccount;
  }

  public GoogleAccountDTO toGoogleAccountDTOWithFile(GoogleAccount googleAccount) {
    GoogleAccountDTO googleAccountDTO = delegate.toGoogleAccountDTO(googleAccount);
    File file = new File(googleAccount.getFileName());
    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(googleAccount.getData());
    } catch (FileNotFoundException e) {
      log.error(e.getLocalizedMessage(), e);
    } catch (IOException e) {
      e.printStackTrace();
    }
    googleAccountDTO.setFile(file);
    return googleAccountDTO;
  }
  public void updateGoogleAccountWithFile(GoogleAccountDTO googleAccountDTO,GoogleAccount googleAccount,
                                          MultipartFile file){
      delegate.updateGoogleAccount(googleAccountDTO,googleAccount);
      googleAccount.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
      googleAccount.setContentType(file.getContentType());
      googleAccount.setSize(file.getSize());
  }
}
