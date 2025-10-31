package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Function;
import Engine.Programs.Program;
import Out.ExecuteResultDTO;
import Server_UTILS.ProgramHolderWrapper;
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

@WebServlet(name = "ExecuteProgramServlet", urlPatterns = {"/executeProgram"})
public class ExecuteProgramServlet extends BaseServlet
{
  /*  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json;charset=UTF-8");
        int deg = Integer.parseInt(request.getParameter("degree"));
        int architecture = Integer.parseInt(request.getParameter("architecture"));

        String[] inputParams = request.getParameterValues("inputs");
        List<Long> inputs = (inputParams == null)
                ? List.of()
                : Arrays.stream(inputParams)
                .map(Long::parseLong)
                .toList();

        EngineFacade facade = requireFacade(request, response);

        if (facade == null) {return;}

        @SuppressWarnings("unchecked")
        ConcurrentMap<String, UserData> users =
                (ConcurrentMap<String, UserData>) getServletContext().getAttribute("USERS");

        UserData thisUser = users.get(getUsername(request, response));
        int archCost = facade.getCreditCostByArchitecture(architecture);

        if (!thisUser.useCredits(archCost))
        {
            response.getWriter().println(GSON.toJson(ExecuteResultDTO.FAILED));
            return;
        }

        ExecuteResultDTO result = facade.executeProgram(deg, inputs, thisUser.getCurrentCredits());
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
    }*/
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException

    {
        response.setContentType("application/json;charset=UTF-8");

        try {
            String degParam = request.getParameter("degree");
            String archParam = request.getParameter("architecture");

            if (degParam == null)
            {
                System.err.println("ERROR: degree parameter is NULL!");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("{\"error\":\"degree parameter is missing\"}");
                return;
            }

            if (archParam == null) {
                System.err.println("ERROR: architecture parameter is NULL!");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("{\"error\":\"architecture parameter is missing\"}");
                return;
            }

            int deg = Integer.parseInt(degParam);
            int architecture = Integer.parseInt(archParam);


            String[] inputParams = request.getParameterValues("inputs");

            List<Long> inputs = (inputParams == null)
                    ? List.of()
                    : Arrays.stream(inputParams)
                    .map(Long::parseLong)
                    .toList();

            EngineFacade facade = requireFacade(request, response);

            if (facade == null) {
                System.err.println("ERROR: Facade is NULL!");
                return;
            }


            @SuppressWarnings("unchecked")
            ConcurrentMap<String, UserData> users =
                    (ConcurrentMap<String, UserData>) getServletContext().getAttribute("USERS");

            String username = getUsername(request, response);

            UserData thisUser = users.get(username);

            if (thisUser == null)
            {
                System.err.println("ERROR: UserData is NULL!");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("{\"error\":\"User not found\"}");
                return;
            }

            int archCost = facade.getCreditCostByArchitecture(architecture);

            if (!thisUser.useCredits(archCost))
            {
                response.getWriter().println(GSON.toJson(ExecuteResultDTO.FAILED));
                return;
            }

            ExecuteResultDTO result = facade.executeProgram(deg, inputs, thisUser.getCurrentCredits(), architecture);

            if (result.isFailed())
            {
                thisUser.takeAllCredits();
                response.getWriter().println(GSON.toJson(ExecuteResultDTO.FAILED));
            }
            else
            {
                if (facade.getRecentExecutionStatistics() != null)
                {
                    thisUser.getUserHistory().add(facade.getRecentExecutionStatistics());
                }
                updateInfo(thisUser, facade, result.cycles() + archCost);
                thisUser.useCredits(result.cycles());
                response.getWriter().println(GSON.toJson(result));
            }


        } catch (NumberFormatException e) {
            System.err.println("ERROR: NumberFormatException - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"error\":\"Invalid number format: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.err.println("ERROR: Exception in ExecuteProgramServlet - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"error\":\"Internal server error: " + e.getMessage() + "\"}");
        }
    }
}
