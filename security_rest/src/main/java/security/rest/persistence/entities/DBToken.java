package security.rest.persistence.entities;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import security.rest.persistence.common.BooleanConverter;
import security.rest.persistence.common.LocalDateTimeAttributeConverter;
import lombok.Data;

/**
 * Token database representation class.
 *
 * @author Juan Rada
 */
@Entity
@Table(name = "TOKENS")
@Data
public class DBToken
{
    @Id
    @GeneratedValue()
    @Column(name = "TOKEN_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "CREATION_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdDate;

    @Column(name = "LAST_USED")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime lastUsed;

    @Column(name = "USER")
    private String user;

    @Column(name = "VALID")
    @Convert(converter = BooleanConverter.class)
    private boolean valid;
}
