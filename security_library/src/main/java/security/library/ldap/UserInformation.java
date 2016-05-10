package security.library.ldap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent user information in the active directory for an applicaiton.
 *
 * @author Juanr Rada
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class UserInformation
{
    private String name;
    private String username;
    private String email;
}
