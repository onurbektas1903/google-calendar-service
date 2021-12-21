package tr.com.obss.googlecalendarservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteEventDTO {
    private String creator;
    private String accountId;
    private String eventId;
}
