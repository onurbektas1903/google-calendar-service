package tr.com.obss.googlecalendarservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import tr.com.common.audit.AuditableEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Where(clause = "deleted = false")
public class GoogleAccount extends AuditableEntity {


    private String accountMail;

    private String applicationName;

    private String fileName;

    private String contentType;

    private Long size;

    @Lob
    private byte[] data;

    private boolean deleted;

}