package data.dto;

import java.util.List;
import utils.validation.validators.ErrorField;

public record ErrorFieldsDto(List<ErrorField> errorFields) {
}
