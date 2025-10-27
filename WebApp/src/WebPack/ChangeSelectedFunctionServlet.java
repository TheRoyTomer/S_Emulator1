
package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Function;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import Server_UTILS.FunctionHolderWrapper;

    @WebServlet(name = "ChangeSelectedFunctionServlet", urlPatterns = {"/changeSelectedFunction"})
    public class ChangeSelectedFunctionServlet extends BaseServlet
    {
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, IOException
        {
            response.setContentType("application/json;charset=UTF-8");

            EngineFacade facade = requireFacade(request, response);
            if (facade == null) return;

            String functionName = request.getParameter("functionName");

            @SuppressWarnings("unchecked")
            ConcurrentMap<String, FunctionHolderWrapper> allFunctions =
                    (ConcurrentMap<String, FunctionHolderWrapper>) getServletContext().getAttribute("ALL_FUNCTIONS");

            FunctionHolderWrapper selectedWrapper = allFunctions.get(functionName);
            if (selectedWrapper != null)
            {
                Function selected = selectedWrapper.getFunction();
                Function selectedDuplicated = selected.duplicateFunction();
                selectedDuplicated.initProgram();
                facade.loadExistingProgram(selectedDuplicated);
                response.getWriter().println("{\"status\":\"success\"}");
            } else {
                response.setStatus(404);
                response.getWriter().println("{\"error\":\"Function not found\"}");
            }
        }
    }

