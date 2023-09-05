package graduationwork.backend.global.error.exception;

public class ConflictException extends BusinessException{

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
    public ConflictException(){
        super(ErrorCode.CONFLICT);
    }

}
