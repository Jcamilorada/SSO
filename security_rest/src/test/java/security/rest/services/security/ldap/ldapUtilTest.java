package security.rest.services.security.ldap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFException;
import java.util.Optional;
import ldap.server.EmbeddedLdap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link LdapUtil} test class use a in memory ldap server with mock data to validate operations.
 *
 * @author Juan Rada
 *
 */
public class ldapUtilTest
{
    private static final String LDAP_SERVER = "localhost";

    private LdapUtil testInstance = new LdapUtil(LDAP_SERVER, EmbeddedLdap.BASE_DN, EmbeddedLdap.SERVER_PORT);

    @Before
    public void setup() throws LDAPException, LDIFException
    {
        EmbeddedLdap.startServer();
    }

    @After
    public void teardDown()
    {
        EmbeddedLdap.shutdown();
    }

    @Test
    public void testAuthenticateUser() throws Exception
    {
        assertThat(testInstance.authenticateUser("dcgonzalez", "dcga123"), is(true));
        assertThat(testInstance.authenticateUser("dcgonzalez", "dcga1523"), is(false));
        assertThat(testInstance.authenticateUser("notvaliduser", "dcga123"), is(false));
    }

    @Test
    public void getUserInformationWithValidUser() throws Exception
    {
        Optional<UserInformation> user = testInstance.getUserInformation("dcgonzalez", "webapp1");
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void getUserInformationWithInvalidUser() throws Exception
    {
        Optional<UserInformation> user = testInstance.getUserInformation("dcgonzalezz", "webapp1");
        assertThat(user.isPresent(), is(false));
    }
}
