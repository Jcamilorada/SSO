package ldap.server;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldif.LDIFException;

/**
 * Utility class that enable in memory ldap server for testing porpose.
 *
 * @author Juan Rada
 */
public class EmbeddedLdap
{
    private static InMemoryDirectoryServer ds;
    public static final String BASE_DN = "dc=rest,dc=com";
    public static final int SERVER_PORT = 8282;

    private EmbeddedLdap()
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
        addDomain();
        addApplicationsOrganizationUnit();
        addUsersCommonName();

        addUser("dcgonzalez", "Diana Gonzalez", "Gonzalez", "dcga@gmail.com", "dcga123");
        addUser("jrada", "Juan Rada", "Rada", "jcamilorada@gmail.com", "12345");

        addApplication("webapp1");
        addApplicationRole("webapp1", "admin");
        addApplicationRole("webapp1", "approver");

        addApplication("webapp2");
        addApplicationRole("webapp2", "designer");

        addUserInApplicationRole("dcgonzalez", "webapp1", "admin");
        addUserInApplicationRole("dcgonzalez", "webapp1", "approver");

        addUserInApplicationRole("jrada", "webapp1", "admin");
        addUserInApplicationRole("jrada", "webapp2", "designer");
    }

    private static void addUserInApplicationRole(String username, String application, String roleName) throws LDIFException, LDAPException
    {
        final String[] userInApplicationRole =
                {
                        String.format("dn:cn=%s,ou=%s,ou=applications,dc=rest,dc=com", roleName, application),
                        "changetype: modify",
                        "add: member",
                        String.format("member: dn:uid=%s,cn=users,dc=rest,dc=com", username)
                };
        ds.modify(new ModifyRequest(userInApplicationRole));
    }


    private static void addApplicationRole(String application, String roleName) throws LDIFException, LDAPException
    {
        final String[] role =
                {
                        String.format("dn:cn=%s,ou=%s,ou=applications,dc=rest,dc=com", roleName, application),
                        "objectClass: organizationalunit",
                        "dc: server"
                };
        ds.add(new AddRequest(role));
    }

    private static void addApplication(String application) throws LDIFException, LDAPException
    {
        final String[] securityGroup =
                {
                        String.format("dn:ou=%s,ou=applications,dc=rest,dc=com", application),
                        "objectClass: organizationalunit"
                };
        ds.add(new AddRequest(securityGroup));
    }

    private static void addUser(String username, String cn, String sn, String email, String password) throws LDIFException, LDAPException
    {
        final String[] userDN =
                {
                        String.format("dn:uid=%s,cn=users,dc=rest,dc=com", username),
                        "changetype: add",
                        String.format("cn: %s", cn),
                        String.format("sn: $s", sn),
                        String.format("uid: %s", username),
                        String.format("mail: %s", email),
                        String.format("userPassword: %s", password)
                };
        ds.add(new AddRequest(userDN));
    }

    private static void addUsersCommonName() throws LDAPException, LDIFException
    {
        final String[] userOrganizationUnit =
                {
                        "dn:cn=users,dc=rest,dc=com",
                        "objectClass: organizationalunit",
                        "dc: server"
                };
        ds.add(new AddRequest(userOrganizationUnit));
    }

    private static void addApplicationsOrganizationUnit() throws LDAPException, LDIFException
    {
        final String[] userOrganizationUnit =
                {
                        "dn:ou=applications,dc=rest,dc=com",
                        "objectClass: organizationalunit",
                        "dc: server"
                };
        ds.add(new AddRequest(userOrganizationUnit));
    }

    private static void addDomain() throws LDAPException, LDIFException
    {
        final String[] baseDomain =
                {
                        "dn:dc=rest,dc=com",
                        "objectClass: top",
                        "objectClass: domain",
                };
        ds.add(new AddRequest(baseDomain));
    }
}
