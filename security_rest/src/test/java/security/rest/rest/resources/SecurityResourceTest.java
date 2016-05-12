package security.rest.rest.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
    private String application = "application";

    @Mock private SecurityService securityService;
    @Mock private SecurityUserMapper securityUserMapper;

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
    public void testLogoutWhenIsValid() throws Exception
    {
        LogoutRequestDTO loginRequestDTO = new LogoutRequestDTO(token);
        when(securityService.invalidateToken(token)).thenReturn(true);

        testInstance.logout(loginRequestDTO);
        verify(securityService).invalidateToken(token);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testLogoutWhenIsInvalid() throws Exception
    {
        LogoutRequestDTO loginRequestDTO = new LogoutRequestDTO(token);
        when(securityService.invalidateToken(token)).thenReturn(false);

        testInstance.logout(loginRequestDTO);
        verify(securityService).invalidateToken(token);
    }

    @Test
    public void testGetUserInformationWhenValid() throws Exception
    {
        TokenValidationRequestDTO tokenValidationRequestDTO = new TokenValidationRequestDTO(token, application);
        UserInformation userInformation = new UserInformation();
        SecurityUserDTO securityUserDTO = new SecurityUserDTO();

        when(securityService.validateToken(token, application)).thenReturn(Optional.of(userInformation));
        when(securityUserMapper.getSecurityUserDTO(userInformation)).thenReturn(securityUserDTO);
        SecurityUserDTO result = testInstance.getUserInformation(tokenValidationRequestDTO);

        assertThat(result, is(securityUserDTO));
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testGetUserInformationWhenInValid() throws Exception
    {
        TokenValidationRequestDTO tokenValidationRequestDTO = new TokenValidationRequestDTO(token, application);

        when(securityService.validateToken(token, application)).thenReturn(Optional.empty());
        testInstance.getUserInformation(tokenValidationRequestDTO);
    }
}
