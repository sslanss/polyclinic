package data.domain.mappers;

import data.domain.ClassMappingException;
import data.domain.models.dictionaries.Speciality;
import data.dto.SpecialityDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialityMapper {
    public Speciality mapSpecialityFromDatabase(ResultSet resultSet) {
        try {
            return new Speciality(
                    resultSet.getInt("speciality_id"),
                    resultSet.getString("speciality_name")
            );
        } catch (SQLException e) {
            throw new ClassMappingException();
        }
    }

    public SpecialityDto mapSpecialityToSpecialityDto(Speciality speciality) {
        return new SpecialityDto(speciality.getSpecialityId(), speciality.getSpecialityName());
    }
}
