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

@WebServlet("/specialities/*")
public class DoctorsBySpecialityServlet extends HttpServlet {
    private DoctorsService doctorsService;

    @Override
    public void init() throws ServletException {
        doctorsService = new DoctorsService(new DoctorRepository(new DoctorMapper(new GenderMapper())),
                new SpecialityRepository(new SpecialityMapper()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer specialityId;

        try {
            specialityId = parseSpecialityIdFromUrl(req.getPathInfo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        List<DoctorListItemDto> doctorsList = doctorsService.getDoctorsBySpeciality(specialityId);
        req.setAttribute("doctorsList", doctorsList);
        //req.setAttribute("speciality", );

        RequestDispatcher dispatcher = req.getRequestDispatcher("/doctors.jsp");
        dispatcher.forward(req, resp);
    }

    //1.попробую без паттерна.
    //2.c
    private Integer parseSpecialityIdFromUrl(String pathInfo) {
        if (pathInfo != null) {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                try {
                    return Integer.valueOf(parts[1]);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e); //
                }
            }
        }
        return 0;
    }
}
