package WebPack;

import Engine.EngineFacade;
import Server_UTILS.ServerConstants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "GetMaxDegreeServlet", urlPatterns = {"/GetMaxDegree"})
public class GetMaxDegreeServlet extends BaseServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        response.setContentType("application/json;charset=UTF-8");

        EngineFacade facade = requireFacade(request, response);
        if (facade == null)
        {
            return;
        }

        int maxDegree = facade.getMaxDegree();
        response.getWriter().println("{\"maxDegree\": " + maxDegree + "}");
    }
}
