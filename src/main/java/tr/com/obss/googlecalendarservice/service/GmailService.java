package tr.com.obss.googlecalendarservice.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.dto.GoogleMailDTO;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailService {

  private final GoogleAuthService authService;
  private final GoogleAccountService accountService;

  public boolean sendMessage(GoogleMailDTO mailDTO) throws MessagingException, IOException {
    Message message =
        createMessageWithEmail(
            createEmail(
                mailDTO.getRecipientMail(),
                mailDTO.getSenderMail(),
                mailDTO.getSubject(),
                mailDTO.getBody(),
                mailDTO.getMeetingLink()));
    Gmail gmail = authService.buildGmailClient(accountService.getGoogleAccount(),mailDTO.getSenderMail());
     gmail.users().messages().send(mailDTO.getSenderMail(), message).setUserId(mailDTO.getSenderMail()).execute();
    return true;
  }

  private MimeMessage createEmail(String to, String from, String subject, String bodyText,String link)
      throws MessagingException {
    MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
    email.setFrom(new InternetAddress(from));
    email.addRecipient(
        javax.mail.Message.RecipientType.TO, new InternetAddress(to));
    email.setSubject(subject);
    String htmlBody =
        "<p>" + bodyText + "</p> <p> <a href="+link+">meetingLink</a></p>";
    email.setText(htmlBody, "UTF-8", "html");
    email.setSender(new InternetAddress(from));
    return email;
  }

  private Message createMessageWithEmail(MimeMessage emailContent)
      throws MessagingException, IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    emailContent.writeTo(buffer);
    return new Message().setRaw(StringUtils.newStringUsAscii(encodeBase64(buffer.toByteArray(),false,true)));
//    return new Message().setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
  }
}
