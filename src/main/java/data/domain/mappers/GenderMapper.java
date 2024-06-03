package data.domain.mappers;

import data.domain.models.dictionaries.Gender;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenderMapper {
    public Gender mapGenderFromDatabase(ResultSet resultSet) throws SQLException {
        String genderString = resultSet.getString("gender");
        try {
            return Gender.valueOf(genderString.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    public Gender mapGenderFromUserForm(String gender) {
        if (gender != null) {
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
        return  null;
    }
}
