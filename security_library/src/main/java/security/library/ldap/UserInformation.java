package security.library.ldap;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent user information in the active directory for an application. Note that roles represent specific application
 * roles.
 *
 * @author Juanr Rada
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class UserInformation
{
    private String name;
    private String username;
    private String email;

    private Set<String> roles;
}
