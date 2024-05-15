package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import services.AppointmentsService;

public class AppointmentServlet extends HttpServlet {
    private AppointmentsService appointmentsService;

    @Override
    public void init() throws ServletException {
        appointmentsService = new AppointmentsService();
    }

}
