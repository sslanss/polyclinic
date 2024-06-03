package services;

import data.domain.mappers.PatientRecordMapper;
import data.domain.models.Doctor;
import data.domain.models.PatientRecord;
import data.domain.models.dictionaries.AppointmentType;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.PatientRecordRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.DoctorAvailableTimeDto;
import data.dto.PatientAppointmentListItemDto;
import data.dto.PatientRecordResultDto;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private static final int SESSION_DURATION_IN_MINUTES = 30;

    public PatientRecordService(PatientRecordRepository patientRecordRepository, DoctorRepository doctorRepository) {
        this.patientRecordRepository = patientRecordRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorAvailableTimeDto> getDoctorFreeTimeForInterval(Integer doctorId, LocalDateTime sinceDate,
                                                                     LocalDateTime untilDate)
            throws DataRepositoryException {
        List<PatientRecord> busyRecordsByTimestamp = patientRecordRepository.findAllByDoctorIdAndTimeInterval(
                doctorId, sinceDate, untilDate);
        List<LocalDateTime> busyDateTimes = busyRecordsByTimestamp.stream()
                .map(PatientRecord::getDateTime)
                .toList();

        LocalDateTime firstAvailableTime = getNextAvailableDateTime(sinceDate);
        LocalDateTime lastAvailableTime = getNextAvailableDateTime(untilDate);

        List<DoctorAvailableTimeDto> freeRecordsByTimestamp = new ArrayList<>();
        while (firstAvailableTime.isBefore(lastAvailableTime)) {
            if (!busyDateTimes.contains(firstAvailableTime)) {
                freeRecordsByTimestamp.add(new DoctorAvailableTimeDto(firstAvailableTime));
            }
            firstAvailableTime = getNextAvailableDateTime(firstAvailableTime);
        }
        return freeRecordsByTimestamp;
    }

    public PatientRecordResultDto makePatientRecordToTheDoctor(Integer doctorId, String patientId,
                                                               LocalDateTime appointmentDateTime)
            throws DataRepositoryException {
        PatientRecord addingRecording = new PatientRecord(doctorId, patientId, appointmentDateTime,
                AppointmentType.SCHEDULED);

        Optional<PatientRecord> existedPatientRecord = patientRecordRepository.findByDoctorIdAndDateTime(doctorId,
                appointmentDateTime);
        if (existedPatientRecord.isPresent()) {
            throw new UnavailableRecordTimeException();
        }

        Doctor doctorForRecord = doctorRepository.findById(doctorId).orElseThrow(UnavailableRecordTimeException::new);
        patientRecordRepository.add(addingRecording);

        return patientRecordRepository.getPatientRecordMapper()
                .mapPatientRecordToPatientRecordResultDto(addingRecording, doctorForRecord.getFullName());
    }

    public List<PatientAppointmentListItemDto> getAllPatientRecords(String patientId, LocalDateTime sinceDate)
            throws DataRepositoryException {
        List<PatientRecord> patientRecords = patientRecordRepository.findAllByPatientIdAndTimeInterval(patientId,
                sinceDate);

        PatientRecordMapper mapper = patientRecordRepository.getPatientRecordMapper();
        return patientRecords.stream()
                .map((mapper::mapPatientRecordToPatientAppointmentListItemDto))
                .toList();
    }

    public PatientRecordResultDto removePatientRecordToTheDoctor(Integer recordId) throws DataRepositoryException {
        Optional<PatientRecord> deletingPatientRecord = patientRecordRepository.findById(recordId);

        if (deletingPatientRecord.isEmpty()) {
            throw new UnavailableRecordTimeException();
        }
        if (!patientRecordRepository.delete(recordId)) {
            throw new UnavailableRecordTimeException();
        }

        Integer doctorId = deletingPatientRecord.get().getDoctorId();
        Doctor doctorForRecord = doctorRepository.findById(doctorId).orElseThrow(UnavailableRecordTimeException::new);

        return patientRecordRepository.getPatientRecordMapper()
                .mapPatientRecordToPatientRecordResultDto(deletingPatientRecord.get(), doctorForRecord.getFullName());
    }


    private LocalDateTime getNextAvailableDateTime(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && (time.equals(LAST_DAY_TIME_FOR_RECORDING)
              ||  time.isAfter(LAST_DAY_TIME_FOR_RECORDING))) {
            return LocalDateTime.of(date.plusDays(2), FIRST_DAY_TIME_FOR_RECORDING);
        }
        if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
             ||  time.isAfter(LAST_DAY_TIME_FOR_RECORDING) || time.equals(LAST_DAY_TIME_FOR_RECORDING)) {
            return LocalDateTime.of(date.plusDays(1), FIRST_DAY_TIME_FOR_RECORDING);
        }
        if (time.isBefore(FIRST_DAY_TIME_FOR_RECORDING)) {
            return LocalDateTime.of(date, FIRST_DAY_TIME_FOR_RECORDING);
        }
        if (time.getMinute() < SESSION_DURATION_IN_MINUTES) {
            return dateTime.withMinute(SESSION_DURATION_IN_MINUTES);
        } else {
            return dateTime.plusHours(1).withMinute(0);
        }
    }
}
