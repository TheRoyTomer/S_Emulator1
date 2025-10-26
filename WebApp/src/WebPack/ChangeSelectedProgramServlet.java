package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Program;
import Out.ViewResultDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "ChangeSelectedProgramServlet", urlPatterns = {"/changeSelectedProgram"})
public class ChangeSelectedProgramServlet extends BaseServlet
{
  /*  @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        response.setContentType("application/json;charset=UTF-8");
        String programName = request.getParameter("Name");
        EngineFacade facade = requireFacade(request, response);
        if (facade == null) {
            return;
        }
        ViewResultDTO result = facade.changeSelectedProgram(programName);
        response.getWriter().println(GSON.toJson(result));
    }*/

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        EngineFacade facade = requireFacade(request, response);
        if (facade == null) return;

        String programName = request.getParameter("programName");

        ConcurrentMap<String, Program> allPrograms =
                (ConcurrentMap<String, Program>) getServletContext().getAttribute("ALL_PROGRAMS");

        Program selected = allPrograms.get(programName);
        if (selected != null)
        {
            facade.loadExistingProgram(selected);
            response.getWriter().println("{\"status\":\"success\"}");
        } else {
            response.setStatus(404);
            response.getWriter().println("{\"error\":\"Program not found\"}");
        }
    }
}
