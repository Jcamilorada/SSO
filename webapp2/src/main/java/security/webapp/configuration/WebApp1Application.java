package security.webapp.configuration;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import security.library.web.servlet.SecurityFilter;

/**
 * Spring boot main configuration class.
 *
 * @author Juan Rada
 */
@SpringBootApplication
@ComponentScan({"security.webapp.controllers"})
public class WebApp1Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(WebApp1Application.class, args);
    }

    /**
     * Security filter registration bean. Register an instance of {@link Filter}
     *
     * @param loginApplication the logging application url.
     * @param validateUrl The rest service end point to validate secuirty tokens.
     *
     * @return an istance of {@link FilterRegistrationBean} with register filter.
     */
    @Bean()
    public FilterRegistrationBean securityFilter(
            @Value("${security.loginUrl}") String loginApplication,
            @Value("${security.validateUrl}") String validateUrl,
            @Value("${application.name}") String application)
    {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new SecurityFilter());

        Map<String, String> filterParameters = new HashMap(3);
        filterParameters.put(SecurityFilter.LOGGING_APPLICATION_PROPERTY, loginApplication);
        filterParameters.put(SecurityFilter.VALIDATE_URL_PROPERTY, validateUrl);
        filterParameters.put(SecurityFilter.APPLICATION_PROPERTY, application);

        filterRegistrationBean.setInitParameters(filterParameters);
        return filterRegistrationBean;
    }
}
