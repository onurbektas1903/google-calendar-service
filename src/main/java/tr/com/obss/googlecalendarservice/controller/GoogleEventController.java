package tr.com.obss.googlecalendarservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.googlecalendarservice.dto.DeleteEventDTO;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.dto.GoogleMailDTO;
import tr.com.obss.googlecalendarservice.service.GmailService;
import tr.com.obss.googlecalendarservice.dto.CalendarEventDTO;
import tr.com.obss.googlecalendarservice.service.GoogleAccountService;
import tr.com.obss.googlecalendarservice.service.CalendarService;
import tr.com.obss.googlecalendarservice.service.GoogleAuthService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google-event-manager")
public class GoogleEventController {
  @Autowired private CalendarService calendarService;
  @Autowired private GmailService gmailService;

  @PostMapping("/event")
  @ResponseBody
  @ResponseStatus(OK)
  public CalendarEventDTO scheduleMeeting(@RequestBody CalendarEventDTO calendarEvent) {
    return calendarService.scheduleEvent(calendarEvent);
  }

  @PostMapping("/update-event")
  @ResponseBody
  @ResponseStatus(OK)
  public CalendarEventDTO updateMeeting(@RequestBody CalendarEventDTO calendarEvent) {
    return calendarService.updateEvent(calendarEvent);
  }
  @PostMapping("/delete-event}")
  @ResponseBody
  @ResponseStatus(OK)
  String deleteEvent(@RequestBody DeleteEventDTO deleteEventDTO) {
    return calendarService.deleteEvent(deleteEventDTO);
  }
  @PostMapping("/google-mail")
  @ResponseBody
  @ResponseStatus(OK)
  public void changeSlot(@RequestBody GoogleMailDTO mailDTO) throws IOException, MessagingException {
    gmailService.sendMessage(mailDTO);
  }
}
