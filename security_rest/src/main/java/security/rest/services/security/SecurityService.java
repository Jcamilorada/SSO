package security.rest.services.security;

import com.google.common.base.Preconditions;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.library.ldap.UserInformation;
import security.rest.services.security.ldap.LdapUtil;
import security.rest.services.security.token.TokenService;

/**
 *  Security Service, provides usefull methods for integrated ldap and security token.
 *
 * @author Juan Rada
 */
@Service
public class SecurityService
{
    private final LdapUtil LdapUtil;
    private final TokenService tokenService;

    @Autowired
    public SecurityService(LdapUtil LdapUtil, TokenService tokenService)
    {
        this.LdapUtil = Preconditions.checkNotNull(LdapUtil);
        this.tokenService = Preconditions.checkNotNull(tokenService);
    }

    /**
     * Validate current user and password agains ldap directory. Retrieve an optional of generated token, empty if
     * password is invalid or user does not exist.
     *
     * @param user the username
     * @param password the user password.
     *
     * @return optional of generated token, empty if password is invalid or user does not exist.
     */
    public Optional<String> authenticateUser(String user, String password)
    {
        Optional<String> optionalToken = Optional.empty();

        if (LdapUtil.authenticateUser(user, password))
        {
            optionalToken = Optional.of(tokenService.getTokenForUser(user));
        }

        return optionalToken;
    }

    /**
     * Validates the given token, and retrieve the token user information if token is valid otherwise and empy optioanl.
     * Application information is used to retrieve user groups for it.
     *
     * @param token the string representation of the security token.
     * @return optional with user information. empty is token is not valid or user does not exist.
     */
    public Optional<UserInformation> validateToken(String token, String application)
    {
        Optional<String> user = tokenService.getSecurityUserFromToken(token);
        Optional<UserInformation> userInformation = Optional.empty();

        if (user.isPresent())
        {
            userInformation = LdapUtil.getUserInformation(user.get(), application);
        }

        return userInformation;
    }

    public void invalidateToken(String token)
    {
        tokenService.invalidateAll(token);
    }
}
