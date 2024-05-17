package utils.validation.validators.model_validators;

import data.domain.models.Doctor;
import utils.constraints.LengthConstraint;
import utils.constraints.NotEmptyConstraint;
import utils.constraints.NotNullConstraint;
import utils.validation.validators.FieldValidator;

public class DoctorValidator {
    private final static FieldValidator<String> FULL_NAME_VALIDATOR = new FieldValidator<String>()
            .add(new NotNullConstraint<>())
            .add(new NotEmptyConstraint());
    private final static FieldValidator<Integer> SPECIALITY_VALIDATOR = new FieldValidator<Integer>()
            .add(new NotNullConstraint<>());
    private final static FieldValidator<String> PHONE_NUMBER_VALIDATOR = new FieldValidator<String>()
            .add(new LengthConstraint(10));

    public boolean validate(Doctor doctor) {
        return FULL_NAME_VALIDATOR.validate(doctor.getFullName())
               && SPECIALITY_VALIDATOR.validate(doctor.getSpecialityId())
               && (doctor.getPhoneNumber() == null || PHONE_NUMBER_VALIDATOR.validate(doctor.getPhoneNumber()));
    }

}
