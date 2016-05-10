package security.library.web.servlet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private String securityWebAppUrl = "security web app url";

    @Mock private FilterConfig filterConfig;
    @Mock private RemoteSecurityService remoteSecurityService;
    @Mock private FilterChain filterChain;

    @InjectMocks private SecurityFilter testInstance;

    @Test
    public void testInit() throws Exception
    {
        when(filterConfig.getInitParameter(SecurityFilter.VALIDATE_URL_PROPERTY)).thenReturn(validationUrl);
        when(filterConfig.getInitParameter(SecurityFilter.LOGGING_APPLICATION_PROPERTY)).thenReturn(securityWebAppUrl);

        testInstance.init(filterConfig);

        assertThat(testInstance.getLoggingApplicationUrl(), is(securityWebAppUrl));
        assertThat(testInstance.getValidationUrl(), is(validationUrl));
    }

    @Test
    public void testDoFilterWhenValidToken() throws Exception
    {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        Cookie cookie = mock(Cookie.class);
        SecurityUserDTO securityUser = new SecurityUserDTO();
        String cookieValue = "token";

        when(cookie.getName()).thenReturn(SecurityFilter.SECURITY_COOKIE);
        when(cookie.getValue()).thenReturn(cookieValue);
        when(servletRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        when(remoteSecurityService.getSecurityUser(cookieValue)).thenReturn(Optional.of(securityUser));

        testInstance.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletRequest).setAttribute(SecurityFilter.SECURITY_REQUEST_ATTRIBUTE, securityUser);
        verifyZeroInteractions(servletResponse);
    }

    @Test
    public void testDoFilterWhenNoValidToken() throws Exception
    {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        SecurityUserDTO securityUser = new SecurityUserDTO();

        String requestUrl = "request url";
        StringBuffer buffer = new StringBuffer(requestUrl);

        when(servletRequest.getRequestURL()).thenReturn(buffer);
        when(servletRequest.getCookies()).thenReturn(new Cookie[0]);
        testInstance = spy(testInstance);
        doReturn(securityWebAppUrl).when(testInstance).getLoggingApplicationUrl();

        testInstance.doFilter(servletRequest, servletResponse, filterChain);

        verify(servletResponse).sendRedirect(String.format("%s?redirectUrl=%s", securityWebAppUrl, requestUrl));
    }

}