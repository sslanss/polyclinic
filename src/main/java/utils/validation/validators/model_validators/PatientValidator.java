package utils.validation.validators.model_validators;

import data.domain.models.Patient;
import java.time.LocalDate;
import utils.constraints.LengthConstraint;
import utils.constraints.NotEmptyConstraint;
import utils.constraints.NotNullConstraint;
import utils.validation.validators.FieldValidator;

public class PatientValidator {
    //string fieldName, message

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

    public boolean validate(Patient patient) {
        return INSURANCE_POLICY_NUMBER_VALIDATOR.validate(patient.getInsurancePolicyNumber())
                && FULL_NAME_VALIDATOR.validate(patient.getFullName())
                && DATE_OF_BIRTH_VALIDATOR.validate(patient.getDateOfBirth())
                && (patient.getPhoneNumber() == null || PHONE_NUMBER_VALIDATOR.validate(patient.getPhoneNumber()));
    }
}
