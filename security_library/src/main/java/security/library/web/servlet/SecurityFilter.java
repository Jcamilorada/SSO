package security.library.web.servlet;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.services.RemoteSecurityService;


/**
 * Security Filter. Enable application to use SSO aplication to provided application security.
 *
 * @author Juan Rada
 *
 */
@Getter(AccessLevel.PACKAGE)
public class SecurityFilter implements Filter
{
    public static final String LOGGING_APPLICATION_PROPERTY = "LOGIN_WEB_APP";
    public static final String VALIDATE_URL_PROPERTY = "VALIDATE_TOKEN_URL";

    public static final String SECURITY_COOKIE = "SECURITY_COOKIE";
    public static final String SECURITY_REQUEST_ATTRIBUTE = "SECURITY_USER";

    private RemoteSecurityService securityService;
    private String loggingApplicationUrl;
    private String validationUrl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        loggingApplicationUrl = filterConfig.getInitParameter(LOGGING_APPLICATION_PROPERTY);
        validationUrl = filterConfig.getInitParameter(VALIDATE_URL_PROPERTY);

        securityService = new RemoteSecurityService(validationUrl);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest servletRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        String token = getCookieValue(servletRequest.getCookies());

        if (token != null)
        {
            Optional<SecurityUserDTO> securityUser = securityService.getSecurityUser(token);

            if (securityUser.isPresent())
            {
                servletRequest.setAttribute(SECURITY_REQUEST_ATTRIBUTE, securityUser.get());
                chain.doFilter(servletRequest, response);
            }
        }

        else
        {
            String url = servletRequest.getRequestURL().toString();
            httpResponse.sendRedirect(String.format("%s?redirectUrl=%s", getLoggingApplicationUrl(), url));
        }
    }

    @Override
    public void destroy()
    {

    }

    private String getCookieValue(Cookie[] cookies)
    {
        String value = null;

        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equalsIgnoreCase(SECURITY_COOKIE) &&
                        !Strings.isNullOrEmpty(cookie.getValue()))
                {
                    value = cookie.getValue();
                }
            }
        }

        return value;
    }
}
