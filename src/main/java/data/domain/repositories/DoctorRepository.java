package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.mappers.DoctorMapper;
import data.domain.models.Doctor;
import data.domain.repositories.exceptions.DataRepositoryException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

public class DoctorRepository {
    @Getter
    private final DoctorMapper mapper;
    private final Connection connection;
    private static final String FIND_ALL_TEMPLATE = """
            SELECT doctor_id, speciality_id, full_name, gender, phone_number
            FROM doctors
            """;
    private static final String FIND_BY_ID_TEMPLATE = """
            SELECT doctor_id, speciality_id, full_name, gender, phone_number
            FROM doctors
            WHERE doctor_id = ?
            """;

    private static final String FIND_BY_SPECIALITY_ID_AND_NAME_TEMPLATE= """
            SELECT doctor_id, speciality_id, full_name, gender, phone_number
            FROM doctors
            WHERE speciality_id = ? AND full_name = ?
            """;

    private static final String ADD_TEMPLATE = """
            INSERT INTO doctors(speciality_id, full_name, gender, phone_number)
            VALUES (?, ?, ?::gender, ?)
            """;

    private static final String ADD_WITH_ID_TEMPLATE = """
            INSERT INTO doctors(doctor_id, speciality_id, full_name, gender, phone_number)
            VALUES (?, ?, ?, ?::gender, ?)
            """;

    private static final String UPDATE_SPECIALITY_TEMPLATE = """
            UPDATE doctors
            SET speciality_id = ?
            WHERE doctor_id = ?
            """;
    private static final String DELETE_TEMPLATE = """
            DELETE FROM doctors
            WHERE speciality_id = ? AND full_name = ?
            """;
    private static final String FIND_ALL_BY_SPECIALITY_TEMPLATE = """
            SELECT doctor_id, speciality_id, full_name, gender, phone_number
            FROM doctors
            WHERE speciality_id = ?
            """;

    public DoctorRepository(DoctorMapper mapper) {
        this.mapper = mapper;
        connection = ConnectionFactory.getConnection();
    }

    public List<Doctor> findAll() throws DataRepositoryException {
        List<Doctor> doctors = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TEMPLATE)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                doctors.add(mapper.mapDoctorFromDatabase(resultSet));
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
        return doctors;
    }

    public Optional<Doctor> findById(Integer id) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_TEMPLATE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapDoctorFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public Optional<Doctor> findBySpecialityAndFullName(Integer specialityId, String fullName)
            throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_SPECIALITY_ID_AND_NAME_TEMPLATE)) {
            preparedStatement.setInt(1, specialityId);
            preparedStatement.setString(2, fullName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapDoctorFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean add(Doctor doctor) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_TEMPLATE)) {
            try {
                preparedStatement.setInt(1, doctor.getSpecialityId());
                preparedStatement.setString(2, doctor.getFullName());

                if (doctor.getGender() != null) {
                    preparedStatement.setString(3, String.valueOf(doctor.getGender()).toLowerCase());
                } else {
                    preparedStatement.setNull(3, Types.OTHER);
                }

                preparedStatement.setString(4, doctor.getPhoneNumber());
            } catch (SQLException e) {
                throw new DataRepositoryException();
            }

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean addWithId(Doctor doctor) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_WITH_ID_TEMPLATE)) {
            try {
                preparedStatement.setInt(1, doctor.getDoctorId());
                preparedStatement.setInt(2, doctor.getSpecialityId());
                preparedStatement.setString(3, doctor.getFullName());

                if (doctor.getGender() != null) {
                    preparedStatement.setString(4, String.valueOf(doctor.getGender()).toLowerCase());
                } else {
                    preparedStatement.setNull(4, Types.OTHER);
                }

                preparedStatement.setString(5, doctor.getPhoneNumber());
            } catch (SQLException e) {
                throw new DataRepositoryException();
            }

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean delete(Doctor doctor) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TEMPLATE)) {
            preparedStatement.setInt(1, doctor.getSpecialityId());
            preparedStatement.setString(2, doctor.getFullName());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean updateSpeciality(Doctor doctor) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SPECIALITY_TEMPLATE)) {
            preparedStatement.setInt(1, doctor.getSpecialityId());
            preparedStatement.setInt(2, doctor.getDoctorId());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public List<Doctor> findAllBySpeciality(Integer specialityId) throws DataRepositoryException {
        List<Doctor> doctors = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_SPECIALITY_TEMPLATE)) {
            preparedStatement.setInt(1, specialityId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                doctors.add(mapper.mapDoctorFromDatabase(resultSet));
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
        return doctors;
    }
}
