package services;

import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientMapper;
import data.domain.models.Patient;
import data.domain.models.dictionaries.Gender;
import data.domain.repositories.PatientRepository;
import data.dto.PatientProfileDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import services.exceptions.InvalidPasswordValue;
import services.exceptions.PatientHaveAlreadyRegistered;
import services.exceptions.PatientHaveNotRegisteredYet;
import utils.password.PasswordHasher;
import utils.validation.validators.model_validators.PatientValidator;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientValidator patientValidator;
    @Mock
    private PasswordHasher passwordHasher;
    @InjectMocks
    private PatientService patientService;
    private final String testPatientNumber = "1111111111111112";
    private final String testPassword = "erqq2d6d44";
    private final Patient testPatient = new Patient(testPatientNumber, "Patient 2",
            LocalDate.of(1990, 2, 2), Gender.FEMALE, null, null,
            testPassword);

    @BeforeEach
    public void mockObjects() {
        MockitoAnnotations.openMocks(this);
        when(patientRepository.getPatientMapper()).thenReturn(new PatientMapper(new GenderMapper()));
        when(patientValidator.validate(any())).thenReturn(new ArrayList<>());
    }

    @Test
    public void testLoginCorrectPatient() {
        when(patientRepository.findByPolicyNumber(testPatientNumber))
                .thenReturn(Optional.of(testPatient));
        when(passwordHasher.check(testPassword, testPatient.getPassword())).thenReturn(true);

        PatientProfileDto patientProfile = patientService.loginPatient(testPatient.getInsurancePolicyNumber(),
                testPassword);

        Assertions.assertThat(comparePatientProfileDto(patientProfile)).isTrue();
    }

    @Test
    public void testLoginIncorrectPatient() {
        when(patientRepository.findByPolicyNumber(testPatientNumber))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> patientService.loginPatient(testPatient.getInsurancePolicyNumber(),
                testPassword)).isInstanceOf(PatientHaveNotRegisteredYet.class);

        testPatient.setPassword(null);
        when(patientRepository.findByPolicyNumber(testPatientNumber))
                .thenReturn(Optional.of(testPatient));

        Assertions.assertThatThrownBy(() -> patientService.loginPatient(testPatient.getInsurancePolicyNumber(),
                testPassword)).isInstanceOf(PatientHaveNotRegisteredYet.class);

        testPatient.setPassword(testPassword);
        when(patientRepository.findByPolicyNumber(testPatientNumber))
                .thenReturn(Optional.of(testPatient));
        when(passwordHasher.check(testPassword, testPatient.getPassword())).thenReturn(false);
        Assertions.assertThatThrownBy(() -> patientService.loginPatient(testPatient.getInsurancePolicyNumber(),
                testPassword)).isInstanceOf(InvalidPasswordValue.class);
    }

    @Test
    public void testRegisterIncorrectPatient() {
        when(patientRepository.findByPolicyNumber(testPatientNumber))
                .thenReturn(Optional.of(testPatient));
        Assertions.assertThatThrownBy(() -> patientService.registerPatient(testPatient.getInsurancePolicyNumber(),
                testPatient.getFullName(), testPatient.getDateOfBirth().toString(), null, null,
                null, testPatient.getPassword())).isInstanceOf(PatientHaveAlreadyRegistered.class);
    }

    @Test
    public void testRegisterNewCorrectPatient() {
        Patient loginedPatient = testPatient;
        loginedPatient.setPassword(null);
        when(patientRepository.findByPolicyNumber(testPatientNumber))
                .thenReturn(Optional.of(loginedPatient));

        PatientProfileDto patientProfile = patientService.registerPatient(loginedPatient.getInsurancePolicyNumber(),
                loginedPatient.getFullName(), loginedPatient.getDateOfBirth().toString(), null, null,
                null, loginedPatient.getPassword());

        Assertions.assertThat(comparePatientProfileDto(patientProfile)).isTrue();
    }

    @Test
    public void testRegisterOnsiteVisitedCorrectPatient() {
        when(patientRepository.findByPolicyNumber(testPatientNumber))
                .thenReturn(Optional.empty());
        when(passwordHasher.hash(testPassword)).thenReturn(testPassword);

        PatientProfileDto patientProfile = patientService.registerPatient(testPatient.getInsurancePolicyNumber(),
                testPatient.getFullName(), testPatient.getDateOfBirth().toString(), "female", null,
                null, testPatient.getPassword());

        Assertions.assertThat(comparePatientProfileDto(patientProfile)).isTrue();
    }

    private boolean comparePatientProfileDto(PatientProfileDto patientProfileDto) {
        return patientProfileDto.insurancePolicyNumber().equals(testPatient.getInsurancePolicyNumber()) &&
                Objects.equals(patientProfileDto.fullName(), testPatient.getFullName()) &&
                Objects.equals(patientProfileDto.dateOfBirth(), testPatient.getDateOfBirth()) &&
                Objects.equals(patientProfileDto.gender(), testPatient.getGender()) &&
                Objects.equals(patientProfileDto.phoneNumber(), testPatient.getPhoneNumber()) &&
                Objects.equals(patientProfileDto.address(), testPatient.getAddress());

    }
}
