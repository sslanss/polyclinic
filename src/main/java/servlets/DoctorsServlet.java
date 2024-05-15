package servlets;

import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.SpecialityMapper;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.SpecialityRepository;
import data.dto.DoctorListItemDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import services.DoctorsService;

@WebServlet("/doctors")
public class DoctorsServlet extends HttpServlet {

    private DoctorsService doctorsService;

    @Override
    public void init() throws ServletException {
        doctorsService = new DoctorsService(new DoctorRepository(new DoctorMapper(new GenderMapper())),
                new SpecialityRepository(new SpecialityMapper()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<DoctorListItemDto> doctorsList = doctorsService.getAllDoctorItems();
        req.setAttribute("doctorsList", doctorsList);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/doctors.jsp");
        dispatcher.forward(req, resp);
    }

}
