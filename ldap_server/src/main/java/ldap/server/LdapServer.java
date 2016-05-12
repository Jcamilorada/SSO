package ldap.server;

/**
 * Ldap server class. Start an ldap server listener.
 *
 * @author Juan Rada
 */
public class LdapServer
{
    public static void main(String... args)
    {
        try
        {
            System.out.println("Runing ldap server");
            EmbeddedLdap.startServer();
        }

        catch (com.unboundid.ldap.sdk.LDAPException e)
        {
            e.printStackTrace();
        }

        catch (com.unboundid.ldif.LDIFException e)
        {
            e.printStackTrace();
        }
    }
}
