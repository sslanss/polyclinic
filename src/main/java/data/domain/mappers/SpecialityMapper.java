package data.domain.mappers;

import data.domain.models.dictionaries.Speciality;
import data.dto.SpecialityDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialityMapper {
    public Speciality mapSpecialityFromDatabase(ResultSet resultSet) throws SQLException {
        return new Speciality(
                resultSet.getInt("speciality_id"),
                resultSet.getString("speciality_name")
        );
    }

    public SpecialityDto mapSpecialityToSpecialityDto(Speciality speciality) {
        return new SpecialityDto(speciality.getSpecialityId(), speciality.getSpecialityName());
    }
}
