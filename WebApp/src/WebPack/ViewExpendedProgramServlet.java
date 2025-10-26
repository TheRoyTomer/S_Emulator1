package WebPack;

import Engine.EngineFacade;
import Out.ViewResultDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "ViewExpendedProgramServlet", urlPatterns = {"/viewExpendedProgram"})
public class ViewExpendedProgramServlet extends BaseServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        int deg = Integer.parseInt(request.getParameter("degree"));

        EngineFacade facade = requireFacade(request, response);
        if (facade == null)
        {
            return;
        }

        ViewResultDTO result = facade.viewExpandedProgram(deg);
        response.getWriter().println(GSON.toJson(result));
    }
}
