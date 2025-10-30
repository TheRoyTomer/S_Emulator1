package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Program;
import Out.ViewResultDTO;
import Server_UTILS.ProgramHolderWrapper;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        EngineFacade facade = requireFacade(request, response);
        if (facade == null) return;

        String programName = request.getParameter("programName");
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, ProgramHolderWrapper> allPrograms =
                (ConcurrentMap<String, ProgramHolderWrapper>) getServletContext().getAttribute("ALL_PROGRAMS");

        ProgramHolderWrapper programWrapper = allPrograms.get(programName);

        if (programWrapper != null)
        {
            Program selected = programWrapper.getProgram();
            Program selectedDuplicated = selected.duplicate();
            selectedDuplicated.initProgram();
            facade.loadExistingProgram(selectedDuplicated);
            response.getWriter().println("{\"status\":\"success\"}");
        } else {
            response.setStatus(404);
            response.getWriter().println("{\"error\":\"Program not found\"}");
        }

    }
}
