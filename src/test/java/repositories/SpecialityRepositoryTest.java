package repositories;

import data.domain.mappers.SpecialityMapper;
import data.domain.models.dictionaries.Speciality;
import data.domain.repositories.SpecialityRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpecialityRepositoryTest {

    private final SpecialityRepository specialityRepository = new SpecialityRepository(new SpecialityMapper());

    private final String testSpecialityName = "Speciality 4";

    private final List<Speciality> testSpecialityRecords = new ArrayList<>() {{
        add(new Speciality("Speciality 1"));
        add(new Speciality("Speciality 2"));
        add(new Speciality("Speciality 3"));
    }};

    @BeforeEach
    public void fillRepository() {
        for (var speciality : testSpecialityRecords) {
            specialityRepository.add(speciality);
        }
    }

    @AfterEach
    public void clearRepository() {
        for (var speciality : testSpecialityRecords) {
            specialityRepository.delete(speciality);
        }
    }

    @Test
    public void repositoryShouldCorrectlyAddSpeciality() {
        Speciality speciality = new Speciality(testSpecialityName);

        Assertions.assertThat(specialityRepository.add(speciality)).isTrue();
        testSpecialityRecords.add(speciality);
        Assertions.assertThatThrownBy(() -> specialityRepository.add(speciality))
                .isInstanceOf(DataRepositoryException.class);
    }

    @Test
    public void repositoryShouldCorrectlyFindAllSpecialities() {
        List<Speciality> allSpecialities = specialityRepository.findAll();

        Assertions.assertThat(allSpecialities).containsAll(testSpecialityRecords);
        Assertions.assertThat(allSpecialities.size()).isGreaterThanOrEqualTo(testSpecialityRecords.size());
    }

    @Test
    public void repositoryShouldCorrectlyDeleteSpeciality() {
        Speciality deletingSpeciality = testSpecialityRecords.get(0);

        Assertions.assertThat(specialityRepository.delete(deletingSpeciality)).isTrue();
        Assertions.assertThat(specialityRepository.delete(deletingSpeciality)).isFalse();
    }

    @Test
    public void repositoryShouldCorrectlyUpdateSpeciality() throws DataRepositoryException {
        Speciality updatingSpeciality = specialityRepository.findByName(testSpecialityRecords.getFirst()
                .getSpecialityName()).get();
        updatingSpeciality.setSpecialityName(testSpecialityName + ".1");
        testSpecialityRecords.getFirst().setSpecialityName(testSpecialityName + ".1");

        Assertions.assertThat(specialityRepository.update(updatingSpeciality)).isTrue();
        Optional<Speciality> updatedSpeciality = specialityRepository.findById(updatingSpeciality.getSpecialityId());

        Assertions.assertThat(updatedSpeciality).isPresent();
        Assertions.assertThat(updatedSpeciality.get().getSpecialityName()).isEqualTo(testSpecialityName + ".1");
    }
}
