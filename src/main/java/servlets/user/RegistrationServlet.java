package servlets.user;

import data.domain.mappers.GenderMapper;
import data.domain.mappers.PatientMapper;
import data.domain.repositories.PatientRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.PatientProfileDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import services.PatientService;
import services.exceptions.InvalidFieldsException;
import utils.password.PasswordHasher;
import utils.validation.validators.model_validators.PatientValidator;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private PatientService patientService;

    @Override
    public void init() throws ServletException {
        patientService = new PatientService(new PatientRepository(new PatientMapper(new GenderMapper())),
                new PasswordHasher(), new PatientValidator());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("registration.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String insurancePolicyNumber = req.getParameter("insurancePolicyNumber");
        String fullName = req.getParameter("fullName");
        String dateOfBirth = req.getParameter("dateOfBirth");
        String gender = req.getParameter("gender");
        String phoneNumber = req.getParameter("phoneNumber");
        String address = req.getParameter("address");
        String password = req.getParameter("password");

        try {
            PatientProfileDto patientProfile = patientService.registerPatient(insurancePolicyNumber, fullName,
                    dateOfBirth, gender, phoneNumber, address,
                    password);
            req.getSession().setAttribute("patient", patientProfile);
            RequestDispatcher dispatcher = req.getRequestDispatcher("successful_authorization.jsp");
            dispatcher.forward(req, resp);
        } catch (DataRepositoryException e) {
            resp.sendRedirect(req.getContextPath() + "/server_error");
        } catch (InvalidFieldsException e) {
            req.setAttribute("errorMessage", "Вы допустили ошибки в заполнении формы. "
                  +  "Пожалуйста, заполните информацию верно.");
            req.setAttribute("errorFields", e.getErrorFields());
            RequestDispatcher dispatcher = req.getRequestDispatcher("registration.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
