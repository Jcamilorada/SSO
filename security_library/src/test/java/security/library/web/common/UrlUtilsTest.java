package security.library.web.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


import org.junit.Test;

/**
 * Test suite for {@link UrlUtils}
 *
 */
public class UrlUtilsTest
{
    @Test
    public void testGetLogoutUrl() throws Exception
    {
        assertThat("asdas",
            UrlUtils.getLogoutUrl("http://www.mydomain.com", "http://security.com"),
            is("redirect:http://security.com/logout?redirectUrl=http://www.mydomain.com"));
    }
}
