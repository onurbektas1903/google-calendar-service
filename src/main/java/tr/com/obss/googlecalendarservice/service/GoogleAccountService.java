package tr.com.obss.googlecalendarservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tr.com.common.exceptions.NotFoundException;
import tr.com.common.exceptions.NotUniqueException;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.entity.GoogleAccount;
import tr.com.obss.googlecalendarservice.mapper.GoogleMapperDecorator;
import tr.com.obss.googlecalendarservice.repository.AccountFileRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAccountService {
  private final AccountFileRepository repository;
  private final GoogleMapperDecorator mapper;

  @Transactional("ptm")
  public GoogleAccountDTO save(GoogleAccountDTO googleAccountDTO, MultipartFile file) {
      checkAnyGoogleAccountExists();
      GoogleAccount googleAccount = mapper.toEntityWithFile(googleAccountDTO,file);
      return mapper.toGoogleAccountDTO(repository.save(googleAccount));
  }
  @Transactional("ptm")
  public GoogleAccountDTO update(String id,GoogleAccountDTO googleAccountDTO,MultipartFile file) {
      GoogleAccount googleAccount = getGoogleAccount(id);
      mapper.updateGoogleAccountWithFile(googleAccountDTO,googleAccount,file);
      return mapper.toGoogleAccountDTO(repository.save(googleAccount));
  }
  public GoogleAccountDTO findById(String id){
      return mapper.toGoogleAccountDTOWithFile(getGoogleAccount(id));

  }
  public GoogleAccountDTO getGoogleAccount(){
      List<GoogleAccount> googleAccounts = repository.findAll();
      if(googleAccounts.isEmpty()){
          throw new NotFoundException("google account not found",
                  Collections.singleton("googleAccount"));
      }
      return mapper.toGoogleAccountDTOWithFile(googleAccounts.get(0));
  }
  private GoogleAccount getGoogleAccount(String id){
     return repository.findById(id).orElseThrow(() -> new NotFoundException("google account not found",
             Collections.singleton("googleAccount")));
  }
  private void checkAnyGoogleAccountExists(){
     if(!repository.findAll().isEmpty()){
       throw new NotUniqueException("There is only 1 google account can exist",Collections.singleton("googleAccount"));
     }
  }

  public File getAccountFileByAccountMail(String accountMail) {
    // TODO fix exception proper way
    GoogleAccount googleAccount =
        repository
            .findAccountFileByAccountMail(accountMail)
            .orElseThrow(() -> new RuntimeException("Account " + "Not Found"));
    File file = new File(googleAccount.getFileName());
    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(googleAccount.getData());
    } catch (FileNotFoundException e) {
      log.error(e.getLocalizedMessage(), e);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }
  @Transactional("ptm")
  public void deleteAccount(String id){
    repository.delete(getGoogleAccount(id));
  }

  public List<GoogleAccountDTO> getAll() {
    return  mapper.toDTOList(repository.findAll());
  }
}
