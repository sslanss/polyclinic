package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.mappers.PatientRecordMapper;
import data.domain.models.PatientRecord;
import data.domain.repositories.exceptions.DataRepositoryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

public class PatientRecordRepository {
    @Getter
    private final PatientRecordMapper patientRecordMapper;
    private final Connection connection;

    private static final String FIND_ALL_TEMPLATE = """
            SELECT record_id, doctor_id, patient_id, date_time, appointment_type
            FROM patient_records
            """;

    private static final String FIND_BY_ID_TEMPLATE = """
            SELECT record_id, doctor_id, patient_id, date_time, appointment_type
            FROM patient_records
            WHERE record_id = ?
            """;

    private static final String FIND_BY_DOCTOR_ID_AND_TIME_INTERVAL_TEMPLATE = """
            SELECT record_id, doctor_id, patient_id, date_time, appointment_type
            FROM patient_records
            WHERE doctor_id = ?
              AND date_time BETWEEN ? AND ?
            """;

    private static final String FIND_BY_DOCTOR_ID_AND_DATE_TIME_TEMPLATE = """
            SELECT record_id, doctor_id, patient_id, date_time, appointment_type
            FROM patient_records
            WHERE doctor_id = ?
              AND date_time = ?
            """;

    private static final String FIND_BY_PATIENT_ID_AND_TIME_INTERVAL_TEMPLATE = """
            SELECT PR.record_id, PR.doctor_id, PR.patient_id, PR.date_time, PR.appointment_type, D.full_name AS
            doctor_full_name
            FROM patient_records PR
                     JOIN doctors D ON PR.doctor_id = D.doctor_id
            WHERE PR.patient_id = ? AND PR.date_time >= ?
            """;

    private static final String ADD_TEMPLATE = """
            INSERT INTO patient_records(doctor_id, patient_id, date_time, appointment_type)
            VALUES (?, ?, ?, ?::appointment_type)
            """;

    private static final String DELETE_TEMPLATE = """
            DELETE FROM patient_records
            WHERE record_id = ?
            """;

    private static final String UPDATE_DATE_TIME_TEMPLATE = """
            UPDATE patient_records
            SET date_time = ?
            WHERE record_id = ?
            """;

    public PatientRecordRepository(PatientRecordMapper patientRecordMapper) {
        this.patientRecordMapper = patientRecordMapper;
        this.connection = ConnectionFactory.getConnection();
    }

    public List<PatientRecord> findAll() throws DataRepositoryException {
        List<PatientRecord> records = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TEMPLATE)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                records.add(patientRecordMapper.mapPatientRecordFromDatabase(resultSet));
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
        return records;
    }

    public Optional<PatientRecord> findById(Integer recordId) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_TEMPLATE)) {
            preparedStatement.setInt(1, recordId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(patientRecordMapper.mapPatientRecordFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public Optional<PatientRecord> findByDoctorIdAndDateTime(Integer doctorId, LocalDateTime dateTime)
            throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                FIND_BY_DOCTOR_ID_AND_DATE_TIME_TEMPLATE)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setObject(2, dateTime);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(patientRecordMapper.mapPatientRecordFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public List<PatientRecord> findAllByDoctorIdAndTimeInterval(Integer doctorId, LocalDateTime since,
                                                               LocalDateTime until) throws DataRepositoryException {
        List<PatientRecord> records = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                FIND_BY_DOCTOR_ID_AND_TIME_INTERVAL_TEMPLATE)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setObject(2, since);
            preparedStatement.setObject(3, until);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                records.add(patientRecordMapper.mapPatientRecordFromDatabase(resultSet));
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
        return records;
    }


    public List<PatientRecord> findAllByPatientIdAndTimeInterval(String patientId, LocalDateTime since)
            throws DataRepositoryException {
        List<PatientRecord> records = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                FIND_BY_PATIENT_ID_AND_TIME_INTERVAL_TEMPLATE)) {
            preparedStatement.setString(1, patientId);
            preparedStatement.setObject(2, since);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                records.add(patientRecordMapper.mapPatientRecordWithDoctorFullNameFromDatabase(resultSet));
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
        return records;
    }

    public boolean add(PatientRecord patientRecord) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_TEMPLATE)) {
            preparedStatement.setInt(1, patientRecord.getDoctorId());
            preparedStatement.setString(2, patientRecord.getPatientId());
            preparedStatement.setObject(3, patientRecord.getDateTime());
            preparedStatement.setString(4, String.valueOf(patientRecord.getAppointmentType()).toLowerCase());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean delete(Integer recordId) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TEMPLATE)) {
            preparedStatement.setInt(1, recordId);

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean update(PatientRecord patientRecord) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DATE_TIME_TEMPLATE)) {
            preparedStatement.setObject(1, patientRecord.getDateTime());
            preparedStatement.setInt(2, patientRecord.getRecordId());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }
}
