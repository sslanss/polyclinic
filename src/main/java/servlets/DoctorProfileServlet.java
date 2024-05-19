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
import data.dto.DoctorFreeTimeForRecordDto;
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
import services.DoctorsService;
import services.PatientRecordService;

@WebServlet("/doctor")
public class DoctorProfileServlet extends HttpServlet {
    private DoctorsService doctorsService;

    private PatientRecordService patientRecordService;

    @Override
    public void init() throws ServletException {
        doctorsService = new DoctorsService(new DoctorRepository(new DoctorMapper(new GenderMapper())),
                new SpecialityRepository(new SpecialityMapper()));
        patientRecordService = new PatientRecordService(
                new PatientRecordRepository(new PatientRecordMapper(new AppointmentTypeMapper())),
                new DoctorRepository(new DoctorMapper(new GenderMapper())));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer doctorId = Integer.valueOf(req.getParameter("id"));

        try {
            DoctorProfileDto doctor = doctorsService.getById(doctorId);
            if (doctor == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            req.setAttribute("doctor", doctor);

            LocalDateTime currentDate = LocalDateTime.now();
            List<DoctorFreeTimeForRecordDto> doctorBusyTimeForTimestamp = patientRecordService
                    .getDoctorFreeTimeForInterval(doctorId, currentDate,
                            currentDate.plusWeeks(2));
            req.setAttribute("freeTime", doctorBusyTimeForTimestamp);

            RequestDispatcher dispatcher = req.getRequestDispatcher("/doctor_profile.jsp");
            dispatcher.forward(req, resp);
        } catch (DataRepositoryException e) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("server_error.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
