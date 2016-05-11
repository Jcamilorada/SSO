package security.library.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Logout request data transfer object.
 *
 * @author Juan Rada
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class LogoutRequestDTO
{
    private String token;
}
