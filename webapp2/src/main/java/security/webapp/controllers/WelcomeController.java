package security.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import security.library.web.common.HttpUtils;
import security.library.web.dto.SecurityUserDTO;
import security.library.web.common.UrlUtils;

/**
 * Welcome Controller, handle web application request.
 *
 * @author Juan Rada
 */
@Controller
public class WelcomeController
{
    public static final String APPLICATION_URL_STRING_FORMAT = "%s://%s:%d%s";

    /**
     * Retrieve the current logged user information.
     *
     * @param servletRequest an instance of {@link HttpServletRequest} with request information.
     *
     * @return an instance of {@link SecurityUserDTO} with the current logged user information.
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @CrossOrigin(origins =  "http://security.app.localhost:9393/security_webapp")
    public @ResponseBody
    SecurityUserDTO getSecurityUser(HttpServletRequest servletRequest)
    {
        return HttpUtils.getSecurityUser(servletRequest);
    }

    /**
     * Redirect user to security web application. Current application is send as redirection url to
     * handle the scenario when user was to loggin again.
     *
     * @param securityServer the security server url.
     * @param contextPath the application context-Path.
     * @param request an instance of {@link HttpServletRequest}
     * @return the logout security application url.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(
            @Value("${security.loginUrl}") String securityServer,
            @Value("${server.context-path}") String contextPath,
            HttpServletRequest request)
    {
        String baseUrl = String.format(
            APPLICATION_URL_STRING_FORMAT,
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                contextPath);

        return UrlUtils.getLogoutUrl(baseUrl, securityServer);
    }
}
