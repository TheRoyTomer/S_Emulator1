package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Function;
import Engine.Programs.Program;
import Out.StepOverResult;
import Server_UTILS.ProgramHolderWrapper;
import Server_UTILS.UserData;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

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
        int architecture = Integer.parseInt(request.getParameter("architecture"));
        boolean isFirstDebugStep = Boolean.parseBoolean(request.getParameter("isFirstStep"));

        @SuppressWarnings("unchecked")
        ConcurrentMap<String, UserData> users =
                (ConcurrentMap<String, UserData>) getServletContext().getAttribute("USERS");

        UserData thisUser = users.get(getUsername(request, response));
        int archCost = facade.getCreditCostByArchitecture(architecture);


        if (isFirstDebugStep)
        {
            if (!thisUser.useCredits(archCost))
            {
                response.getWriter().println(GSON.toJson(StepOverResult.FAILED));
                return;
            }
            else
            {
                thisUser.incrementExecutionCount();

            }
        }

        StepOverResult result = facade.breakPoint(startPC, destPC, thisUser.getCurrentCredits());

        if (result.isFailed())
        {
            thisUser.takeAllCredits();
            response.getWriter().println(GSON.toJson(StepOverResult.FAILED));
        }
        else
        {
            Program p = facade.getProgram();

            if (result.isDebugCompleted() && p != null)
            {
                updateInfo(thisUser, facade, result.cycles() + archCost);

            }

            thisUser.useCredits(result.cycles());
            response.getWriter().println(GSON.toJson(result));
        }
    }
}
