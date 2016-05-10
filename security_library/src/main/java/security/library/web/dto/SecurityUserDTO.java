package security.library.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by JUANC on 5/8/16.
 */
@Data @NoArgsConstructor
public class SecurityUserDTO
{
    private String name;
    private String username;
    private String email;
}
