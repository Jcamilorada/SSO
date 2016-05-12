package security.library.web.servlet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.services.RemoteSecurityService;

/**
 * Test suite for {@link SecurityFilter}
 *
 * @author Juan Rada
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityFilterTest
{
    private String validationUrl = "validation end point url";
    private String loggingApplicationUrl = "security web app url";
    private String application = "application";

    @Mock private FilterConfig filterConfig;
    @Mock private RemoteSecurityService remoteSecurityService;
    @Mock private FilterChain filterChain;
    @Mock private HttpServletRequest servletRequest;
    @Mock private HttpServletResponse servletResponse;

    @InjectMocks private SecurityFilter testInstance;

    @Before
    public void setup()
    {
        when(servletRequest.getRequestURI()).thenReturn("url");
    }

    @Test
    public void testInit() throws Exception
    {
        when(filterConfig.getInitParameter(SecurityFilter.VALIDATE_URL_PROPERTY))
            .thenReturn(validationUrl);
        when(filterConfig.getInitParameter(SecurityFilter.LOGGING_APPLICATION_PROPERTY))
            .thenReturn(loggingApplicationUrl);
        when(filterConfig.getInitParameter(SecurityFilter.APPLICATION_PROPERTY))
            .thenReturn(application);

        testInstance.init(filterConfig);
        assertThat(testInstance.getLoggingApplicationUrl(), is(loggingApplicationUrl));
        assertThat(testInstance.getValidationUrl(), is(validationUrl));
        assertThat(testInstance.getApplication(), is(application));
    }

    @Test
    public void testDoFilterWhenValidToken() throws Exception
    {
        String cookieValue = "token";
        Cookie cookie = new Cookie(SecurityFilter.SECURITY_COOKIE, cookieValue);

        when(servletRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        SecurityUserDTO securityUser = new SecurityUserDTO();
        when(remoteSecurityService.getSecurityUser(cookieValue, application))
            .thenReturn(Optional.of(securityUser));

        testInstance = spy(testInstance);
        doReturn(application).when(testInstance).getApplication();
        testInstance.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).setAttribute(SecurityFilter.SECURITY_REQUEST_ATTRIBUTE, securityUser);
        verifyZeroInteractions(servletResponse);
        verify(filterChain).doFilter(servletRequest, servletResponse);
    }

    @Test
    public void testDoFilterWhenNoValidToken() throws Exception
    {
        String requestUrl = "request url";

        when(servletRequest.getRequestURL()).thenReturn(new StringBuffer(requestUrl));
        when(servletRequest.getCookies()).thenReturn(new Cookie[0]);

        testInstance = spy(testInstance);
        doReturn(loggingApplicationUrl).when(testInstance).getLoggingApplicationUrl();
        testInstance.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletResponse).sendRedirect(String.format("%s?redirectUrl=%s", loggingApplicationUrl, requestUrl));
        verifyZeroInteractions(filterChain);
    }
}
