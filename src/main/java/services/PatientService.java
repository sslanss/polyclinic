package services;

import data.domain.models.Patient;
import data.domain.repositories.AppointmentRepository;
import data.domain.repositories.PatientRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.PatientProfileDto;
import utils.password.PasswordHasher;
import services.exceptions.InvalidPasswordValue;
import services.exceptions.PatientHaveAlreadyRegistered;
import services.exceptions.PatientHaveNotRegisteredYet;

import java.util.Optional;

public class PatientService {
    private final PatientRepository patientRepository;
    private final PasswordHasher passwordHasher;
    private final AppointmentRepository appointmentRepository;

    public PatientService(PatientRepository patientRepository, PasswordHasher passwordHasher, AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.passwordHasher = passwordHasher;
        this.appointmentRepository = appointmentRepository;
    }

    public void registerPatient(String insurancePolicyNumber, String fullName, String dateOfBirth,
                                String gender, String phoneNumber, String address, String password)
            throws DataRepositoryException {
        Optional<Patient> patient = patientRepository.findByPolicyNumber(insurancePolicyNumber);
        if (patient.isPresent()) {
            if (isPatientAnUser(patient.get())) {
                throw new PatientHaveAlreadyRegistered();
            }
            patientRepository.updatePassword(insurancePolicyNumber, password);
        } else {
            //PatientValidator.validate(patient)
           // Patient registeringPatient = new Patient(insurancePolicyNumber,fullName,LocalDate.parse(dateOfBirth),
            //patientRepository.getMapper().
            //patientRepository.add()
        }

    }

    public PatientProfileDto loginPatient(String insurancePolicyNumber, String password) throws DataRepositoryException {
        //try {
        Patient patient = patientRepository.findByPolicyNumber(insurancePolicyNumber)
                .orElseThrow(PatientHaveNotRegisteredYet::new);

        if (!isPatientAnUser(patient)) {
            throw new PatientHaveNotRegisteredYet();
        }

        String encryptedPassword = passwordHasher.encryptPassword(password);
        patient = patientRepository.findByPassword(encryptedPassword).orElseThrow(InvalidPasswordValue::new);

        return patientRepository.getMapper().mapPatientToPatientProfileDto(patient);
        //}
    }

    private boolean isPatientAnUser(Patient patient) {
        return patient.getPassword() != null;
    }
}
