package tr.com.obss.googlecalendarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAccountDetails implements Serializable {
    private static final long serialVersionUID = 4161749320600189647L;
    private String adminUserEmail;
    @NotEmpty(message = "Credentials file cant be empty")
    private String credentialsFileName;
}