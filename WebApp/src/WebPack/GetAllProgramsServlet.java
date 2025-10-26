package WebPack;

import Engine.Programs.Program;
import Out.FunctionSelectorChoiseDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "GetAllProgramsServlet", urlPatterns = {"/getAllPrograms"})
public class GetAllProgramsServlet extends BaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");

        @SuppressWarnings("unchecked")
        ConcurrentMap<String, Program> allPrograms =
                (ConcurrentMap<String, Program>) getServletContext().getAttribute("ALL_PROGRAMS");

        List<FunctionSelectorChoiseDTO> programNames = new ArrayList<>(
                allPrograms
                        .values()
                        .stream()
                        .map(p -> new FunctionSelectorChoiseDTO(p.getName(), p.getName()))
                        .toList());
        response.getWriter().println(GSON.toJson(programNames));
    }
}