package WebPack;

import Engine.EngineFacade;
import Out.StepOverResult;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "BreakPointServlet", urlPatterns = {"/breakPoint"})

public class BreakPointServlet extends BaseServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");

        EngineFacade facade = requireFacade(request, response);
        if (facade == null) {
            return;
        }

        long startPC = Long.parseLong(request.getParameter("startPC"));
        long destPC = Long.parseLong(request.getParameter("destPC"));

        StepOverResult result = facade.breakPoint(startPC, destPC);
        response.getWriter().println(GSON.toJson(result));
    }
}
