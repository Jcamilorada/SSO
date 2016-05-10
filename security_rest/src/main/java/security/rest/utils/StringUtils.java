package security.rest.utils;

/**
 * Utitity String methds.
 *
 * @author Juan Rada
 *
 */
public class StringUtils
{
    private static String sanitizeUsername(String username)
    {
        return username.replaceAll("[^A-Za-z0-9-_.]", "");
    }
}
