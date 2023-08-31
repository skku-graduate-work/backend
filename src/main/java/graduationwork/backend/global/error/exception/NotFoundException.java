package graduationwork.backend.global.error.exception;

public class NotFoundException extends BusinessException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }
}
