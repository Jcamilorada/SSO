package security.library.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validation request response data transfer object.
 *
 * @author Juan Rada
 */
@Data @NoArgsConstructor
public class LogoutResponseDTO
{
    private boolean isValid;
    private String errorMessage;

    public LogoutResponseDTO(boolean isValid)
    {
        this.isValid = isValid;
    }
}
