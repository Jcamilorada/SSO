package security.rest.rest.resources;

import com.google.common.base.Preconditions;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import security.library.web.dto.LoginRequestDTO;
import security.library.web.dto.LoginResponseDTO;
import security.library.web.dto.LogoutRequestDTO;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.dto.TokenValidationRequestDTO;
import security.rest.rest.common.exception.InvalidCredentialsException;
import security.rest.rest.mappers.SecurityUserMapper;
import security.rest.services.security.SecurityService;
import security.rest.services.security.ldap.UserInformation;


/**
 * Provided rest security services for token generation and user information retrieval
 *
 * @author Juan Rada
 *
 */
@RestController
@RequestMapping("/rest")
class SecurityResource
{
    private static final String INVALID_CREDENTIALS_ERROR = "Invalid username or password";
    private static final String INVALID_TOKEN_ERROR = "Invalid security token";

    private final SecurityService securityService;
    private final SecurityUserMapper securityUserMapper;

    @Autowired
    public SecurityResource(SecurityService securityService, SecurityUserMapper securityUserMapper)
    {
        this.securityService = Preconditions.checkNotNull(securityService);
        this.securityUserMapper = Preconditions.checkNotNull(securityUserMapper);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    LoginResponseDTO login(@RequestBody LoginRequestDTO requestDTO)
    {
        Optional<String> token = securityService.authenticateUser(requestDTO.getUser(), requestDTO.getPassword());

        if (token.isPresent())
        {
            return new LoginResponseDTO(token.get(), true);
        }

        throw new InvalidCredentialsException(INVALID_CREDENTIALS_ERROR);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(@RequestBody LogoutRequestDTO requestDTO)
    {
        if (!securityService.invalidateToken(requestDTO.getToken()))
        {
            throw new InvalidCredentialsException(INVALID_TOKEN_ERROR);
        }
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public @ResponseBody
    SecurityUserDTO getUserInformation(@RequestBody TokenValidationRequestDTO requestDTO)
    {
        Optional<UserInformation> userInformation =
                securityService.validateToken(requestDTO.getToken(), requestDTO.getApplication());

        if (userInformation.isPresent())
        {
            SecurityUserDTO securityUserDTO =
                securityUserMapper.getSecurityUserDTO(userInformation.get());

            return securityUserDTO;
        }

        throw new InvalidCredentialsException(INVALID_CREDENTIALS_ERROR);
    }
}
