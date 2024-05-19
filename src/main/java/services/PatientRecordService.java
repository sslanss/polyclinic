package services;

import data.domain.models.Doctor;
import data.domain.models.PatientRecord;
import data.domain.models.dictionaries.AppointmentType;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.PatientRecordRepository;
import data.dto.AddedPatientRecordDto;
import data.dto.DoctorFreeTimeForRecordDto;
import data.dto.RemovedPatientRecordDto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import services.exceptions.UnavailableRecordTimeException;

public class PatientRecordService {
    private final PatientRecordRepository patientRecordRepository;
    private final DoctorRepository doctorRepository;

    private static final LocalTime FIRST_DAY_TIME_FOR_RECORDING = LocalTime.of(8, 0, 0);
    private static final LocalTime LAST_DAY_TIME_FOR_RECORDING = LocalTime.of(20, 0, 0);

    public PatientRecordService(PatientRecordRepository patientRecordRepository, DoctorRepository doctorRepository) {
        this.patientRecordRepository = patientRecordRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorFreeTimeForRecordDto> getDoctorFreeTimeForInterval(Integer doctorId, LocalDateTime sinceDate,
                                                                         LocalDateTime untilDate) {
        List<PatientRecord> busyRecordsByTimestamp = patientRecordRepository.findAllByDoctorIdAndTimeInterval(
                doctorId, sinceDate, untilDate);
        List<LocalDateTime> busyDateTimes = busyRecordsByTimestamp.stream()
                .map(PatientRecord::getDateTime)
                .toList();

        LocalDateTime firstAvailableTime = getNextAvailableTime(sinceDate);
        LocalDateTime lastAvailableTime = getNextAvailableTime(untilDate);

        List<DoctorFreeTimeForRecordDto> freeRecordsByTimestamp = new ArrayList<>();
        while (firstAvailableTime.isBefore(lastAvailableTime)) {

            if (!busyDateTimes.contains(firstAvailableTime)) {
                freeRecordsByTimestamp.add(new DoctorFreeTimeForRecordDto(firstAvailableTime));
            }
            if (firstAvailableTime.toLocalTime().isBefore(LAST_DAY_TIME_FOR_RECORDING)) {
                firstAvailableTime = firstAvailableTime.plusMinutes(30);
            } else {
                firstAvailableTime = LocalDateTime.of(firstAvailableTime.toLocalDate().plusDays(1),
                        FIRST_DAY_TIME_FOR_RECORDING);
            }
        }
        return freeRecordsByTimestamp;
    }

    public AddedPatientRecordDto makePatientRecordToTheDoctor(Integer doctorId, String patientId,
                                                              LocalDateTime appointmentDateTime) {
        PatientRecord recordingPatient = new PatientRecord(doctorId, patientId, appointmentDateTime,
                AppointmentType.SCHEDULED);

        Optional<PatientRecord> existedPatientRecord = patientRecordRepository.findByDoctorIdAndDateTime(doctorId,
                appointmentDateTime);
        if (existedPatientRecord.isPresent()) {
            throw new UnavailableRecordTimeException();
        }

        patientRecordRepository.add(recordingPatient);
        Doctor doctorForRecord = doctorRepository.findById(doctorId).orElseThrow(UnavailableRecordTimeException::new);

        return patientRecordRepository.getPatientRecordMapper()
                .mapPatientRecordToAddedPatientRecordDto(recordingPatient, doctorForRecord.getFullName());
    }

    public RemovedPatientRecordDto removePatientRecordToTheDoctor(Integer doctorId, String patientId,
                                                                  LocalDateTime appointmentDateTime) {
        return null;
    }


    private LocalDateTime getNextAvailableTime(LocalDateTime time) {
        if (time.toLocalTime().isAfter(LAST_DAY_TIME_FOR_RECORDING)) {
            return LocalDateTime.of(time.toLocalDate().plusDays(1), FIRST_DAY_TIME_FOR_RECORDING);
        }
        if (time.toLocalTime().isBefore(FIRST_DAY_TIME_FOR_RECORDING)) {
            return LocalDateTime.of(time.toLocalDate(), LAST_DAY_TIME_FOR_RECORDING);
        }
        if (time.getMinute() < 30) {
            return time.withMinute(30);
        } else {
            return time.plusHours(1).withMinute(0);
        }
    }
}
