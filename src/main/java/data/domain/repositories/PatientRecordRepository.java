package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.mappers.PatientRecordMapper;
import data.domain.models.PatientRecord;
import data.domain.repositories.exceptions.DataRepositoryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void update(PatientRecord patientRecord) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DATE_TIME_TEMPLATE)) {
            preparedStatement.setObject(1, patientRecord.getDateTime());
            preparedStatement.setInt(2, patientRecord.getRecordId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }
}
