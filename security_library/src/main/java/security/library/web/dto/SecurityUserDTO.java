package security.library.web.dto;

import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Security user data trasnfer object. Holds use logged user information.
 *
 * @author Juan Rada
 */
@Data @NoArgsConstructor
public class SecurityUserDTO
{
    private String name;
    private String username;
    private String email;

    private Set<String> roles;
}
