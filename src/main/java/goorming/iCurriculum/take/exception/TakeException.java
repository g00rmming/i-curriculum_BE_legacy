package goorming.iCurriculum.take.exception;

import goorming.iCurriculum.common.code.BaseErrorCode;
import goorming.iCurriculum.common.exception.GeneralException;
import lombok.Getter;

@Getter
public class TakeException extends GeneralException {
    public TakeException(BaseErrorCode code){
        super(code);
    }
}
