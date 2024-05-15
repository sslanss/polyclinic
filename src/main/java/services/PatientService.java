package services;

import data.domain.repositories.AppointmentRepository;
import data.domain.repositories.PatientRepository;
import data.dto.PatientProfileDto;
import java.sql.SQLException;
import services.exceptions.PatientHaveNotRegisteredYet;

public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public void registerPatient() {
    }

    public PatientProfileDto loginPatient(String insurancePolicyNumber) throws SQLException {
        patientRepository.findByPolicyNumber(insurancePolicyNumber).orElseThrow(PatientHaveNotRegisteredYet::new);

        return null;
    }
}
