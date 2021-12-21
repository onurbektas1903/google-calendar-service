package tr.com.obss.googlecalendarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class CalendarEventDTO implements Serializable {
    private static final long serialVersionUID = -8935093558051855158L;
    private long start;
    private long end;
    private String title;
    private String description;
    private Set<String> eventAttendees;
    private String meetingUrl;
    private boolean createMeeting = false;
    private String summary;
    private String creator;
    private String accountId;
    private String eventId;
    private GoogleSettingsDTO googleSettings;
}
