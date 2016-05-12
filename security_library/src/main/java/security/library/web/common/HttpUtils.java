package security.library.web.common;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.servlet.SecurityFilter;

/**
 * Contains useful methods related to http request operations.
 *
 * @author Juan Rada
 */
public class HttpUtils
{
    private HttpUtils(){}
    static String DOMAIN = ".app.localhost";
    static String PATH = "/";

    public static Optional<Cookie> getCookie(Cookie[] cookies, String cookieName)
    {
        return cookies != null ?
                Arrays.stream(cookies).filter(c -> c.getName().equals(cookieName)).findFirst() : Optional.empty();
    }

    public static void invalidateSecurityCookie(HttpServletResponse response)
    {
        Cookie cookie = new Cookie(SecurityFilter.SECURITY_COOKIE, null);
        cookie.setMaxAge(0);
        cookie.setDomain(DOMAIN);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static Optional<Cookie> getSecurityCookie(HttpServletRequest request)
    {
        return getCookie(request.getCookies(), SecurityFilter.SECURITY_COOKIE);
    }

    public static Cookie createSecurityCookie(String cookieValue)
    {
        Cookie cookie = new Cookie(SecurityFilter.SECURITY_COOKIE, cookieValue);
        cookie.setMaxAge(3600);
        cookie.setDomain(DOMAIN);
        cookie.setPath("/");

        return cookie;
    }

    public static SecurityUserDTO getSecurityUser(HttpServletRequest request)
    {
        return (SecurityUserDTO) request.getAttribute(SecurityFilter.SECURITY_REQUEST_ATTRIBUTE);
    }
}
