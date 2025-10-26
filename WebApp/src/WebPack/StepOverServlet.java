package WebPack;

import Engine.EngineFacade;
import Out.ExecuteResultDTO;
import Out.StepOverResult;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "StepOverServlet", urlPatterns = {"/stepOver"})
public class StepOverServlet extends BaseServlet
{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        long pc = Long.parseLong(request.getParameter("PC"));


        EngineFacade facade = requireFacade(request, response);
        if (facade == null)
        {
            return;
        }

        StepOverResult result = facade.stepOver(pc);
        response.getWriter().println(GSON.toJson(result));
    }
}
