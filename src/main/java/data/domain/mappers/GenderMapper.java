package data.domain.mappers;

import data.domain.ClassMappingException;
import data.domain.models.dictionaries.Gender;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenderMapper {
    public Gender mapGenderFromDatabase(ResultSet resultSet) throws SQLException {
        String genderString = resultSet.getString("gender");
        return Gender.valueOf(genderString.toUpperCase());
    }

    public void mapGenderToDatabase(int parameterIndex, Gender gender, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setString(parameterIndex, String.valueOf(gender).toLowerCase());

        } catch (SQLException e) {
            throw new ClassMappingException();
        }
    }
}
