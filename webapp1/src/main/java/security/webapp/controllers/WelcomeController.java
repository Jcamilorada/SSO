package security.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import security.library.web.dto.SecurityUserDTO;

@Controller
public class WelcomeController
{
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public @ResponseBody
    SecurityUserDTO getSecurityUser(@Value("#{request.getAttribute('SECURITY_USER')}") SecurityUserDTO currentUser)
    {
        return currentUser;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(
            @Value("${security.loginUrl}") String securityServer,
            @Value("${server.context-path}") String contextPath,
            HttpServletRequest request)
    {
        String baseUrl = String.format(
                "%s://%s:%d%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                contextPath);

        return String.format("redirect:%s/logout?redirectUrl=%s", securityServer, baseUrl);
    }
}
