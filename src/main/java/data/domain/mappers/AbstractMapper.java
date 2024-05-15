package data.domain.mappers;

import java.sql.ResultSet;

public interface AbstractMapper<T> {
    T mapEntityFromDatabase(ResultSet resultSet);
}
