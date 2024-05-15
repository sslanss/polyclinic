package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.ClassMappingException;
import data.domain.mappers.PatientMapper;
import data.domain.models.Patient;
import lombok.Getter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepository {
    @Getter
    private final PatientMapper mapper;
    private static final Connection CONNECTION;
    private static final String FIND_ALL_TEMPLATE = """
            SELECT insurance_policy_number, full_name, date_of_birth, gender, phone_number, address, password
            FROM patients
            """;
    private static final String FIND_BY_NUMBER_TEMPLATE = """
            SELECT insurance_policy_number, full_name, date_of_birth, gender, phone_number, address,  password
            FROM patients
            WHERE insurance_policy_number = ?
            """;
    private static final String ADD_TEMPLATE = """
            INSERT INTO patients(insurance_policy_number, full_name, date_of_birth, gender, phone_number, address)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_ADDRESS_TEMPLATE = """
            UPDATE patients
            SET address = ?
            WHERE insurance_policy_number = ?
            """;
    private static final String DELETE_TEMPLATE = """
            DELETE FROM patients
            WHERE insurance_policy_number = ?
            """;

    static {
        CONNECTION = ConnectionFactory.getConnection();
    }

    public PatientRepository(PatientMapper mapper) {
        this.mapper = mapper;
    }

    public List<Patient> findAll() throws ClassMappingException, SQLException {
        List<Patient> patients = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_ALL_TEMPLATE)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                patients.add(mapper.mapPatientFromDatabase(resultSet));
            }
        }
        return patients;
    }

    public Optional<Patient> findByPolicyNumber(String insurancePolicyNumber) throws ClassMappingException,
            SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_BY_NUMBER_TEMPLATE)) {
            preparedStatement.setString(1, insurancePolicyNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapPatientFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        }
    }

    public boolean add(Patient patient) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(ADD_TEMPLATE)) {
            try {
                preparedStatement.setString(1, patient.getInsurancePolicyNumber());
                preparedStatement.setString(2, patient.getFullName());
                preparedStatement.setDate(3, Date.valueOf(patient.getDateOfBirth()));
                preparedStatement.setString(4, String.valueOf(patient.getGender()).toLowerCase());
                preparedStatement.setString(5, patient.getPhoneNumber());
                preparedStatement.setString(6, patient.getAddress());
            } catch (SQLException e) {
                throw new ClassMappingException();
            }

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean delete(String insurancePolicyNumber) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(DELETE_TEMPLATE)) {
            preparedStatement.setString(1, insurancePolicyNumber);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public void updateAddress(String insurancePolicyNumber, String newAddress) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(UPDATE_ADDRESS_TEMPLATE)) {
            preparedStatement.setString(1, newAddress);
            preparedStatement.setString(2, insurancePolicyNumber);

            preparedStatement.executeUpdate();
        }
    }
}
