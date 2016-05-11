package security.rest.services.security.ldap;

import com.google.common.base.Preconditions;
import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import security.library.ldap.UserInformation;

/**
 * Ldap helper class. Provides useful methods to query configured ldap service.
 *
 * @author Juan Rada
 */
@Component
public class LdapUtil
{
    private static final Logger logger = LoggerFactory.getLogger(LdapUtil.class);

    private final String ldapServer;
    private final String baseDN;

    private final int serverPort;

    @Autowired
    public LdapUtil(
            @Value("${ldap.ldapServer}") final String ldapServer,
            @Value("${ldap.baseDN}") final String baseDN,
            @Value("${ldap.serverPort}") final int serverPort)
    {
        this.ldapServer = Preconditions.checkNotNull(ldapServer);
        this.baseDN = Preconditions.checkNotNull(baseDN);
        this.serverPort = serverPort;
    }

    public boolean authenticateUser(final String user, final String password)
    {
        Preconditions.checkNotNull(user, "user cannot be null");
        Preconditions.checkNotNull(password, "passowrd cannot be null");

        ResultCode resultCode = null;
        LDAPConnection ldapConnection = null;

        boolean validUser = false;
        try
        {
            ldapConnection = new LDAPConnection(ldapServer, serverPort);
            String userDN = dnFromUsername(ldapConnection, user);

            BindRequest bindRequest = new SimpleBindRequest(userDN, password);
            BindResult bindResult = ldapConnection.bind(bindRequest);
            resultCode = bindResult.getResultCode();
        }

        catch (LDAPException ex)
        {
            logger.error(ex.getMessage());
        }

        finally
        {
            ldapConnection.close();
        }

        if (resultCode != null && resultCode.equals(ResultCode.SUCCESS))
        {
            validUser = true;
        }

        return validUser;
    }


    public Optional<UserInformation> getUserInformation(String username, String application)
    {
        Preconditions.checkNotNull(username, "username cannot be null");

        Optional<UserInformation> userInformation = Optional.empty();

        try
        {
            LDAPConnection ldapConnection = new LDAPConnection(ldapServer, serverPort);
            SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, "(uid=" + username + ")");
            SearchResult sr = ldapConnection.search(searchRequest);

            List<SearchResultEntry> entries = sr.getSearchEntries();

            if (!entries.isEmpty())
            {
                SearchResultEntry user = sr.getSearchEntries().get(0);

                String email = user.getAttribute("mail").getValue();
                String name = user.getAttribute("cn").getValue();
                String dn = String.format("dn:%s", user.getDN());
                Set<String> roles = getUserApplicationRoles(ldapConnection, dn, application);

                userInformation = Optional.of(new UserInformation(username, name, email, roles));
            }
        }

        catch (LDAPException ex)
        {
            logger.error(ex.getMessage());
        }

        return userInformation;
    }

    private String dnFromUsername(final LDAPConnection connection, final String username) throws LDAPException
    {
        SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, "(uid=" + username + ")");

        SearchResult sr = connection.search(searchRequest);

        if (sr.getEntryCount() == 0)
        {
            throw new LDAPException(ResultCode.INVALID_CREDENTIALS);
        }

        return sr.getSearchEntries().get(0).getDN();
    }

    private Set<String> getUserApplicationRoles(
            final LDAPConnection connection,
            final String user,
            final String application) throws LDAPException
    {
        List<SearchResultEntry> applicationRoles = getApplicationRoles(connection, application);
        Set<String> applicationAccessSet = new LinkedHashSet<>(applicationRoles.size());

        for (SearchResultEntry sre : applicationRoles)
        {
            String[] members = sre.getAttribute("member").getValues();
            if (ArrayUtils.contains(members, user))
            {
                applicationAccessSet.add(sre.getAttribute("cn").getValue());
            }


        }
        return applicationAccessSet;
    }

    private List<SearchResultEntry> getApplicationRoles(
            final LDAPConnection connection, final String application) throws LDAPSearchException
    {
        SearchRequest searchRequest = new SearchRequest(
                String.format("ou=%s,ou=applications,dc=rest,dc=com", application),
                SearchScope.SUB,
                Filter.createPresenceFilter("member"));

        return connection.search(searchRequest).getSearchEntries();
    }
}
