package security.library.web.services;

import com.google.common.base.Preconditions;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import security.library.web.dto.LoginRequestDTO;
import security.library.web.dto.LoginResponseDTO;
import security.library.web.dto.LogoutRequestDTO;
import security.library.web.dto.LogoutResponseDTO;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.dto.TokenValidationRequestDTO;

/**
 * Remote security service. Provided method to perform remote security operations.
 *
 * @author Juan Rada
 *
 */
public class RemoteSecurityService
{
    private static final Logger logger = LoggerFactory.getLogger(RemoteSecurityService.class);

    private final String securityServerUrl;
    private final RestTemplate restTemplate;

    private static final String LOGIN_PATH = "/login";
    private static final String LOGOUT_PATH = "/logout";
    private static final String VALIDATE_PATH = "/validate";

    private static final String GET_USER_INFORMATION_ERROR = "Error extracting user information from %s. %s";
    private static final String ERROR_AUTHENTICATION = "Error authenticating user %s. %s";

    public RemoteSecurityService(String securityServerUrl)
    {
        this(securityServerUrl, new RestTemplate());
    }

    RemoteSecurityService(String securityServerUrl, RestTemplate restTemplate)
    {
        this.securityServerUrl = Preconditions.checkNotNull(securityServerUrl);
        this.restTemplate = Preconditions.checkNotNull(restTemplate);
    }

    /**
     * Get the user information for the given token. Retrieves an optional instance of {@link SecurityUserDTO} if user
     * is found otherwise empty. Perform an Http request to configured security server.
     *
     * @param token the security Token.
     * @param application the application name to retrieve user roles.
     *
     * @return an optional instance of {@link SecurityUserDTO} if user is found otherwise empty.
     */
    public Optional<SecurityUserDTO> getSecurityUser(String token, String application)
    {
        Preconditions.checkNotNull(token);

        Optional<SecurityUserDTO> userDTO = Optional.empty();

        try
        {
            TokenValidationRequestDTO requestDTO = new TokenValidationRequestDTO(token, application);

            userDTO = Optional.of(
                restTemplate.postForObject(
                    securityServerUrl + VALIDATE_PATH,
                    requestDTO,
                    SecurityUserDTO.class));
        }

        catch (HttpClientErrorException ex)
        {
            logger.error(String.format(GET_USER_INFORMATION_ERROR, token, ex.getMessage()));
        }

        return userDTO;
    }

    /**
     *  Perform an Http request and authenticate user and password in security server. if authentication fail  retrieved
     *  {@link LoginResponseDTO} is flagged as invalid.
     *
     * @param username the username.
     * @param password the user password.
     *
     * @return an instance of {@link LoginResponseDTO} with the authentication result.
     */
    public LoginResponseDTO login(String username, String password)
    {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(password);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        try
        {
            LoginRequestDTO requestDTO = new LoginRequestDTO(username, password);
            loginResponseDTO = restTemplate.postForObject(
                    securityServerUrl + LOGIN_PATH, requestDTO, LoginResponseDTO.class);
            loginResponseDTO.setValid(true);
        }

        catch (HttpClientErrorException ex)
        {
            logger.error(String.format(ERROR_AUTHENTICATION, username, ex.getMessage()));

            loginResponseDTO.setValid(false);
            loginResponseDTO.setErrorMessage(ex.getMessage());
        }

        return loginResponseDTO;
    }

    /**
     * Invalidate given token, perform an http request to security server end point.
     *
     * @param token the security token to invalidate.
     *
     * @return an instance of {@link LoginResponseDTO} with logout operation result.
     */
    public LogoutResponseDTO logout(String token)
    {
        Preconditions.checkNotNull(token);

        LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO(true);

        try
        {
            restTemplate.postForLocation(securityServerUrl + LOGOUT_PATH, new LogoutRequestDTO(token));
        }

        catch (HttpClientErrorException ex)
        {
            logoutResponseDTO.setValid(false);
            logoutResponseDTO.setErrorMessage(ex.getMessage());
        }

        return logoutResponseDTO;
    }
}
