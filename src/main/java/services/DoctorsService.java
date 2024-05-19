package services;

import data.domain.models.Doctor;
import data.domain.models.dictionaries.Speciality;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.SpecialityRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.DoctorListItemDto;
import data.dto.DoctorProfileDto;
import data.dto.SpecialityDto;
import java.util.List;

public class DoctorsService {
    private final DoctorRepository doctorRepository;

    private final SpecialityRepository specialityRepository;

    public DoctorsService(DoctorRepository doctorRepository, SpecialityRepository specialityRepository) {
        this.doctorRepository = doctorRepository;
        this.specialityRepository = specialityRepository;
    }

    public List<SpecialityDto> getAllSpecialities() throws DataRepositoryException {
        return specialityRepository.findAll().stream()
                .map(speciality -> specialityRepository.getMapper().mapSpecialityToSpecialityDto(speciality))
                .toList();
    }

    public List<DoctorListItemDto> getAllDoctorItems() throws DataRepositoryException {
        return doctorRepository.findAll().stream()
                .map(doctor -> doctorRepository.getMapper().mapDoctorToDoctorListItemDto(doctor))
                .toList();
    }

    public List<DoctorListItemDto> getDoctorsBySpeciality(Integer specialityId) throws DataRepositoryException {
        return doctorRepository.findAllBySpeciality(specialityId).stream()
                .map(doctor -> doctorRepository.getMapper().mapDoctorToDoctorListItemDto(doctor))
                .toList();
    }

    public DoctorProfileDto getById(Integer doctorId) throws DataRepositoryException {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow();

        Speciality speciality = specialityRepository.findById(doctor.getSpecialityId())
                .orElseThrow();
        return doctorRepository.getMapper().mapDoctorToDoctorProfileDto(doctor, speciality);
    }
}
