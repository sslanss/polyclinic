package utils.validation.validators.model_validators;

import data.domain.models.Patient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.constraints.LengthConstraint;
import utils.constraints.NotEmptyConstraint;
import utils.constraints.NotNullConstraint;
import utils.validation.validators.ErrorField;
import utils.validation.validators.FieldValidator;

public class PatientValidator {
    private List<ErrorField> errorFields;

    private final static FieldValidator<String> INSURANCE_POLICY_NUMBER_VALIDATOR = new FieldValidator<String>()
            .add(new NotNullConstraint<>())
            .add(new LengthConstraint(16));
    private final static FieldValidator<String> FULL_NAME_VALIDATOR = new FieldValidator<String>()
            .add(new NotNullConstraint<>())
            .add(new NotEmptyConstraint());
    private final static FieldValidator<LocalDate> DATE_OF_BIRTH_VALIDATOR = new FieldValidator<LocalDate>()
            .add(new NotNullConstraint<>());
    private final static FieldValidator<String> PHONE_NUMBER_VALIDATOR = new FieldValidator<String>()
            .add(new LengthConstraint(10));

    private final static FieldValidator<String> PASSWORD_VALIDATOR = new FieldValidator<String>()
            .add(new NotNullConstraint<>());

    public PatientValidator() {
        errorFields = new ArrayList<>();
    }

    public List<ErrorField> validate(Patient patient) {
        errorFields = new ArrayList<>();
        if (!INSURANCE_POLICY_NUMBER_VALIDATOR.validate(patient.getInsurancePolicyNumber())) {
            errorFields.add(new ErrorField("Номер страхового полиса (ОМС)", "Непустое значение. "
                  +  "16 числовых символов."));
        }
        if (!FULL_NAME_VALIDATOR.validate(patient.getFullName())) {
            errorFields.add(new ErrorField("ФИО", "Непустое значение. Буквенный формат."));
        }
        if (!DATE_OF_BIRTH_VALIDATOR.validate(patient.getDateOfBirth())) {
            errorFields.add(new ErrorField("Дата рождения", "Непустое значение."));
        }
        if (patient.getPhoneNumber() != null && !PHONE_NUMBER_VALIDATOR.validate(patient.getPhoneNumber())) {
            errorFields.add(new ErrorField("Номер телефона", "Непустое значение. "
                  +  "10 числовых символов"));
        }
        if (!PASSWORD_VALIDATOR.validate(patient.getPassword())) {
            errorFields.add(new ErrorField("Пароль", "Непустое значение."));
        }
        return errorFields;
    }
}
