package security.rest.services.security.token;

import com.google.common.base.Preconditions;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.rest.persistence.entities.DBToken;
import security.rest.persistence.repositories.DBTokenRepository;

@Service
public class TokenService
{
    private final DBTokenRepository dbTokenRepository;
    private final TokenHelper tokenHelper;

    @Autowired
    public TokenService(
            DBTokenRepository dbTokenRepository,
            TokenHelper tokenHelper)
    {
        this.dbTokenRepository = Preconditions.checkNotNull(dbTokenRepository);
        this.tokenHelper = Preconditions.checkNotNull(tokenHelper);
    }

    @Transactional
    public String getTokenForUser(String user)
    {
        dbTokenRepository.invalidateByUser(user);

        DBToken token = new DBToken();
        token.setCreatedDate(LocalDateTime.now());
        token.setLastUsed(LocalDateTime.now());
        token.setUser(user);
        token.setValid(true);

        token = dbTokenRepository.save(token);
        String tokenString = tokenHelper.getTokenFromTokenId(String.valueOf(token.getId()));

        return tokenString;
    }

    public Optional<String> getSecurityUserFromToken(String tokenString)
    {
        Optional<String> user = Optional.empty();

        Optional<String> tokenId = tokenHelper.getTokenIdFromToken(tokenString);
        if (tokenId.isPresent())
        {
            // Update last usage date
            DBToken token = dbTokenRepository.findOne(Long.parseLong(tokenId.get()));
            token.setLastUsed(LocalDateTime.now());
            token = dbTokenRepository.save(token);

            user = Optional.of(token.getUser());
        }

        return user;
    }

    @Transactional
    public void invalidateAll(String token)
    {
        Optional<String> user = getSecurityUserFromToken(token);
        if (user.isPresent())
        {
            dbTokenRepository.invalidateByUser(user.get());
        }
    }
}
