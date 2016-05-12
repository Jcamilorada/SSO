package security.webapp.controllers;


import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import security.library.web.common.HttpUtils;
import security.library.web.dto.LoginResponseDTO;
import security.library.web.dto.LogoutResponseDTO;
import security.library.web.services.RemoteSecurityService;
import security.library.web.servlet.SecurityFilter;


/**
 * Handle logging request.
 *
 * @author Juan Rada
 */
@Controller
public class LoginController
{
    private RemoteSecurityService remoteSecurityService;

    @Autowired
    LoginController(@Value("${security.server}") String loginServer)
    {
        remoteSecurityService = new RemoteSecurityService(loginServer);
    }

    @RequestMapping("/")
    public String index(
            Map<String, Object> model,
            @RequestParam(value = "redirectUrl", required = false) String redirectUrl)
    {
        model.put("RedirectUrl", redirectUrl);
        return "login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(
            @RequestParam("Username") String username,
            @RequestParam("Password") String password,
            @RequestParam("RedirectUrl") String redirectUrl, Map<String, Object> model,
            HttpServletResponse response)
    {
        String view = "login";

        LoginResponseDTO responseDTO = remoteSecurityService.login(username, password);
        model.put("hasErrors", responseDTO.isValid());

        if (responseDTO.isValid())
        {
            response.addCookie(HttpUtils.createSecurityCookie(responseDTO.getToken()));
            view = String.format("redirect:%s", redirectUrl);
        }

        else
        {
            model.put("error", "Invalid user or pasword");
            model.put("RedirectUrl", redirectUrl);
        }

        return view;
    }


    @RequestMapping("/logout")
    public String logout(
            Map<String, Object> model,
            @RequestParam("redirectUrl") String redirectUrl,
            @CookieValue(SecurityFilter.SECURITY_COOKIE) String token,
            HttpServletResponse response)
    {
        LogoutResponseDTO logoutResponseDTO = remoteSecurityService.logout(token);
        HttpUtils.invalidateSecurityCookie(response);
        model.put("RedirectUrl", redirectUrl);
        return "login";
    }
}
