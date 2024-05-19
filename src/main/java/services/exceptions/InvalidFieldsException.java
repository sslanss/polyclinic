package services.exceptions;

import data.dto.ErrorFieldsDto;
import lombok.Getter;

@Getter
public class InvalidFieldsException extends RuntimeException {
    private final ErrorFieldsDto errorFields;

    public InvalidFieldsException(ErrorFieldsDto errorFields) {
        this.errorFields = errorFields;
    }
}
