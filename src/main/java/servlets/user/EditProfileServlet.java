package servlets.user;

import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientMapper;
import data.domain.repositories.AppointmentRepository;
import data.domain.repositories.PatientRepository;
import utils.password.PasswordHasher;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.PatientService;

import java.io.IOException;

@WebServlet("/edit_profile")
public class EditProfileServlet extends HttpServlet {
    private PatientService patientService;

    @Override
    public void init() throws ServletException {
        patientService = new PatientService(new PatientRepository(new PatientMapper(new GenderMapper())),
                new PasswordHasher(),  new AppointmentRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("edit_profile.jsp");
        dispatcher.forward(req, resp);
    }
}
