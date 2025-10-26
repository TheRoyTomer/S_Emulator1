package WebPack;

import Engine.EngineFacade;
import EngineObject.StatisticDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "getHistoryServlet", urlPatterns = {"/getHistory"})

public class getHistoryServlet extends BaseServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        response.setContentType("application/json;charset=UTF-8");

        EngineFacade facade = requireFacade(request, response);
        if (facade == null)
        {
            return;
        }

        List<StatisticDTO> res = facade.getHistory();
        response.getWriter().println(GSON.toJson(res));
    }

}
