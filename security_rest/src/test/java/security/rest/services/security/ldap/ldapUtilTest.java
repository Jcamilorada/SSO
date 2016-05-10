package security.rest.services.security.ldap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFException;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import security.library.ldap.UserInformation;

/**
 * Test suite for {@link LdapUtil} test class use a in memory ldap server with mock data to validate operations.
 *
 * @author Juan Rada
 *
 */
public class ldapUtilTest
{
    private static final String LDAP_SERVER = "localhost";
    private static final int LDAP_SERVER_PORT=  8888;
    private static final String BASE_DN = "dc=rest,dc=com";

    private LdapUtil testInstance = new LdapUtil(LDAP_SERVER, BASE_DN, LDAP_SERVER_PORT);

    @Before
    public void setup() throws LDAPException, LDIFException
    {
        EmbeddedLdap.startServer(BASE_DN, LDAP_SERVER_PORT);
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
        Optional<UserInformation> user = testInstance.getUserInformation("dcgonzalez");
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void getUserInformationWithInvalidUser() throws Exception
    {
        Optional<UserInformation> user = testInstance.getUserInformation("dcgonzalezz");
        assertThat(user.isPresent(), is(false));
    }
}