package security.rest.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({
        "security.rest.rest.*",
        "security.rest.services.*"
})
@EntityScan(basePackages = "security.rest.persistence.entities")
@EnableJpaRepositories("security.rest.persistence.repositories")
public class SecurityApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
