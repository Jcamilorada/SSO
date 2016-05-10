package security.library.web.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import security.library.web.dto.LoginRequestDTO;
import security.library.web.dto.LogoutRequestDTO;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.dto.LoginResponseDTO;

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

    @Mock
    RestTemplate restTemplate;

    private RemoteSecurityService testInstance;

    @Before
    public void setup()
    {
        testInstance = new RemoteSecurityService(SERVER_URL, restTemplate);
    }

    @Test
    public void getSecurityUser() throws Exception
    {
        String token = "token";
        SecurityUserDTO securityUserDTO = new SecurityUserDTO();

        when(restTemplate.postForObject(eq(SERVER_URL + "/validate"),
                any(LoginResponseDTO.class), eq(SecurityUserDTO.class))).thenReturn(securityUserDTO);

        Optional<SecurityUserDTO> result = testInstance.getSecurityUser(token);
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(securityUserDTO));

    }

    @Test
    public void logIn() throws Exception
    {
        String user = "user";
        String password = "password";
        String token = "token";

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        when(restTemplate.postForObject(eq(SERVER_URL + "/login"),
                any(LoginRequestDTO.class), eq(LoginResponseDTO.class))).thenReturn(loginResponseDTO);

        assertThat(testInstance.login(user, password), is(loginResponseDTO));
    }

    @Test
    public void logout() throws Exception
    {
        String token = "token";

        testInstance.logout(token);
        verify(restTemplate).postForLocation(eq(SERVER_URL + "/logout"), any(LogoutRequestDTO.class));
    }

}