package security.rest.rest.common.exception;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * Test suite for {@link ExceptionHandlerAdvice}
 *
 * @author Juan Rada
 *
 */
public class ExceptionHandlerAdviceTest
{
    private ExceptionHandlerAdvice testInstance = new ExceptionHandlerAdvice();

    @Test
    public void onInvalidCredentialsException() throws Exception
    {
        String errorMessage = "error message";
        ErrorDTO error = testInstance.onInvalidCredentialsException(new InvalidCredentialsException(errorMessage));
        assertThat(error.getMessage(), is(errorMessage));
    }

}