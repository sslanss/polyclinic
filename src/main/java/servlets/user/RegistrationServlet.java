package servlets.user;

import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientMapper;
import data.domain.repositories.AppointmentRepository;
import data.domain.repositories.PatientRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.PatientService;
import utils.password.PasswordHasher;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private PatientService patientService;

    @Override
    public void init() throws ServletException {
        patientService = new PatientService(new PatientRepository(new PatientMapper(new GenderMapper())),
                new PasswordHasher(), new AppointmentRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("registration.jsp");
        dispatcher.forward(req, resp);
    }

    //if session user
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String insurancePolicyNumber = req.getParameter("insurancePolicyNumber");
        String fullName = req.getParameter("fullName");
        String dateOfBirth = req.getParameter("dateOfBirth");
        String gender = req.getParameter("gender");
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");
        String password = req.getParameter("password");
        patientService.registerPatient(insurancePolicyNumber, fullName, dateOfBirth, gender, phoneNumber, address, password);
    }


}
