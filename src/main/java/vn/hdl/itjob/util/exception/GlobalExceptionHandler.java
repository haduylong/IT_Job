package vn.hdl.itjob.util.exception;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hdl.itjob.domain.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            Exception.class
    }) // handle all exception
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ApiResponse<Void> res = new ApiResponse<>();
        res.setMessage("Internal Server Error");
        res.setError(ex.getMessage());
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(value = {
            InvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleAppException(Exception ex) {
        ApiResponse<Void> res = new ApiResponse<>();
        res.setMessage("Exception occurs ...");
        res.setError(ex.getMessage());
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            Locale locale) {
        // get list error
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(field -> field.getDefaultMessage()).toList();

        // response
        ApiResponse<Void> res = new ApiResponse<>();
        res.setMessage(ex.getBody().getDetail());
        Object respErrors = errors.size() > 1 ? errors : errors.get(0);
        res.setError(respErrors);
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            StorageException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleAppException(StorageException ex) {
        ApiResponse<Void> res = new ApiResponse<>();
        res.setMessage("File upload exception occurs ...");
        res.setError(ex.getMessage());
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
