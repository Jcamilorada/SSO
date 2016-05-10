package security.rest.rest.common.exception;

/**
 * Invalid credentials exceptions. Is throw then user try to autenticate with invalid credentials.
 *
 * @author Juan Rada
 */
public class InvalidCredentialsException extends RuntimeException
{
    public InvalidCredentialsException(String message)
    {
        super(message);
    }
}
