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
import services.exceptions.InvalidPasswordValue;
import services.exceptions.PatientHaveNotRegisteredYet;
import utils.password.PasswordHasher;
import utils.validation.validators.model_validators.PatientValidator;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private PatientService patientService;

    @Override
    public void init() throws ServletException {
        patientService = new PatientService(new PatientRepository(new PatientMapper(new GenderMapper())),
                new PasswordHasher(), new PatientValidator());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String insurancePolicyNumber = req.getParameter("insurancePolicyNumber");
        String password = req.getParameter("password");

        try {
            PatientProfileDto patientProfile = patientService.loginPatient(insurancePolicyNumber, password);
            req.getSession().setAttribute("patient", patientProfile);
            forwardToPageWithAnAttribute(req, resp, "successful_authorization.jsp", "patient", patientProfile);
        } catch (DataRepositoryException e) {
            resp.sendRedirect(req.getContextPath() + "/server_error");
        } catch (PatientHaveNotRegisteredYet e) {
            forwardToPageWithAnAttribute(req, resp, "registration.jsp", "errorMessage",
                    "Вы еще не зарегистрированы. Пожалуйста, зарегистрируйтесь.");
        } catch (InvalidPasswordValue e) {
            forwardToPageWithAnAttribute(req, resp, "login.jsp", "errorMessage",
                    "Пароль неверный! Попробуйте еще раз.");
        }
    }

    private void forwardToPageWithAnAttribute(HttpServletRequest req, HttpServletResponse resp,
                                              String page, String attributeName, Object attribute)
            throws ServletException, IOException {
        if (attribute != null) {
            req.setAttribute(attributeName, attribute);
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
        dispatcher.forward(req, resp);
    }
}
