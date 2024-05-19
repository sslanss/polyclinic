package data.domain.repositories;

import data.database.ConnectionFactory;
import data.domain.mappers.SpecialityMapper;
import data.domain.models.dictionaries.Speciality;
import data.domain.repositories.exceptions.DataRepositoryException;
import lombok.Getter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialityRepository {
    @Getter
    private final SpecialityMapper mapper;
    private final Connection connection;
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

    public SpecialityRepository(SpecialityMapper mapper) {
        this.mapper = mapper;
        connection = ConnectionFactory.getConnection();
    }

    public List<Speciality> findAll() throws DataRepositoryException {
        List<Speciality> specialities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_TEMPLATE)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                specialities.add(mapper.mapSpecialityFromDatabase(resultSet));
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
        return specialities;
    }

    public Optional<Speciality> findByName(Integer id) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_TEMPLATE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapSpecialityFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public Optional<Speciality> findById(Integer id) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_TEMPLATE)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapper.mapSpecialityFromDatabase(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean add(Speciality speciality) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_TEMPLATE)) {
            preparedStatement.setString(1, speciality.getSpecialityName());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean update(Integer id, Speciality speciality) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            preparedStatement.setString(1, speciality.getSpecialityName());
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }

    public boolean delete(Integer id) throws DataRepositoryException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TEMPLATE)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataRepositoryException();
        }
    }
}
