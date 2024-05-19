package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.mappers.SpecialityMapper;
import data.domain.models.dictionaries.Speciality;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

public class SpecialityRepository {
    @Getter
    private final SpecialityMapper mapper;
    private static final Connection CONNECTION;
    private static final String FIND_ALL_TEMPLATE = """
            SELECT speciality_id, speciality_name
            FROM specialities
            """;
    private static final String FIND_BY_NAME_TEMPLATE = """
            SELECT speciality_id, speciality_name
            FROM specialities
            WHERE speciality_name = ?
            """;

    private static final String FIND_BY_ID_TEMPLATE = """
            SELECT speciality_id, speciality_name
            FROM specialities
            WHERE speciality_id = ?
            """;
    private static final String ADD_TEMPLATE = """
            INSERT INTO specialities(speciality_name)
            VALUES (?)
            """;
    private static final String UPDATE_TEMPLATE = """
            UPDATE specialities
            SET speciality_name = ?
            WHERE speciality_id = ?
            """;
    private static final String DELETE_TEMPLATE = """
            DELETE FROM specialities
            WHERE speciality_id = ?
            """;

    static {
        CONNECTION = ConnectionFactory.getConnection();
    }

    public SpecialityRepository(SpecialityMapper mapper) {
        this.mapper = mapper;
    }

    public List<Speciality> findAll() throws SQLException {
        List<Speciality> specialities = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_ALL_TEMPLATE)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                specialities.add(mapper.mapSpecialityFromDatabase(resultSet));
            }
        }
        return specialities;
    }

    public Optional<Speciality> findByName(Integer id) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_BY_NAME_TEMPLATE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapSpecialityFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional<Speciality> findById(Integer id) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(FIND_BY_ID_TEMPLATE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapSpecialityFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        }
    }

    public boolean add(Speciality speciality) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(ADD_TEMPLATE)) {
            preparedStatement.setString(1, speciality.getSpecialityName());

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean update(Integer id, Speciality speciality) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(UPDATE_TEMPLATE)) {
            preparedStatement.setString(1, speciality.getSpecialityName());
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean delete(Integer id) throws SQLException {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(DELETE_TEMPLATE)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() == 1;
        }
    }
}
