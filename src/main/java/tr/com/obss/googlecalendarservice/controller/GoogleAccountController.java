package tr.com.obss.googlecalendarservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.service.GoogleAccountService;
import tr.com.obss.googlecalendarservice.service.GoogleAuthService;
import tr.com.obss.googlecalendarservice.util.FileUtil;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google-account-manager")
public class GoogleAccountController {
  private final GoogleAccountService googleAccountService;
  private final GoogleAuthService authService;

  @ResponseBody
  @RequestMapping(
          value = "/account",
          method = RequestMethod.POST,
          consumes = {"multipart/form-data"})
  public GoogleAccountDTO createGoogleAccount(
          @Valid @RequestPart("googleAccountDTO") GoogleAccountDTO googleAccount,
          @RequestPart("file") MultipartFile file) {

    googleAccount.setFile( FileUtil.convertToFile(file));
    authService.validateClient(googleAccount);
     return googleAccountService.save(googleAccount,file);
  }

  @PutMapping("/account/{id}")
  @ResponseBody
  public GoogleAccountDTO updateGoogleAccount( @PathVariable String id,
          @Valid @RequestPart("googleAccountDTO") GoogleAccountDTO googleAccount,
          @RequestPart("file") MultipartFile file) {
//    authService.validateClient(accountName);
    return googleAccountService.update(id,googleAccount,file);
  }
  @GetMapping("/accounts")
  @ResponseBody
  @ResponseStatus(OK)
  public List<GoogleAccountDTO> listGoogleAccounts(){
    return googleAccountService.getAll();
  }

  @GetMapping("/account/{id}")
  @ResponseBody
  @ResponseStatus(OK)
  GoogleAccountDTO findGoogleAccountById(@PathVariable  String id){
    return googleAccountService.findById(id);
  }

  @DeleteMapping("/account/{accountName}")
  @ResponseBody
  @ResponseStatus(OK)
  void deleteAccount(@PathVariable  String accountName){
    googleAccountService.deleteAccount(accountName);
  }
}
