package security.rest.services.security.token;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 *
 * Test suite for {@link TokenHelper}
 *
 * @author Juan Rada
 */
public class TokenHelperTest
{
    private final static String SALT = "ABCDE";

    private TokenHelper tokenHelper = new TokenHelper(SALT.getBytes());

    @Test
    public void testGetTokenFromTokenId() throws Exception
    {
        assertThat(tokenHelper.getTokenFromTokenId("5"), is("NQ==.b1GwNYJYmNSykxwmpbNvr3M1En9FdUjDpNBx5PFc/Bw="));
    }

    @Test
    public void testGetTokenIdFromToken() throws Exception
    {
        assertThat(tokenHelper.getTokenIdFromToken("NQ==.b1GwNYJYmNSykxwmpbNvr3M1En9FdUjDpNBx5PFc/Bw=").get(), is("5"));
    }

    @Test
    public void testGetTokenIdFromTokenWithInvalidToken() throws Exception
    {
        assertThat(
                tokenHelper.getTokenIdFromToken("NQ==.b1GwNYJYmNSykxwmpbNvr3M1En9FdUjDpNBx5PFc/BwJ").isPresent(), is(false));
    }

}