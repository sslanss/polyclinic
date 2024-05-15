package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.ClassMappingException;
import data.domain.mappers.DoctorMapper;
import data.domain.models.Doctor;
import lombok.Getter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorRepository {
    @Getter
    private final DoctorMapper mapper;
    private static final Connection CONNECTION;
    private static final String FIND_ALL_TEMPLATE = """
            SELECT doctor_id, speciality_id, full_name, gender, phone_number
            FROM doctors
            """;
    private static final String FIND_BY_ID_TEMPLATE = """
            SELECT doctor_id, speciality_id, full_name, gender, phone_number
            FROM doctors
            WHERE doctor_id = ?
            """;
    private static final String ADD_TEMPLATE = """
            INSERT INTO doctors(speciality_id, full_name, gender, phone_number)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SPECIALITY_TEMPLATE = """
            UPDATE doctors
            SET speciality_id = ?
            WHERE doctor_id = ?
            """;
    private static final String DELETE_TEMPLATE = """
            DELETE FROM doctors
            WHERE doctor_id = ?
            """;
    private static final String FIND_ALL_BY_SPECIALITY_TEMPLATE = """
            SELECT doctor_id, speciality_id, full_name, gender, phone_number
            FROM doctors
            WHERE speciality_id = ?
            """;

    static {
        CONNECTION = ConnectionFactory.getConnection();
    }

    public DoctorRepository(DoctorMapper mapper) {
        this.mapper = mapper;
    }

    public List<Doctor> findAll() throws ClassMappingException, SQLException {
        List<Doctor> doctors = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_ALL_TEMPLATE)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                doctors.add(mapper.mapDoctorFromDatabase(resultSet));
            }
        }
        return doctors;
    }

    public Optional<Doctor> findById(Integer id) throws ClassMappingException, SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_BY_ID_TEMPLATE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapDoctorFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        }
    }

    public boolean add(Doctor doctor) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(ADD_TEMPLATE)) {
            try {
                preparedStatement.setInt(1, doctor.getSpecialityId());
                preparedStatement.setString(2, doctor.getFullName());
                preparedStatement.setString(3, String.valueOf(doctor.getGender()).toLowerCase());
                preparedStatement.setString(4, doctor.getPhoneNumber());
            } catch (SQLException e) {
                throw new ClassMappingException();
            }

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean delete(Integer id) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(DELETE_TEMPLATE)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public void updateSpeciality(Integer id, Doctor doctor) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(UPDATE_SPECIALITY_TEMPLATE)) {
            preparedStatement.setInt(1, doctor.getSpecialityId());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        }
    }

    public List<Doctor> findAllBySpeciality(Integer id) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_ALL_BY_SPECIALITY_TEMPLATE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                doctors.add(mapper.mapDoctorFromDatabase(resultSet));
            }
        }
        return doctors;
    }
}
