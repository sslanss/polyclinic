package servlets;

import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.SpecialityMapper;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.SpecialityRepository;
import data.dto.DoctorProfileDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import services.DoctorsService;

@WebServlet("/doctors/*")
public class DoctorProfileServlet extends HttpServlet {
    private DoctorsService doctorsService;

    @Override
    public void init() throws ServletException {
        doctorsService = new DoctorsService(new DoctorRepository(new DoctorMapper(new GenderMapper())),
                new SpecialityRepository(new SpecialityMapper()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String doctorId = req.getParameter("id");
        if (doctorId == null || doctorId.isEmpty()) {
            // Если параметр id отсутствует или пуст, возвращаем ошибку
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        DoctorProfileDto doctor = doctorsService.getById(Integer.valueOf(doctorId));
        if (doctor == null) {
            // Доктор с указанным идентификатором не найден
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("doctor", doctor);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/doctor_profile.jsp");
        dispatcher.forward(req, resp);
    }
}
