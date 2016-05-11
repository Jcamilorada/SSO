package security.library.web.common;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;

/**
 * Contains useful methods related to http request operations.
 *
 * @author Juan Rada
 */
public class HttpUtil
{
    private HttpUtil(){}

    public static Optional<Cookie> getCookie(Cookie[] cookies, String cookieName)
    {
        return cookies != null ?
                Arrays.stream(cookies).filter(c -> c.getName().equals(cookieName)).findFirst() : Optional.empty();
    }
}
