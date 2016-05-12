package security.library.web.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import security.library.web.servlet.SecurityFilter;

/**
 * Test suite for {@link HttpUtils}
 *
 * @author Juan Rada
 */
public class HttpUtilsTest
{
    @Test
    public void testGetCookieWhenCookieExist() throws Exception
    {
        String cookieName = "cookie";
        String cookieValue = "cookie value";

        Cookie cookie = new Cookie(cookieName, cookieValue);
        Cookie[] cookies = new Cookie[]{cookie};

        Optional<Cookie> result = HttpUtils.getCookie(cookies, cookieName);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(cookie));
    }

    @Test
    public void testGetCookieWhenCookieDoesNotExist() throws Exception
    {
        String cookieName = "cookie";
        String cookieValue = "cookie value";

        Cookie cookie = new Cookie(cookieName, cookieValue);
        Cookie[] cookies = new Cookie[]{cookie};

        Optional<Cookie> result = HttpUtils.getCookie(cookies, "another cookie");

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testInvalidateCookie()
    {
        HttpServletResponse response = mock(HttpServletResponse.class);
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);


        HttpUtils.invalidateSecurityCookie(response);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie cookie = cookieCaptor.getValue();
        assertThat(cookie.getName(), is(SecurityFilter.SECURITY_COOKIE));
        assertThat(cookie.getMaxAge(), is(0));
    }

    @Test
    public void testCreateCookie()
    {
        String token = "security token";
        Cookie cookie = HttpUtils.createSecurityCookie(token);

        assertThat(cookie.getName(), is(SecurityFilter.SECURITY_COOKIE));
        assertThat(cookie.getDomain(), is(HttpUtils.DOMAIN));
        assertThat(cookie.getPath(), is(HttpUtils.PATH));
    }
}
