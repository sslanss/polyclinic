package servlets;

import data.domain.mappers.AppointmentTypeMapper;
import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientRecordMapper;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.PatientRecordRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.PatientProfileDto;
import data.dto.PatientRecordResultDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import services.PatientRecordService;
import services.exceptions.UnavailableRecordTimeException;

@WebServlet("/record")
public class PatientRecordServlet extends HttpServlet {
    private PatientRecordService patientRecordService;

    @Override
    public void init() throws ServletException {
        patientRecordService = new PatientRecordService(
                new PatientRecordRepository(new PatientRecordMapper(new AppointmentTypeMapper())),
                new DoctorRepository(new DoctorMapper(new GenderMapper())));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer doctorId = Integer.valueOf(req.getParameter("doctorId"));
        String dateTime = req.getParameter("dateTime");
        PatientProfileDto patientProfile = (PatientProfileDto) req.getSession().getAttribute("patient");

        try {
            PatientRecordResultDto patientRecord = patientRecordService.makePatientRecordToTheDoctor(doctorId,
                    patientProfile.insurancePolicyNumber(), LocalDateTime.parse(dateTime));

            req.setAttribute("patientRecord", patientRecord);
            RequestDispatcher dispatcher = req.getRequestDispatcher("record.jsp");
            dispatcher.forward(req, resp);
        } catch (UnavailableRecordTimeException e) {
            req.setAttribute("errorMessage", "На выбранную дату уже нельзя записаться."
                    + "Пожалуйста, запишитесь на другое время или к другому врачу.");
        } catch (DataRepositoryException e) {
            resp.sendRedirect(req.getContextPath() + "/server_error");
        }
    }

}
