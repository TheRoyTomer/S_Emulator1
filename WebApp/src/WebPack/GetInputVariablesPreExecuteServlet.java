package WebPack;

import Engine.EngineFacade;
import EngineObject.VariableDTO;
import Out.ViewResultDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "GetInputVariablesPreExecuteServlet", urlPatterns = {"/getInputVariablesPreExecute"})
public class GetInputVariablesPreExecuteServlet extends BaseServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");

        EngineFacade facade = requireFacade(request, response);
        if (facade == null)
        {
            return;
        }

        List<VariableDTO> result = facade.getInputVariablesPreExecute();
        response.getWriter().println(GSON.toJson(result));
    }
}
