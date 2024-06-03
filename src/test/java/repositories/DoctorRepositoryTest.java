package repositories;

import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.models.Doctor;
import data.domain.models.dictionaries.Gender;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DoctorRepositoryTest {
    private final DoctorRepository doctorRepository = new DoctorRepository(new DoctorMapper(new GenderMapper()));

    private final List<Doctor> testDoctorRecords = new ArrayList<>() {{
        add(new Doctor(1, "Doctor 1", Gender.FEMALE, "3333444410"));
        add(new Doctor(1, "Doctor 2", Gender.MALE, "3333444411"));
        add(new Doctor(2, "Doctor 3", Gender.MALE, "3333444412"));
    }};

    @BeforeEach
    public void fillRepository() {
        for (var doctor : testDoctorRecords) {
            doctorRepository.add(doctor);
        }
    }

    @AfterEach
    public void clearRepository() {
        for (var doctor : testDoctorRecords) {
            doctorRepository.delete(doctor);
        }
    }

    @Test
    public void repositoryShouldCorrectlyAddDoctor() throws SQLException {
        Doctor doctor = new Doctor(1, "Doctor 4", Gender.FEMALE,
                "3333444413");

        Assertions.assertThat(doctorRepository.add(doctor)).isTrue();
        testDoctorRecords.add(doctor);
        Assertions.assertThatThrownBy(() -> doctorRepository.add(doctor)).isInstanceOf(DataRepositoryException.class);
    }

    @Test
    public void repositoryShouldCorrectlyFindAllDoctors() throws SQLException {
        List<Doctor> allDoctors = doctorRepository.findAll();

        Assertions.assertThat(allDoctors).containsAll(testDoctorRecords);
        Assertions.assertThat(allDoctors.size()).isGreaterThanOrEqualTo(testDoctorRecords.size());
    }

    @Test
    public void repositoryShouldCorrectlyFindAllDoctorsBySpeciality() {
        List<Doctor> allDoctorsBySpeciality = doctorRepository.findAllBySpeciality(1);
        List<Doctor> expectedAllDoctorsBySpeciality = new ArrayList<>() {{
            add(testDoctorRecords.get(0));
            add(testDoctorRecords.get(1));
        }};

        Assertions.assertThat(allDoctorsBySpeciality).containsAll(expectedAllDoctorsBySpeciality);
    }

    @Test
    public void repositoryShouldCorrectlyDeleteDoctors() {
        Doctor deletingDoctor = testDoctorRecords.getFirst();

        Assertions.assertThat(doctorRepository.delete(deletingDoctor)).isTrue();
        Assertions.assertThat(doctorRepository.delete(deletingDoctor)).isFalse();
    }

    @Test
    public void repositoryShouldCorrectlyUpdateDoctor() {
        Doctor updatingDoctor = doctorRepository.findBySpecialityAndFullName(
                testDoctorRecords.getFirst().getSpecialityId(), testDoctorRecords.getFirst().getFullName()).get();
        testDoctorRecords.getFirst().setSpecialityId(2);
        updatingDoctor.setSpecialityId(2);

        Assertions.assertThat(doctorRepository.updateSpeciality(updatingDoctor)).isTrue();
        Assertions.assertThat(doctorRepository.findAllBySpeciality(2).contains(updatingDoctor)).isTrue();
    }

}
