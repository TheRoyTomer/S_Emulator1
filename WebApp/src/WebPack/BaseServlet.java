package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Function;
import Engine.Programs.Program;
import Server_UTILS.FunctionHolderWrapper;
import Server_UTILS.ProgramHolderWrapper;
import Server_UTILS.ServerConstants;
import Server_UTILS.UserData;
import jakarta.servlet.http.*;

import java.util.concurrent.ConcurrentMap;

public abstract class BaseServlet extends HttpServlet {

    protected EngineFacade requireFacade(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession s = req.getSession(false);
        if (s == null) {
            write401(resp);
            return null;
        }
        EngineFacade f = (EngineFacade) s.getAttribute(ServerConstants.SESSION_FACADE_KEY);
        if (f == null) {
            write401(resp);
            return null;
        }
        return f;
    }

    private void write401(HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"unauthorized\"}");
        } catch (Exception ignore) {}
    }

    protected String getUsername(HttpServletRequest req, HttpServletResponse resp)
    {
        HttpSession session = req.getSession(false);
        if (session != null)
        {
            return (String) session.getAttribute("username");
        }
        return null;
    }

    public void updateInfo(UserData user, EngineFacade userFacade, int creditCost)
    {


        Program p = userFacade.getProgram();

        if (p == null) {return;}

        if (p instanceof Function)
        {
            @SuppressWarnings("unchecked")
            ConcurrentMap<String, FunctionHolderWrapper> allFunctions =
                    (ConcurrentMap<String, FunctionHolderWrapper>) getServletContext().getAttribute("ALL_FUNCTIONS");
            FunctionHolderWrapper funcWrapper = allFunctions.get(p.getName());

            if (funcWrapper == null) {return;}

            funcWrapper.addTotalCreditCost(creditCost);
            funcWrapper.IncreaseExecutions();
        }
        else
        {
            @SuppressWarnings("unchecked")
            ConcurrentMap<String, ProgramHolderWrapper> allPrograms =
                    (ConcurrentMap<String, ProgramHolderWrapper>) getServletContext().getAttribute("ALL_PROGRAMS");

                user.incrementExecutionCount();
                ProgramHolderWrapper thisProgram = allPrograms.get(p.getName());
                thisProgram.IncreaseExecutions();
                thisProgram.addTotalCreditCost(creditCost);
        }
    }

}

