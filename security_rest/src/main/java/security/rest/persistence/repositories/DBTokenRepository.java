package security.rest.persistence.repositories;

import security.rest.persistence.entities.DBToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring JPA repository. Enable Crud operations for Tokens.
 *
 * @author Juan Rada
 */
public interface DBTokenRepository extends CrudRepository<DBToken, Long>
{
    @Modifying
    @Query("Update DBToken SET VALID = 0 WHERE User=?1")
    void invalidateByUser(String user);
}
