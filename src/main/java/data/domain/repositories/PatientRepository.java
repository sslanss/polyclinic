package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.mappers.PatientMapper;
import data.domain.models.Patient;
import data.domain.repositories.exceptions.DataRepositoryException;
import lombok.Getter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepository {
    @Getter
    private final PatientMapper patientMapper;
    private final Connection connection;
    private static final String FIND_ALL_TEMPLATE = """
            SELECT insurance_policy_number, full_name, date_of_birth, gender, phone_number, address, password
            FROM patients
            """;
    private static final String FIND_BY_NUMBER_TEMPLATE = """
            SELECT insurance_policy_number, full_name, date_of_birth, gender, phone_number, address, password
            FROM patients
            WHERE insurance_policy_number = ?
            """;
    private static final String FIND_BY_NUMBER_AND_PASSWORD_TEMPLATE = """
            SELECT insurance_policy_number, full_name, date_of_birth, gender, phone_number, address, password
            FROM patients
            WHERE insurance_policy_number = ? AND password = ?
            """;

    private static final String ADD_TEMPLATE = """
            INSERT INTO patients(insurance_policy_number, full_name, date_of_birth, gender, phone_number, address,
            password)
            VALUES (?, ?, ?, ?::gender, ?, ?, ?)
            """;
    private static final String UPDATE_PASSWORD_TEMPLATE = """
            UPDATE patients
            SET password = ?
            WHERE insurance_policy_number = ?
            """;
    private static final String DELETE_TEMPLATE = """
            DELETE FROM patients
            WHERE insurance_policy_number = ?
            """;

    public PatientRepository(PatientMapper patientMapper) {
        this.patientMapper = patientMapper;
        connection = ConnectionFactory.getConnection();
    }

    public List<Patient> findAll() throws DataRepositoryException {
        List<Patient> patients = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TEMPLATE)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                patients.add(patientMapper.mapPatientFromDatabase(resultSet));
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
        return patients;
    }

    public Optional<Patient> findByPolicyNumber(String insurancePolicyNumber) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NUMBER_TEMPLATE)) {
            preparedStatement.setString(1, insurancePolicyNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(patientMapper.mapPatientFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public Optional<Patient> findByPolicyNumberAndPassword(String insurancePolicyNumber, String password)
            throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NUMBER_AND_PASSWORD_TEMPLATE)) {
            preparedStatement.setString(1, insurancePolicyNumber);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(patientMapper.mapPatientFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean add(Patient patient) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_TEMPLATE)) {
                preparedStatement.setString(1, patient.getInsurancePolicyNumber());
                preparedStatement.setString(2, patient.getFullName());
                preparedStatement.setDate(3, Date.valueOf(patient.getDateOfBirth()));

            if (patient.getGender() != null) {
                preparedStatement.setString(4, String.valueOf(patient.getGender()).toLowerCase());
            } else {
                preparedStatement.setNull(4, Types.OTHER);
            }

                preparedStatement.setString(5, patient.getPhoneNumber());
                preparedStatement.setString(6, patient.getAddress());
                preparedStatement.setString(7, patient.getPassword());
            return preparedStatement.executeUpdate() == 1;
            } catch (SQLException e) {
                throw new DataRepositoryException();
            }
    }

    public boolean delete(String insurancePolicyNumber) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TEMPLATE)) {
            preparedStatement.setString(1, insurancePolicyNumber);

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public void updatePassword(String insurancePolicyNumber, String password) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD_TEMPLATE)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, insurancePolicyNumber);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }
}
