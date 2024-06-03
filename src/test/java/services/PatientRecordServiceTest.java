package services;

import data.domain.mappers.AppointmentTypeMapper;
import data.domain.mappers.PatientRecordMapper;
import data.domain.models.Doctor;
import data.domain.models.PatientRecord;
import data.domain.models.dictionaries.AppointmentType;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.PatientRecordRepository;
import data.dto.DoctorAvailableTimeDto;
import data.dto.PatientAppointmentListItemDto;
import data.dto.PatientRecordResultDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.exceptions.UnavailableRecordTimeException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class PatientRecordServiceTest {
    @Mock
    private PatientRecordRepository patientRecordRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private PatientRecordService patientRecordService;

    private final LocalDate testDate = LocalDate.of(2024, 6, 3);
    private final LocalTime testTime = LocalTime.of(8, 0, 0);
    private final Integer testDoctorId = 1;
    private static final LocalTime LAST_DAY_TIME_FOR_RECORDING = LocalTime.of(20, 0, 0);
    private final LocalDateTime testAppointmentDateTime = LocalDateTime.of(testDate, testTime);
    private final String testPatientId = "1111111111111111";
    private final PatientRecord testPatientRecord = new PatientRecord(testDoctorId, testPatientId,
            testAppointmentDateTime,
            AppointmentType.SCHEDULED);
    private final Doctor testDoctor = new Doctor(testDoctorId, 1, "Doctor Name",
            null, null);

    @BeforeEach
    public void mockObjects() {
        MockitoAnnotations.openMocks(this);
        when(patientRecordRepository.getPatientRecordMapper()).thenReturn(new PatientRecordMapper(
                new AppointmentTypeMapper()));
    }

    @Test
    public void testGetDoctorFreeTimeForInterval() {
        when(patientRecordRepository.findAllByDoctorIdAndTimeInterval(testDoctorId, testAppointmentDateTime,
                testAppointmentDateTime.plusHours(12)))
                .thenReturn(new ArrayList<>());

        List<DoctorAvailableTimeDto> expectedAvailableTime = new ArrayList<>();
        LocalTime startTime = LocalTime.of(testTime.getHour(), testTime.plusMinutes(30).getMinute(),
                testTime.getSecond());
        while (startTime.isBefore(LAST_DAY_TIME_FOR_RECORDING) || startTime.equals(LAST_DAY_TIME_FOR_RECORDING)) {
            expectedAvailableTime.add(new DoctorAvailableTimeDto(LocalDateTime.of(testDate, startTime)));
            startTime = startTime.plusMinutes(30);
        }

        List<DoctorAvailableTimeDto> availableTime = patientRecordService.getDoctorFreeTimeForInterval(testDoctorId,
                testAppointmentDateTime, testAppointmentDateTime.plusHours(12));

        assertThat(availableTime).isEqualTo(expectedAvailableTime);
    }

    @Test
    public void testMakePatientRecordToTheDoctor() {
        PatientRecordResultDto expectedRecordResult = new PatientRecordResultDto(testDoctor.getFullName(),
                testAppointmentDateTime);
        when(patientRecordRepository.findByDoctorIdAndDateTime(testDoctorId, testAppointmentDateTime))
                .thenReturn(Optional.empty());
        when(doctorRepository.findById(testDoctorId)).thenReturn(Optional.of(testDoctor));
        when(patientRecordRepository.add(testPatientRecord)).thenReturn(true);

        PatientRecordResultDto recordResult = patientRecordService.makePatientRecordToTheDoctor(
                testDoctorId, testPatientId, testAppointmentDateTime);

        assertThat(recordResult.doctorFullName()).isEqualTo(expectedRecordResult.doctorFullName());
        assertThat(recordResult.dateTime()).isEqualTo(expectedRecordResult.dateTime());
    }

    @Test
    public void testMakePatientRecordToTheDoctorIfTimeUnavailable() {
        when(patientRecordRepository.findByDoctorIdAndDateTime(testDoctorId, testAppointmentDateTime))
                .thenReturn(Optional.of(testPatientRecord));

        assertThatThrownBy(() -> patientRecordService.makePatientRecordToTheDoctor(testDoctorId, testPatientId,
                testAppointmentDateTime)).isInstanceOf(UnavailableRecordTimeException.class);

        when(patientRecordRepository.findByDoctorIdAndDateTime(testDoctorId, testAppointmentDateTime))
                .thenReturn(Optional.empty());
        when(doctorRepository.findById(testDoctorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientRecordService.makePatientRecordToTheDoctor(testDoctorId, testPatientId,
                testAppointmentDateTime)).isInstanceOf(UnavailableRecordTimeException.class);
    }

    @Test
    public void testGetAllPatientRecords() {
        PatientAppointmentListItemDto expectedDto = new PatientAppointmentListItemDto(1,
                testDoctor.getFullName(), testAppointmentDateTime);

        List<PatientRecord> patientRecords = new ArrayList<>() {{
            add(testPatientRecord);
        }};
        patientRecords.getFirst().setDoctorFullName(testDoctor.getFullName());
        when(patientRecordRepository.findAllByPatientIdAndTimeInterval(testPatientId, testAppointmentDateTime))
                .thenReturn(patientRecords);

        List<PatientAppointmentListItemDto> result = patientRecordService.getAllPatientRecords(testPatientId,
                testAppointmentDateTime);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().dateTime()).isEqualTo(expectedDto.dateTime());
        assertThat(result.getFirst().doctorFullName()).isEqualTo(expectedDto.doctorFullName());
    }

    @Test
    public void testRemovePatientRecordToTheDoctor() {
        PatientRecordResultDto expectedRecordResult = new PatientRecordResultDto(testDoctor.getFullName(),
                testAppointmentDateTime);

        when(patientRecordRepository.findById(1)).thenReturn(Optional.of(testPatientRecord));
        when(patientRecordRepository.delete(1)).thenReturn(true);
        when(doctorRepository.findById(testDoctorId)).thenReturn(Optional.of(testDoctor));

        PatientRecordResultDto recordResult = patientRecordService.removePatientRecordToTheDoctor(1);

        assertThat(recordResult.doctorFullName()).isEqualTo(expectedRecordResult.doctorFullName());
        assertThat(recordResult.dateTime()).isEqualTo(expectedRecordResult.dateTime());
    }

    @Test
    public void testRemovePatientRecordToTheDoctorIfRecordNotFound() {
        when(patientRecordRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientRecordService.removePatientRecordToTheDoctor(1))
                .isInstanceOf(UnavailableRecordTimeException.class);

        when(patientRecordRepository.findById(1)).thenReturn(Optional.of(testPatientRecord));
        when(patientRecordRepository.delete(1)).thenReturn(true);
        when(doctorRepository.findById(testDoctorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientRecordService.removePatientRecordToTheDoctor(1))
                .isInstanceOf(UnavailableRecordTimeException.class);

        when(patientRecordRepository.findById(1)).thenReturn(Optional.of(testPatientRecord));
        when(patientRecordRepository.delete(1)).thenReturn(false);

        assertThatThrownBy(() -> patientRecordService.removePatientRecordToTheDoctor(1))
                .isInstanceOf(UnavailableRecordTimeException.class);
    }
}

