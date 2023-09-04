package graduationwork.backend.global.error.handler;


import graduationwork.backend.global.error.dto.ErrorBaseResponse;
import graduationwork.backend.global.error.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionResponseHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleForbiddenException(BadRequestException e) {
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleJwtException(UnauthorizedException e) {
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBaseResponse);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity handleConflictException(ConflictException e) {
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBaseResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity handleForbiddenException(ForbiddenException e) {
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBaseResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleForbiddenException(NotFoundException e) {
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBaseResponse);
    }


    /**
     * 지원하지 않는 HTTP method로 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity handleHttpRequestMethodNotSupportedException() {
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBaseResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBadRequest(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("url {}, message {}", request.getRequestURI(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.builder().message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()).status(400).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }
}
