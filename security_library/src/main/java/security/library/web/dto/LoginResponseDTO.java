package security.library.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Validation request data transfer object.
 *
 * @author Juan Rada
 */
@Data @NoArgsConstructor
public class LoginResponseDTO
{
    private String token;
    private boolean isValid;
    private String errorMessage;

    public LoginResponseDTO(String token, boolean isValid)
    {
        this.token = token;
        this.isValid = isValid;
    }
}
