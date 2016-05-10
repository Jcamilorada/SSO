package security.webapp.configuration;


import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import security.library.web.servlet.SecurityFilter;

@SpringBootApplication
@ComponentScan({"security.webapp.controllers"})
public class WebApp1Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(WebApp1Application.class, args);
    }

    @Bean()
    public FilterRegistrationBean securityFilter(
            @Value("${security.loginUrl}") String server,
            @Value("${security.validateUrl}") String validateUrl)
    {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new SecurityFilter());

        Map<String, String> filterParameters = new HashMap(2);
        filterParameters.put(SecurityFilter.LOGGING_APPLICATION_PROPERTY, server);
        filterParameters.put(SecurityFilter.VALIDATE_URL_PROPERTY, validateUrl);

        filterRegistrationBean.setInitParameters(filterParameters);
        return filterRegistrationBean;
    }
}
