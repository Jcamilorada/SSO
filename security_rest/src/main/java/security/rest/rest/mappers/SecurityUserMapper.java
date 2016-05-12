package security.rest.rest.mappers;

import org.springframework.stereotype.Component;
import security.library.web.dto.SecurityUserDTO;
import security.rest.services.security.ldap.UserInformation;

/**
 * Maps between {@link SecurityUserDTO} and {@link UserInformation}
 *
 * @author Juan Rada
 */
@Component
public class SecurityUserMapper
{
    public SecurityUserDTO getSecurityUserDTO(UserInformation userInformation)
    {
        SecurityUserDTO securityUserDTO = new SecurityUserDTO();
        securityUserDTO.setEmail(userInformation.getEmail());
        securityUserDTO.setName(userInformation.getName());
        securityUserDTO.setUsername(userInformation.getUsername());
        securityUserDTO.setRoles(userInformation.getRoles());

        return securityUserDTO;
    }
}
