package security.rest.services.security.ldap;

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
class EmbeddedLdap
{
    private static InMemoryDirectoryServer ds;

    private EmbeddedLdap()
    {

    }

    public static void startServer(String baseDN, int port) throws LDAPException, LDIFException
    {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(baseDN);
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("listener", port));
        config.setSchema(null);

        ds = new InMemoryDirectoryServer(config);
        addTestUserAndData(baseDN);
        ds.startListening();
    }

    public static void shutdown()
    {
        ds.shutDown(true);
    }

    private static void addTestUserAndData(String baseDN) throws LDIFException, LDAPException
    {
        final String[] baseDomain =
                {
                        "dn:" + baseDN,
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
