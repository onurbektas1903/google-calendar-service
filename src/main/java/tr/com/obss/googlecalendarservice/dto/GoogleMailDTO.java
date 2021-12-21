package tr.com.obss.googlecalendarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleMailDTO implements Serializable {
    private static final long serialVersionUID = -2131029533242986067L;
    private String subject;
    private String senderMail;
    private String recipientMail;
    private String body;
    private GoogleAccountDTO account;
    private String meetingLink;
}
