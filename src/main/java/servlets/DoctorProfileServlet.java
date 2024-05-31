package servlets;

import data.domain.mappers.AppointmentTypeMapper;
import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientRecordMapper;
import data.domain.mappers.SpecialityMapper;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.PatientRecordRepository;
import data.domain.repositories.SpecialityRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.DoctorAvailableTimeDto;
import data.dto.DoctorProfileDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import services.DoctorService;
import services.PatientRecordService;
import services.exceptions.DoctorUnavailableException;

@WebServlet("/doctor")
public class DoctorProfileServlet extends HttpServlet {
    private DoctorService doctorService;

    private PatientRecordService patientRecordService;

    private static final int WEEKS_COUNT_FOR_RECORD = 2;

    @Override
    public void init() throws ServletException {
        doctorService = new DoctorService(new DoctorRepository(new DoctorMapper(new GenderMapper())),
                new SpecialityRepository(new SpecialityMapper()));
        patientRecordService = new PatientRecordService(
                new PatientRecordRepository(new PatientRecordMapper(new AppointmentTypeMapper())),
                new DoctorRepository(new DoctorMapper(new GenderMapper())));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer doctorId = Integer.valueOf(req.getParameter("id"));

        try {
            DoctorProfileDto doctor = doctorService.getById(doctorId);
            req.setAttribute("doctor", doctor);

            LocalDateTime currentDate = LocalDateTime.now();
            List<DoctorAvailableTimeDto> doctorBusyTimeForTimestamp = patientRecordService
                    .getDoctorFreeTimeForInterval(doctorId, currentDate,
                            currentDate.plusWeeks(WEEKS_COUNT_FOR_RECORD));

            req.setAttribute("freeTime", doctorBusyTimeForTimestamp);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/doctor_profile.jsp");
            dispatcher.forward(req, resp);
        } catch (DoctorUnavailableException e) {
            resp.sendRedirect(req.getContextPath() + "/doctors");
        } catch (DataRepositoryException e) {
            resp.sendRedirect(req.getContextPath() + "/server_error");
        }
    }
}
