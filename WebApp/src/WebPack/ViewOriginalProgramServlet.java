package WebPack;

import Engine.EngineFacade;
import Out.ViewResultDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static Server_UTILS.ServerConstants.GSON;
@WebServlet(name = "ViewOriginalProgramServlet", urlPatterns = {"/viewProgram"})
public class ViewOriginalProgramServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        response.setContentType("application/json;charset=UTF-8");

        EngineFacade facade = requireFacade(request, response);
        if (facade == null)
        {
            return;
        }

        ViewResultDTO result = facade.viewProgram();
        response.getWriter().println(GSON.toJson(result));
    }
}
