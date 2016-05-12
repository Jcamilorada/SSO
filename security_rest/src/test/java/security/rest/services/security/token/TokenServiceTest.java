package security.rest.services.security.token;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import security.rest.persistence.entities.DBToken;
import security.rest.persistence.repositories.DBTokenRepository;

/**
 *  Test suite for {@link TokenService}
 *
 *  @author Juan Rada
 */
@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest
{
    private String user = "user";

    private String tokenId = "1";
    private String token = "token";

    @Mock private DBTokenRepository tokenRepository;
    @Mock private TokenHelper tokenHelper;

    @InjectMocks private TokenService testInstance;

    @Test
    public void testGetTokenForUser() throws Exception
    {
        DBToken dbToken = new DBToken();
        dbToken.setId(1l);

        when(tokenRepository.save(any(DBToken.class))).thenReturn(dbToken);
        when(tokenHelper.getTokenFromTokenId(tokenId)).thenReturn(token);

        assertThat(testInstance.getTokenForUser(user), is(token));
        verify(tokenRepository).save(any(DBToken.class));
        verify(tokenHelper).getTokenFromTokenId(tokenId);
    }

    @Test
    public void testGetSecurityUserFromTokenWhenTokenCorrespond() throws Exception
    {
        DBToken dbToken = new DBToken();
        dbToken.setUser(user);
        when(tokenHelper.getTokenIdFromToken(token)).thenReturn(Optional.of(tokenId));
        when(tokenRepository.findOne(1l)).thenReturn(dbToken);
        when(tokenRepository.save(dbToken)).thenReturn(dbToken);

        assertThat(testInstance.getSecurityUserFromToken(token), is(Optional.of(user)));
    }

    @Test
    public void testGetSecurityUserFromTokenWhenTokenNotCorrespond() throws Exception
    {
        DBToken dbToken = new DBToken();
        dbToken.setUser(user);
        when(tokenHelper.getTokenIdFromToken(token)).thenReturn(Optional.empty());
        when(tokenRepository.findOne(1l)).thenReturn(dbToken);

        assertThat(testInstance.getSecurityUserFromToken(token), is(Optional.empty()));
    }

    @Test
    public void testInvalidateAllWhenTokenExist() throws Exception
    {
        testInstance = spy(testInstance);
        doReturn(Optional.of(user)).when(testInstance).getSecurityUserFromToken(token);

        testInstance.invalidateAll(user);
        verify(tokenRepository).invalidateByUser(user);
    }
}
