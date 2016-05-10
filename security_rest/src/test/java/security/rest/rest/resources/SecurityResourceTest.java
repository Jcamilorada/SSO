package security.rest.rest.resources;

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
import security.library.web.dto.LoginRequestDTO;
import security.library.web.dto.LogoutRequestDTO;
import security.library.web.dto.TokenValidationRequestDTO;
import security.library.web.dto.LoginResponseDTO;
import security.rest.rest.common.exception.InvalidCredentialsException;
import security.rest.services.security.SecurityService;


/**
 * Test suite for {@link SecurityResource}
 *
 *@author Juan Rada
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityResourceTest
{
    private String user = "user";
    private String password = "password";
    private String token = "token";

    @Mock private SecurityService securityService;

    @InjectMocks private SecurityResource testInstance;

    @Test
    public void testLoginWhenValidCredentials() throws Exception
    {
        when(securityService.authenticateUser(user, password)).thenReturn(Optional.of(token));

        LoginResponseDTO loginResponseDTO = testInstance.login(new LoginRequestDTO(user, password));
        assertThat(loginResponseDTO.getToken(), is(token));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLoginWhenInValidCredentials() throws Exception
    {
        when(securityService.authenticateUser(user, password)).thenReturn(Optional.empty());
        testInstance.login(new LoginRequestDTO(user, password));
    }

    @Test
    public void testLogout() throws Exception
    {
        LogoutRequestDTO loginRequestDTO = new LogoutRequestDTO(token);

        testInstance.logout(loginRequestDTO);
        verify(securityService).invalidateToken(token);
    }

    @Test
    public void testGetUserInformationWhenValid() throws Exception
    {
        TokenValidationRequestDTO tokenValidationRequestDTO = new TokenValidationRequestDTO(token);
        UserInformation userInformation = new UserInformation();

        when(securityService.validateToken(token)).thenReturn(Optional.of(userInformation));
        UserInformation result = testInstance.getUserInformation(tokenValidationRequestDTO);

        assertThat(result, is(userInformation));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testHetUserInformationWhenInValid() throws Exception
    {
        TokenValidationRequestDTO tokenValidationRequestDTO = new TokenValidationRequestDTO(token);

        when(securityService.validateToken(token)).thenReturn(Optional.empty());
        testInstance.getUserInformation(tokenValidationRequestDTO);
    }
}