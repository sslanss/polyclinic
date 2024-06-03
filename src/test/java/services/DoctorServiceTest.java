package services;

import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.SpecialityMapper;
import data.domain.models.Doctor;
import data.domain.models.dictionaries.Gender;
import data.domain.models.dictionaries.Speciality;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.SpecialityRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.DoctorListItemDto;
import data.dto.DoctorProfileDto;
import data.dto.SpecialityDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private SpecialityRepository specialityRepository;
    @InjectMocks
    private DoctorService doctorService;
    private final Integer testSpecialityId = 1;

    private final List<Speciality> testSpecialities = List.of(
            new Speciality(1, "Speciality 1"),
            new Speciality(2, "Speciality 2"),
            new Speciality(3, "Speciality 3"));

    private final List<Doctor> testDoctors = new ArrayList<>() {{
        add(new Doctor(1, "Doctor 1", Gender.FEMALE, "3333444410"));
        add(new Doctor(1, "Doctor 2", Gender.MALE, "3333444411"));
        add(new Doctor(1, "Doctor 3", Gender.MALE, "3333444412"));
    }};

    @BeforeEach
    void mockObjects() {
        MockitoAnnotations.openMocks(this);
        when(specialityRepository.getMapper()).thenReturn(new SpecialityMapper());
        when(doctorRepository.getMapper()).thenReturn(new DoctorMapper(new GenderMapper()));
    }

    @Test
    void testGetAllSpecialities() throws DataRepositoryException {
        when(specialityRepository.findAll()).thenReturn(testSpecialities);

        List<SpecialityDto> allSpecialities = doctorService.getAllSpecialities();

        Assertions.assertEquals(allSpecialities.size(), testSpecialities.size());
        for (int i = 0; i < allSpecialities.size(); i++) {
            Assertions.assertEquals(allSpecialities.get(i).specialityName(), testSpecialities.get(i)
                    .getSpecialityName());
        }
    }

    @Test
    void testGetDoctorsBySpeciality() throws DataRepositoryException {
        when(doctorRepository.findAllBySpeciality(testSpecialityId)).thenReturn(testDoctors);

        List<DoctorListItemDto> doctorsBySpeciality = doctorService.getDoctorsBySpeciality(testSpecialityId);

        Assertions.assertEquals(doctorsBySpeciality.size(), testDoctors.size());
        for (int i = 0; i < doctorsBySpeciality.size(); i++) {
            Assertions.assertEquals(doctorsBySpeciality.get(i).doctorFullName(),
                    testDoctors.get(i).getFullName());
        }

    }

    @Test
    void testGetById() throws DataRepositoryException {
        Doctor testDoctor = testDoctors.getFirst();
        Speciality testSpeciality = testSpecialities.getFirst();
        Integer testDoctorId = 1;
        when(doctorRepository.findById(testDoctorId)).thenReturn(Optional.of(testDoctor));
        when(specialityRepository.findById(testSpecialityId)).thenReturn(Optional.of(testSpeciality));

        DoctorProfileDto doctorProfileDto = doctorService.getById(testDoctorId);

        Assertions.assertEquals(doctorProfileDto.fullName(), testDoctor.getFullName());
        Assertions.assertEquals(doctorProfileDto.speciality(), testSpeciality.getSpecialityName());
    }
}
