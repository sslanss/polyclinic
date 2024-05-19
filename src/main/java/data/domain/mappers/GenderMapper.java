package data.domain.mappers;

import data.domain.models.dictionaries.Gender;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenderMapper {
    public Gender mapGenderFromDatabase(ResultSet resultSet) throws SQLException {
        String genderString = resultSet.getString("gender");
        return Gender.valueOf(genderString.toUpperCase());
    }

    public Gender mapGenderFromUserForm(String gender) {
        switch (gender) {
            case "male" -> {
                return Gender.MALE;
            }
            case "female" -> {
                return Gender.FEMALE;
            }
            default -> {
                return null;
            }
        }
    }
}
