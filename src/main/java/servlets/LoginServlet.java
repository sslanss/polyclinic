package servlets;

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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import services.PatientService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private PatientService patientService;

    @Override
    public void init() throws ServletException {
        patientService = new PatientService(new PatientRepository(new PatientMapper(new GenderMapper())),
                new AppointmentRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //patientService.loginUser()
        //catch: notRegistered-->setAttribute(error)
        // ErrorDto=new ErrorDto
        // RegistrationServlet


    }

    private void setSession(HttpServletRequest req, HttpSession session) {
        req.getAttribute("insurancePolicyNumber");
        req.getAttribute("password");
        //PatientProfileDto patient = patientService.loginPatient()
        //Patient patient = new Patient();
        //session.setAttribute("user", patient);
    }
}
