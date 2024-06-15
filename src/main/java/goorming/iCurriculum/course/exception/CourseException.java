package goorming.iCurriculum.course.exception;

import goorming.iCurriculum.common.code.BaseErrorCode;
import goorming.iCurriculum.common.exception.GeneralException;

public class CourseException extends GeneralException {
    public CourseException(BaseErrorCode code) {
        super(code);
    }
}
