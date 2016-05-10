package security.rest.rest.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller Exception advice. Captures exception and retrieve message with specfic error code.
 *
 * @author Juan Rada
 */
@ControllerAdvice(annotations = RestController.class)
class ExceptionHandlerAdvice
{
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO onInvalidCredentialsException(InvalidCredentialsException ex)
    {
        ErrorDTO validationErrorDTO = new ErrorDTO();
        validationErrorDTO.setMessage(ex.getMessage());

        return validationErrorDTO;
    }
}
