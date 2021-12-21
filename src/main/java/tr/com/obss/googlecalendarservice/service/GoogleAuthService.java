package tr.com.obss.googlecalendarservice.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tr.com.common.exceptions.NotFoundException;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.exception.InvalidCredentialsException;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Set;

import static tr.com.obss.googlecalendarservice.constants.GoogleErrorCodes.BACK_OFF_ERROR_CODES;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {

  private final GoogleAccountService googleAccountService;

  private final JsonFactory JSON_FACTORY =  GsonFactory.getDefaultInstance();

  public Calendar buildCalendarClient(GoogleAccountDTO account, String creator) {
    try {
      NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      Credential credential =
          createCredentials(account,creator ,httpTransport, Collections.singleton(CalendarScopes.CALENDAR));
    return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
          .setApplicationName(account.getApplicationName())
          .build();
    } catch (IOException | GeneralSecurityException e) {
      log.error(e.getLocalizedMessage(), e);
      throw new InvalidCredentialsException("Invalid Google Credentials",
              Collections.singleton(account.getAccountMail()));
    }
  }

  public Gmail buildGmailClient(GoogleAccountDTO account,String creator) {
    try {
      NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      Credential credential =
          createCredentials(account, creator, httpTransport, Collections.singleton(GmailScopes.MAIL_GOOGLE_COM));
                    return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                            .setApplicationName(account.getApplicationName())
                            .build();

    } catch (IOException | GeneralSecurityException e) {
      log.error(e.getLocalizedMessage(), e);
      throw new InvalidCredentialsException("Invalid Google Credentials",
              Collections.singleton(account.getAccountMail()));    }
  }
  public void validateClient(GoogleAccountDTO googleAccountDTO){
    try {
      NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      Credential credentials = createCredentials(googleAccountDTO, googleAccountDTO.getAccountMail(), httpTransport,
              Collections.singleton(GmailScopes.MAIL_GOOGLE_COM));
      log.info("Account validated",credentials);

    } catch (GeneralSecurityException | IOException e) {
      log.error(e.getLocalizedMessage(),e);
      throw new InvalidCredentialsException("Invalid Google Credentials",
              Collections.singleton(googleAccountDTO.getAccountMail()));
    }
  }

  public HttpBackOffUnsuccessfulResponseHandler getErrorHandler() {
    HttpBackOffUnsuccessfulResponseHandler errorCodeHandler =
        new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff());
    errorCodeHandler.setBackOffRequired(
        response -> BACK_OFF_ERROR_CODES.contains(response.getStatusCode()));
    return errorCodeHandler;
  }

  public Credential createCredentials(
          GoogleAccountDTO account, String creator, NetHttpTransport httpTransport, Set<String> scopes)

      throws GeneralSecurityException, IOException {
    Credential credentials =
        new GoogleCredential.Builder()
            .setTransport(httpTransport)
            .setJsonFactory(JSON_FACTORY)
            .setServiceAccountId(account.getAccountMail())
            .setServiceAccountUser(creator)
            .setServiceAccountScopes(scopes)
            .setServiceAccountPrivateKeyFromP12File(account.getFile())
            .build();
    credentials.refreshToken();
    return credentials;
  }
}
