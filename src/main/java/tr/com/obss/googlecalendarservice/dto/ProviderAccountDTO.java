package tr.com.obss.googlecalendarservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProviderAccountDTO implements Serializable {
    private static final long serialVersionUID = -542895211665937754L;
    private String id;
    @NotEmpty(message="account mail can't be null")
    protected String accountMail;
    @NotEmpty(message="applicationName mail can't be null")
    private String applicationName;
}
