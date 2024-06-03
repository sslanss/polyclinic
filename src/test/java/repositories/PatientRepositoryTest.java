package repositories;

import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientMapper;
import data.domain.models.Patient;
import data.domain.models.dictionaries.Gender;
import data.domain.repositories.PatientRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryTest {

    private final PatientRepository patientRepository = new PatientRepository(new PatientMapper(new GenderMapper()));

    private final List<Patient> testPatientRecords = new ArrayList<>() {{
        add(new Patient("1111111111111111", "Patient 1", LocalDate.of(
                1980, 1, 1), Gender.MALE, null, null,
                "erqq23dd44"));
        add(new Patient("1111111111111112", "Patient 2", LocalDate.of(
                1990, 2, 2), Gender.FEMALE, null, null,
                "erqq2d6d44"));
        add(new Patient("1111111111111113", "Patient 3", LocalDate.of(
                2000, 3, 3), Gender.FEMALE, null, null,
                "ertd3gd644"));
    }};

    @BeforeEach
    public void fillRepository() {
        for (var patient : testPatientRecords) {
            patientRepository.add(patient);
        }
    }

    @AfterEach
    public void clearRepository() {
        for (var patient : testPatientRecords) {
            patientRepository.delete(patient);
        }
    }

    @Test
    public void repositoryShouldCorrectlyAddPatient() {
        Patient patient = new Patient("1111111111111114", "Bob Brown",
                LocalDate.of(1975, 4, 4), Gender.MALE, "555-3456",
                "321 Pine St", "password321");

        Assertions.assertThat(patientRepository.add(patient)).isTrue();
        testPatientRecords.add(patient);
        Assertions.assertThatThrownBy(() -> patientRepository.add(patient)).isInstanceOf(DataRepositoryException.class);
    }

    @Test
    public void repositoryShouldCorrectlyFindAllPatients() {
        List<Patient> allPatients = patientRepository.findAll();

        Assertions.assertThat(allPatients).containsAll(testPatientRecords);
        Assertions.assertThat(allPatients.size()).isGreaterThanOrEqualTo(testPatientRecords.size());
    }

    @Test
    public void repositoryShouldCorrectlyFindByPolicyNumber() {
        Patient patient = testPatientRecords.get(0);
        Optional<Patient> foundPatient = patientRepository.findByPolicyNumber(patient.getInsurancePolicyNumber());

        Assertions.assertThat(foundPatient).isPresent();
        Assertions.assertThat(foundPatient.get()).isEqualTo(patient);
    }

    @Test
    public void repositoryShouldCorrectlyDeletePatient() {
        Patient deletingPatient = testPatientRecords.get(0);

        Assertions.assertThat(patientRepository.delete(deletingPatient)).isTrue();
        Assertions.assertThat(patientRepository.delete(deletingPatient)).isFalse();
    }

    @Test
    public void repositoryShouldCorrectlyUpdatePassword() throws DataRepositoryException {
        Patient patient = testPatientRecords.get(0);
        String newPassword = "ee456fgtd44";
        patient.setPassword("ee456fgtd44");
        patientRepository.updatePassword(patient);

        Optional<Patient> updatedPatient = patientRepository.findByPolicyNumber(patient.getInsurancePolicyNumber());
        Assertions.assertThat(updatedPatient).isPresent();
        Assertions.assertThat(updatedPatient.get().getPassword()).isEqualTo(newPassword);
    }
}
