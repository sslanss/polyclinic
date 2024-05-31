package services;

import data.domain.models.Patient;
import data.domain.repositories.PatientRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.ErrorFieldsDto;
import data.dto.PatientProfileDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import services.exceptions.InvalidFieldsException;
import services.exceptions.InvalidPasswordValue;
import services.exceptions.PatientHaveAlreadyRegistered;
import services.exceptions.PatientHaveNotRegisteredYet;
import utils.password.PasswordHasher;
import utils.validation.validators.ErrorField;
import utils.validation.validators.model_validators.PatientValidator;

public class PatientService {
    private final PatientRepository patientRepository;
    private final PasswordHasher passwordHasher;
    private final PatientValidator patientValidator;

    public PatientService(PatientRepository patientRepository, PasswordHasher passwordHasher,
                          PatientValidator patientValidator) {
        this.patientRepository = patientRepository;
        this.passwordHasher = passwordHasher;
        this.patientValidator = patientValidator;
    }

    public PatientProfileDto registerPatient(String insurancePolicyNumber, String fullName, String dateOfBirth,
                                             String gender, String phoneNumber, String address, String password)
            throws DataRepositoryException {
        Optional<Patient> findingPatient = patientRepository.findByPolicyNumber(insurancePolicyNumber);

        if (findingPatient.isPresent()) {
            Patient onsiteVisitedPatient = findingPatient.get();
            if (isPatientAnUser(onsiteVisitedPatient)) {
                throw new PatientHaveAlreadyRegistered();
            }

            patientRepository.updatePassword(insurancePolicyNumber, password);
            return patientRepository.getPatientMapper().mapPatientToPatientProfileDto(onsiteVisitedPatient);
        } else {
            Patient registeringPatient = new Patient(insurancePolicyNumber, fullName, LocalDate.parse(dateOfBirth),
                    patientRepository.getPatientMapper().getGenderMapper().mapGenderFromUserForm(gender),
                    phoneNumber, address, password);

            List<ErrorField> errors = patientValidator.validate(registeringPatient);
            if (!errors.isEmpty()) {
                throw new InvalidFieldsException(new ErrorFieldsDto(errors));
            }

            registeringPatient.setPassword(passwordHasher.hash(password));
            patientRepository.add(registeringPatient);

            return patientRepository.getPatientMapper().mapPatientToPatientProfileDto(registeringPatient);
        }
    }

    public PatientProfileDto loginPatient(String insurancePolicyNumber, String password)
            throws DataRepositoryException {
        Patient patient = patientRepository.findByPolicyNumber(insurancePolicyNumber)
                .orElseThrow(PatientHaveNotRegisteredYet::new);

        if (!isPatientAnUser(patient)) {
            throw new PatientHaveNotRegisteredYet();
        }

        if (!passwordHasher.check(password, patient.getPassword())) {
            throw new InvalidPasswordValue();
        }

        return patientRepository.getPatientMapper().mapPatientToPatientProfileDto(patient);
    }

    private boolean isPatientAnUser(Patient patient) {
        return patient.getPassword() != null;
    }
}
