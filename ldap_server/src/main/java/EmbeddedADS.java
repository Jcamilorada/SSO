import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFException;

/**
 * Utility class that enable in memory ldap server for testing porpose.
 *
 * @author Juan Rada
 */
public class EmbeddedADS
{
    private static InMemoryDirectoryServer ds;
    private static final String BASE_DN = "dc=rest,dc=com";
    private static final int SERVER_PORT = 8282;

    private EmbeddedADS()
    {

    }

    public static void startServer() throws LDAPException, LDIFException
    {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(BASE_DN);
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("listener", SERVER_PORT));
        config.setSchema(null);

        ds = new InMemoryDirectoryServer(config);
        addTestUserAndData();
        ds.startListening();
    }

    public static void shutdown()
    {
        ds.shutDown(true);
    }

    private static void addTestUserAndData() throws LDIFException, LDAPException
    {
        final String[] baseDomain =
                {
                        "dn:dc=rest,dc=com",
                        "objectClass: top",
                        "objectClass: domain",
                };
        ds.add(new AddRequest(baseDomain));

        final String[] userOrganizationUnit =
                {
                    "dn:ou=users,dc=rest,dc=com",
                    "objectClass: organizationalunit",
                    "dc: server"
                };
        ds.add(new AddRequest(userOrganizationUnit));

        final String[] userDcGonzalez =
                {
                        "dn:uid=dcgonzalez,ou=users,dc=rest,dc=com",
                        "changetype: add",
                        "cn: Diana Gonzalez",
                        "sn: Gonzalez",
                        "uid: dcgonzalez",
                        "mail: dcga@gmail.com",
                        "userPassword: dcga123"
                };
        ds.add(new AddRequest(userDcGonzalez));

        final String[] userJcRada =
                {
                        "dn:uid=jrada,ou=users,dc=rest,dc=com",
                        "changetype: add",
                        "cn: Juan Rada",
                        "sn: Rada",
                        "uid: jrada",
                        "mail: jcamilorada@gmail.com",
                        "userPassword: 12345"
                };
        ds.add(new AddRequest(userJcRada));
    }
}
