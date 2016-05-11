package security.rest.services.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import security.library.ldap.UserInformation;
import security.rest.services.security.ldap.LdapUtil;
import security.rest.services.security.token.TokenService;

/**
 * Test suite for {@link SecurityService}
 *
 * @author Juan Rada
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceTest
{
    private String user = "user";
    private String password = "password";
    private String token = "token";
    private String application = "application";

    @Mock private LdapUtil ldapUtil;
    @Mock private TokenService tokenService;

    @InjectMocks
    private SecurityService testInstance;

    @Test
    public void testAuthenticateUserWhenInvalidCredentials() throws Exception
    {
        when(ldapUtil.authenticateUser(user, password)).thenReturn(false);

        Optional<String> token = testInstance.authenticateUser(user, password);
        assertThat(token.isPresent(), is(false));
    }

    @Test
    public void testAuthenticateUserWhenValidCredentials() throws Exception
    {
        when(ldapUtil.authenticateUser(user, password)).thenReturn(true);
        when(tokenService.getTokenForUser(user)).thenReturn(token);

        Optional<String> token = testInstance.authenticateUser(user, password);
        assertThat(token, is((token)));
    }

    @Test
    public void validateTokenWhenNoRegister() throws Exception
    {
        when(tokenService.getSecurityUserFromToken(token)).thenReturn(Optional.empty());
        assertThat(testInstance.validateToken(token, application), is(Optional.empty()));
    }

    @Test
    public void validateTokenWhenRegister() throws Exception
    {
        UserInformation userInformation = new UserInformation();
        when(tokenService.getSecurityUserFromToken(token)).thenReturn(Optional.of(user));
        when(ldapUtil.getUserInformation(user, application)).thenReturn(Optional.of(userInformation));

        assertThat(testInstance.validateToken(token, application), is(Optional.of(userInformation)));
    }

    @Test
    public void invalidateToken() throws Exception
    {
        testInstance.invalidateToken(token);
        verify(tokenService).invalidateAll(token);
    }

}