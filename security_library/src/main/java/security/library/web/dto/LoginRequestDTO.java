package security.library.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login request data transfer object.
 *
 * @author Juan Rada
 *
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class LoginRequestDTO
{
    private String user;
    private String password;
}
