package security.library.web.common;


/**
 * Provided usefull methods that can be used for security url generations.
 *
 * @author Juan Rada
 */
public class UrlUtils
{
    private static final String REDIRECT_URL_FORMAT = "redirect:%s/logout?redirectUrl=%s";

    private UrlUtils(){}

    /**
     *  Generate the redirection security url for the given security server. The generated url
     *  include source url to transfer user once the authentication process is completed.
     *
     * @param redirectUrl
     * @param securityServer
     *
     * @return String url representation.
     */
    public static String getLogoutUrl(String redirectUrl, String securityServer)
    {
        return String.format(REDIRECT_URL_FORMAT, securityServer, redirectUrl);
    }
}
