package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Program;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletResponse;
import Server_UTILS.ProgramHolderWrapper;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@WebServlet(name = "ChangeSelectedProgramServlet", urlPatterns = {"/changeSelectedProgram"})
public class ChangeSelectedProgramServlet extends BaseServlet
{
    protected void doPost(jakarta.servlet.http.HttpServletRequest request,
                          jakarta.servlet.http.HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        EngineFacade facade = requireFacade(request, response);
        if (facade == null) return;

        final int degree = parseIntOrDefault(request.getParameter("degree"), 1); // NEW

        String programName = request.getParameter("programName");
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, ProgramHolderWrapper> allPrograms =
                (ConcurrentMap<String, ProgramHolderWrapper>) getServletContext().getAttribute("ALL_PROGRAMS");

        ProgramHolderWrapper programWrapper = allPrograms.get(programName);

        if (programWrapper != null)
        {
            Program selected = programWrapper.getProgram();
            Program selectedDuplicated = selected.duplicate();

            // === החדשות: נצמד ל-degree שביקש הלקוח ומכין הרחבות לפני טעינה לפסאדה ===
            selectedDuplicated.setMaxDegree(degree);
            selectedDuplicated.initProgram();
            // =======================================================================

            facade.loadExistingProgram(selectedDuplicated);
            response.getWriter().println("{\"status\":\"success\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"error\":\"Program not found\"}");
        }
    }

    private static int parseIntOrDefault(String s, int defVal) {
        try {
            return (s == null || s.isBlank()) ? defVal : Integer.parseInt(s.trim());
        } catch (Exception e) {
            return defVal;
        }
    }
}
