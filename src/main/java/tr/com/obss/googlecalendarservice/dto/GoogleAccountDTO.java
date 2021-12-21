package tr.com.obss.googlecalendarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GoogleAccountDTO  implements Serializable {
    private static final long serialVersionUID = 6774219758102322947L;
    private String id;
    @NotEmpty(message="account mail can't be null")
    protected String accountMail;
    @NotEmpty(message="applicationName mail can't be null")
    private String applicationName;
    private File file;
}
