package WebPack;

import Engine.EngineFacade;
import Out.ExecuteResultDTO;
import Server_UTILS.UserData;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "ResumeDebugServlet", urlPatterns = {"/resumeDebug"})
public class ResumeDebugServlet extends BaseServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        int pc = Integer.parseInt(request.getParameter("PC"));
        int architecture = Integer.parseInt(request.getParameter("architecture"));
        boolean isFirstDebugStep =  Boolean.parseBoolean(request.getParameter("isFirstStep"));

        EngineFacade facade = requireFacade(request, response);
        if (facade == null)
        {
            return;
        }

        @SuppressWarnings("unchecked")
        ConcurrentMap<String, UserData> users =
                (ConcurrentMap<String, UserData>) getServletContext().getAttribute("USERS");

        UserData thisUser = users.get(getUsername(request, response));
        int archCost = facade.getCreditCostByArchitecture(architecture);

        if (isFirstDebugStep)
        {
            if (!thisUser.useCredits(archCost))
            {
                response.getWriter().println(GSON.toJson(ExecuteResultDTO.FAILED));
                return;
            }
        }
            ExecuteResultDTO result = facade.resumeDebug(pc, thisUser.getCurrentCredits());
            if (result.isFailed())
            {
                thisUser.takeAllCredits();
                response.getWriter().println(GSON.toJson(ExecuteResultDTO.FAILED));
            }
            else
            {
                thisUser.incrementExecutionCount();
                thisUser.useCredits(result.cycles());
                response.getWriter().println(GSON.toJson(result));
            }
        }

    }
