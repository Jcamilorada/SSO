package security.library.web.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;
import javax.servlet.http.Cookie;
import org.junit.Test;

/**
 * Test suite for {@link HttpUtil}
 *
 * @author Juan Rada
 */
public class HttpUtilTest
{
    @Test
    public void getCookieWhenCookieExist() throws Exception
    {
        String cookieName = "cookie";
        String cookieValue = "cookie value";

        Cookie cookie = new Cookie(cookieName, cookieValue);
        Cookie[] cookies = new Cookie[]{cookie};

        Optional<Cookie> result = HttpUtil.getCookie(cookies, cookieName);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(cookie));
    }

    @Test
    public void getCookieWhenCookieDoesNotExist() throws Exception
    {
        String cookieName = "cookie";
        String cookieValue = "cookie value";

        Cookie cookie = new Cookie(cookieName, cookieValue);
        Cookie[] cookies = new Cookie[]{cookie};

        Optional<Cookie> result = HttpUtil.getCookie(cookies, "another cookie");

        assertThat(result.isPresent(), is(false));
    }
}