package services;

import data.domain.models.Doctor;
import data.domain.models.dictionaries.Speciality;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.SpecialityRepository;
import data.dto.DoctorListItemDto;
import data.dto.DoctorProfileDto;
import data.dto.SpecialityDto;
import java.sql.SQLException;
import java.util.List;

public class DoctorsService {
    private final DoctorRepository doctorRepository;

    private final SpecialityRepository specialityRepository;

    public DoctorsService(DoctorRepository doctorRepository, SpecialityRepository specialityRepository) {
        this.doctorRepository = doctorRepository;
        this.specialityRepository = specialityRepository;
    }

    public List<SpecialityDto> getAllSpecialities() {
        try {
            return specialityRepository.findAll().stream()
                    .map(speciality -> specialityRepository.getMapper().mapSpecialityToSpecialityDto(speciality))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DoctorListItemDto> getAllDoctorItems() {
        try {
            return doctorRepository.findAll().stream()
                    .map(doctor -> doctorRepository.getMapper().mapDoctorToDoctorListItemDto(doctor))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DoctorListItemDto> getDoctorsBySpeciality(Integer specialityId) {
        try {
            return doctorRepository.findAllBySpeciality(specialityId).stream()
                    .map(doctor -> doctorRepository.getMapper().mapDoctorToDoctorListItemDto(doctor))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DoctorProfileDto getById(Integer doctorId) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow();

            Speciality speciality = specialityRepository.findById(doctor.getSpecialityId())
                    .orElseThrow();
            return doctorRepository.getMapper().mapDoctorToDoctorProfileDto(doctor, speciality);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
