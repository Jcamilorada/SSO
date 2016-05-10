package security.library.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token validation request dto.
 *
 * @author Juan Rada
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class TokenValidationRequestDTO
{
    private String token;
}
