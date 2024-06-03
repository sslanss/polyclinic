package repositories;

import data.domain.mappers.AppointmentTypeMapper;
import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientMapper;
import data.domain.mappers.PatientRecordMapper;
import data.domain.models.Doctor;
import data.domain.models.Patient;
import data.domain.models.PatientRecord;
import data.domain.models.dictionaries.AppointmentType;
import data.domain.models.dictionaries.Gender;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.PatientRecordRepository;
import data.domain.repositories.PatientRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PatientRecordRepositoryTest {
    private final PatientRecordRepository patientRecordRepository = new PatientRecordRepository(
            new PatientRecordMapper(new AppointmentTypeMapper()));

    private final DoctorRepository doctorRepository = new DoctorRepository(new DoctorMapper(new GenderMapper()));

    private final PatientRepository patientRepository = new PatientRepository(new PatientMapper(new GenderMapper()));

    private final LocalDateTime currentDateTime = LocalDateTime.now();

    private final List<PatientRecord> testPatientRecords = new ArrayList<>() {{
        add(new PatientRecord(100, "1111111111111111", currentDateTime, AppointmentType.SCHEDULED));
        add(new PatientRecord(100, "1111111111111111", currentDateTime.plusDays(1),
                AppointmentType.SCHEDULED));
        add(new PatientRecord(101, "1111111111111111", currentDateTime.plusDays(2),
                AppointmentType.SCHEDULED));
    }};

    private final List<Patient> testPatients = new ArrayList<>() {{
        add(new Patient("1111111111111111", "Patient 1",
                LocalDate.of(2000, 3, 1), Gender.FEMALE, null, null,
                "12erfdydd345"));
    }};

    private final List<Doctor> testDoctors = new ArrayList<>() {{
        add(new Doctor(100, 1, "Doctor 1", Gender.FEMALE, "3333444410"));
        add(new Doctor(101, 1, "Doctor 2", Gender.MALE, "3333444411"));
    }};

    @BeforeEach
    public void fillRepositoryAndConnectedTables() {
        for (var patient : testPatients) {
            patientRepository.add(patient);
        }
        for (var doctor : testDoctors) {
            doctorRepository.addWithId(doctor);
        }
        for (var record : testPatientRecords) {
            patientRecordRepository.add(record);
        }
    }

    @AfterEach
    public void clearRepositoryAndConnectedTables() {
        for (var patient : testPatients) {
            patientRepository.delete(patient);
        }
        for (var doctor : testDoctors) {
            doctorRepository.delete(doctor);
        }
    }

    @Test
    public void repositoryShouldCorrectlyAddPatientRecord() {
        PatientRecord record = new PatientRecord(101, "1111111111111111",
                currentDateTime.plusDays(3), AppointmentType.SCHEDULED);

        Assertions.assertThat(patientRecordRepository.add(record)).isTrue();
        Assertions.assertThatThrownBy(() -> patientRecordRepository.add(record))
                .isInstanceOf(DataRepositoryException.class);
    }

    @Test
    public void repositoryShouldCorrectlyFindAllPatientRecords() {
        List<PatientRecord> allRecords = patientRecordRepository.findAll();

        Assertions.assertThat(allRecords).containsAll(testPatientRecords);
        Assertions.assertThat(allRecords.size()).isGreaterThanOrEqualTo(testPatientRecords.size());
    }

    @Test
    public void repositoryShouldCorrectlyFindAllPatientRecordsByDoctorIdAndInterval() {
        List<PatientRecord> recordsByDoctor = patientRecordRepository.findAllByDoctorIdAndTimeInterval(
                100, currentDateTime, currentDateTime.plusDays(1));
        List<PatientRecord> expectedRecordsByDoctor = new ArrayList<>() {{
            add(testPatientRecords.get(0));
            add(testPatientRecords.get(1));
        }};
        Assertions.assertThat(recordsByDoctor).containsExactlyInAnyOrderElementsOf(expectedRecordsByDoctor);

        recordsByDoctor = patientRecordRepository.findAllByDoctorIdAndTimeInterval(
                999, currentDateTime, currentDateTime.plusDays(1));
        Assertions.assertThat(recordsByDoctor).isEmpty();

        recordsByDoctor = patientRecordRepository.findAllByDoctorIdAndTimeInterval(
                100, currentDateTime.plusDays(2), currentDateTime.plusDays(4));
        Assertions.assertThat(recordsByDoctor).isEmpty();
    }

    @Test
    public void repositoryShouldCorrectlyFindAllPatientRecordsByPatientIdAndInterval() {
        List<PatientRecord> recordsByDoctor = patientRecordRepository.findAllByPatientIdAndTimeInterval(
                "1111111111111111", currentDateTime.plusDays(1));
        List<PatientRecord> expectedRecordsByDoctor = new ArrayList<>() {{
            add(testPatientRecords.get(1));
            add(testPatientRecords.get(2));
        }};
        Assertions.assertThat(recordsByDoctor).containsExactlyInAnyOrderElementsOf(expectedRecordsByDoctor);

        recordsByDoctor = patientRecordRepository.findAllByPatientIdAndTimeInterval(
                "1111111111111112", currentDateTime.plusDays(1));
        Assertions.assertThat(recordsByDoctor).isEmpty();

        recordsByDoctor = patientRecordRepository.findAllByPatientIdAndTimeInterval(
                "1111111111111111", currentDateTime.plusDays(4));
        Assertions.assertThat(recordsByDoctor).isEmpty();
    }

    @Test
    public void repositoryShouldCorrectlyFindPatientRecordByDoctorIdAndDatetime() {
        Optional<PatientRecord> recordByDoctor = patientRecordRepository.findByDoctorIdAndDateTime(
                100, currentDateTime);
        Assertions.assertThat(recordByDoctor.get()).isEqualTo(testPatientRecords.getFirst());

        recordByDoctor = patientRecordRepository.findByDoctorIdAndDateTime(
                100, currentDateTime.plusWeeks(1));
        Assertions.assertThat(recordByDoctor).isEqualTo(Optional.empty());
    }

    @Test
    public void repositoryShouldCorrectlyUpdatePatientRecord() {
        PatientRecord updatingRecord = patientRecordRepository.findByDoctorIdAndDateTime(100,
                currentDateTime).get();
        updatingRecord.setDateTime(currentDateTime.plusDays(4));

        Assertions.assertThat(patientRecordRepository.update(updatingRecord)).isTrue();
        Optional<PatientRecord> updatedRecord = patientRecordRepository.findByDoctorIdAndDateTime(100,
                currentDateTime.plusDays(4));

        Assertions.assertThat(updatedRecord.isPresent()).isTrue();
        Assertions.assertThat(updatedRecord.get().getDateTime()).isEqualToIgnoringNanos(updatingRecord.getDateTime());
    }
}
