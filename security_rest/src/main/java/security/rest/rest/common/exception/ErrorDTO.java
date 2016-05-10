package security.rest.rest.common.exception;

import lombok.Data;

/**
 * Error data transfer object class to retrieve errors.
 *
 * @author Juan Rada
 */
@Data
public class ErrorDTO
{
    private String message;
}
