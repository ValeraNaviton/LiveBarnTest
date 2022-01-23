package livebarntest.endpoints.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(SushiTypeNotFoundException.class)
  public ResponseEntity<Object> handleException(SushiTypeNotFoundException e, WebRequest request) {
    return handleExceptionInternal(e, e.getErrorMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Object> handleException(OrderNotFoundException e, WebRequest request) {
    return handleExceptionInternal(e, e.getOrderId(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(IllegalOrderStatusChangeException.class)
  public ResponseEntity<Object> handleException(IllegalOrderStatusChangeException e, WebRequest request) {
    return handleExceptionInternal(e,
      "Order [" + e.getOrderId() + "] status cannot be changed from [" + e.getOldStatus().getName() + "] to [" + e.getNewStatus().getName() + "]",
      new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
  }

}