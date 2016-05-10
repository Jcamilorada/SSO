package security.library.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by JUANC on 5/9/16.
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class LogoutRequestDTO
{
    private String token;
}
