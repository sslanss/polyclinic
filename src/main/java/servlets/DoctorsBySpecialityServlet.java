package servlets;

import data.domain.mappers.DoctorMapper;
import data.domain.mappers.GenderMapper;
import data.domain.mappers.SpecialityMapper;
import data.domain.repositories.DoctorRepository;
import data.domain.repositories.SpecialityRepository;
import data.domain.repositories.exceptions.DataRepositoryException;
import data.dto.DoctorListItemDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import services.DoctorService;

@WebServlet("/doctors")
public class DoctorsBySpecialityServlet extends HttpServlet {
    private DoctorService doctorService;

    @Override
    public void init() throws ServletException {
        doctorService = new DoctorService(new DoctorRepository(new DoctorMapper(new GenderMapper())),
                new SpecialityRepository(new SpecialityMapper()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer specialityId = Integer.valueOf(req.getParameter("specialityId"));

        try {
            List<DoctorListItemDto> doctorsBySpecialityList = doctorService.getDoctorsBySpeciality(specialityId);

            req.setAttribute("doctorsBySpecialityList", doctorsBySpecialityList);
            RequestDispatcher dispatcher = req.getRequestDispatcher("doctors_by_specialities.jsp");
            dispatcher.forward(req, resp);
        } catch (DataRepositoryException e) {
            resp.sendRedirect(req.getContextPath() + "/server_error");
        }
    }
}
