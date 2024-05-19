package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import services.PatientRecordService;

@WebServlet("/record")
public class PatientRecordServlet extends HttpServlet {
    private PatientRecordService patientRecordService;

    @Override
    public void init() throws ServletException {
        patientRecordService = new PatientRecordService();
    }

}
