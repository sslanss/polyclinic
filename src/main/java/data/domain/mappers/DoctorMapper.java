package data.domain.mappers;

import data.domain.models.Doctor;
import data.domain.models.dictionaries.Speciality;
import data.dto.DoctorListItemDto;
import data.dto.DoctorProfileDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DoctorMapper {

    private final GenderMapper genderMapper;

    public Doctor mapDoctorFromDatabase(ResultSet resultSet) throws SQLException {
        return new Doctor(
                resultSet.getInt("doctor_id"),
                resultSet.getInt("speciality_id"),
                resultSet.getString("full_name"),
                genderMapper.mapGenderFromDatabase(resultSet),
                resultSet.getString("phone_number")
        );
    }

    public DoctorListItemDto mapDoctorToDoctorListItemDto(Doctor doctor) {
        return new DoctorListItemDto(doctor.getDoctorId(), doctor.getFullName());
    }

    public DoctorProfileDto mapDoctorToDoctorProfileDto(Doctor doctor, Speciality speciality) {
        return new DoctorProfileDto(doctor.getDoctorId(), doctor.getFullName(), speciality.getSpecialityName());
    }
}
