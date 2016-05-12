package security.library.web.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import security.library.web.dto.LoginRequestDTO;
import security.library.web.dto.LoginResponseDTO;
import security.library.web.dto.LogoutRequestDTO;
import security.library.web.dto.SecurityUserDTO;

/**
 * Test suite for {@link RemoteSecurityService}
 *
 * @author Juan Rada
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoteSecurityServiceTest
{
    private static final String SERVER_URL = "SERVER_URL";
    private String token = "token";
    private String application = "application";

    @Mock
    RestTemplate restTemplate;

    private RemoteSecurityService testInstance;

    @Before
    public void setup()
    {
        testInstance = new RemoteSecurityService(SERVER_URL, restTemplate);
    }

    @Test
    public void testGetSecurityUser() throws Exception
    {
        SecurityUserDTO securityUserDTO = new SecurityUserDTO();

        when(restTemplate.postForObject(
            eq(SERVER_URL + "/validate"),
            any(LoginResponseDTO.class),
            eq(SecurityUserDTO.class))).thenReturn(securityUserDTO);

        Optional<SecurityUserDTO> result = testInstance.getSecurityUser(token, application);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(securityUserDTO));
    }

    @Test
    public void testGetSecurityUserWhenError() throws Exception
    {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);

        doThrow(exception).when(restTemplate).postForObject(
            eq(SERVER_URL + "/validate"),
            any(LoginResponseDTO.class),
            eq(SecurityUserDTO.class));

        Optional<SecurityUserDTO> result = testInstance.getSecurityUser(token, application);
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testLogIn() throws Exception
    {
        String user = "user";
        String password = "password";

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        when(restTemplate.postForObject(
            eq(SERVER_URL + "/login"),
            any(LoginRequestDTO.class),
            eq(LoginResponseDTO.class))).thenReturn(loginResponseDTO);

        ArgumentCaptor<LoginRequestDTO> captor =
            ArgumentCaptor.forClass(LoginRequestDTO.class);

        assertThat(testInstance.login(user, password), is(loginResponseDTO));
        verify(restTemplate).postForObject(
            eq(SERVER_URL + "/login"), captor.capture(), eq(LoginResponseDTO.class));
        LoginRequestDTO requestDTO = captor.getValue();

        assertThat(requestDTO.getUser(), is(user));
        assertThat(requestDTO.getPassword(), is(password));
    }

    @Test
    public void testLogInWhenError() throws Exception
    {
        String user = "user";
        String password = "password";
        String errorMessage = "error message";

        HttpClientErrorException exception =
            new HttpClientErrorException(HttpStatus.UNAUTHORIZED, errorMessage);

        doThrow(exception).when(restTemplate).postForObject(
            eq(SERVER_URL + "/login"),
            any(LoginRequestDTO.class),
            eq(LoginResponseDTO.class));

        ArgumentCaptor<LoginRequestDTO> captor = ArgumentCaptor.forClass(LoginRequestDTO.class);

        LoginResponseDTO loginResponseDTO = testInstance.login(user, password);
        assertThat(loginResponseDTO.isValid(), is(false));
        assertThat(loginResponseDTO.getErrorMessage(), is("401 error message"));

        verify(restTemplate).postForObject(
            eq(SERVER_URL + "/login"), captor.capture(), eq(LoginResponseDTO.class));
        LoginRequestDTO requestDTO = captor.getValue();

        assertThat(requestDTO.getUser(), is(user));
        assertThat(requestDTO.getPassword(), is(password));
    }

    @Test
    public void testLogout() throws Exception
    {
        testInstance.logout(token);

        verify(restTemplate).postForLocation(eq(SERVER_URL + "/logout"), any(LogoutRequestDTO.class));
    }

}
