package servlets;

import data.domain.mappers.AppointmentTypeMapper;
import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientRecordMapper;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.PatientRecordRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.PatientAppointmentListItemDto;
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
import java.util.List;
import services.PatientRecordService;
import services.exceptions.UnavailableRecordTimeException;

@WebServlet("/patient_profile")
public class PatientProfileServlet extends HttpServlet {
    private PatientRecordService patientRecordService;

    @Override
    public void init() throws ServletException {
        patientRecordService = new PatientRecordService(
                new PatientRecordRepository(new PatientRecordMapper(new AppointmentTypeMapper())),
                new DoctorRepository(new DoctorMapper(new GenderMapper())));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            displayPatientRecords(req, resp);
        } catch (DataRepositoryException e) {
            resp.sendRedirect(req.getContextPath() + "/server_error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String removedRecordId = req.getParameter("removedRecordId");
        Integer recordId = Integer.parseInt(removedRecordId);

        try {
            PatientRecordResultDto removedRecord = patientRecordService.removePatientRecordToTheDoctor(recordId);
            req.setAttribute("removedRecord", removedRecord);
            displayPatientRecords(req, resp);
        } catch (UnavailableRecordTimeException e) {
            resp.sendRedirect(req.getContextPath() + "/patient_profile");
        } catch (DataRepositoryException e) {
            resp.sendRedirect(req.getContextPath() + "/server_error");
        }
    }

    private void displayPatientRecords(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException, DataRepositoryException {
        PatientProfileDto patientProfile = (PatientProfileDto) req.getSession().getAttribute("patient");

        List<PatientAppointmentListItemDto> patientRecords = patientRecordService.getAllPatientRecords(
                patientProfile.insurancePolicyNumber(),
                LocalDateTime.now());

        req.setAttribute("patientRecords", patientRecords);
        RequestDispatcher dispatcher = req.getRequestDispatcher("patient_profile.jsp");
        dispatcher.forward(req, resp);
    }
}
