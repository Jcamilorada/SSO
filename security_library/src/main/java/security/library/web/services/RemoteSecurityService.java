package security.library.web.services;

import com.google.common.base.Preconditions;
import java.util.Optional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import security.library.web.dto.LoginRequestDTO;
import security.library.web.dto.LogoutRequestDTO;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.dto.TokenValidationRequestDTO;
import security.library.web.dto.LoginResponseDTO;

/**
 * Remote security service. Provided method to perform remote security operations.
 *
 * @author Juan Rada
 *
 */
public class RemoteSecurityService
{
    private final String securityServerUrl;
    private final RestTemplate restTemplate;

    public RemoteSecurityService(String securityServerUrl)
    {
        this(securityServerUrl, new RestTemplate());
    }

    RemoteSecurityService(String securityServerUrl, RestTemplate restTemplate)
    {
        this.securityServerUrl = securityServerUrl;
        this.restTemplate = restTemplate;
    }

    public Optional<SecurityUserDTO> getSecurityUser(String token)
    {
        Preconditions.checkNotNull(token);

        return Optional.of(restTemplate.postForObject(
                securityServerUrl + "/validate", new TokenValidationRequestDTO(token), SecurityUserDTO.class));
    }

    public LoginResponseDTO login(String username, String password)
    {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(password);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        try
        {
            loginResponseDTO = restTemplate.postForObject(
                    securityServerUrl + "/login", new LoginRequestDTO(username, password), LoginResponseDTO.class);
            loginResponseDTO.setValid(true);
        }

        catch (HttpClientErrorException ex)
        {
            loginResponseDTO.setValid(false);
            loginResponseDTO.setErrorMessage(ex.getMessage());
        }

        return loginResponseDTO;
    }

    public void logout(String token)
    {
        Preconditions.checkNotNull(token);

        restTemplate.postForLocation(securityServerUrl + "/logout", new LogoutRequestDTO(token));
    }
}
