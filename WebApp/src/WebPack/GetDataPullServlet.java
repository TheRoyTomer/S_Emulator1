package WebPack;

import Out.FunctionInfoDTO;
import Out.ProgramInfoDTO;
import Out.UpdateDataDTO;
import Out.UserInfoDTO;
import Server_UTILS.FunctionHolderWrapper;
import Server_UTILS.ProgramHolderWrapper;
import Server_UTILS.UserData;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "GetDataPullServlet", urlPatterns = {"/getDataPull"})

public class GetDataPullServlet extends BaseServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("application/json; charset=UTF-8");

        // Get all programs from ServletContext
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, ProgramHolderWrapper> allPrograms =
                (ConcurrentMap<String, ProgramHolderWrapper>) getServletContext().getAttribute("ALL_PROGRAMS");

        // Get all functions from ServletContext
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, FunctionHolderWrapper> allFunctions =
                (ConcurrentMap<String, FunctionHolderWrapper>) getServletContext().getAttribute("ALL_FUNCTIONS");

        // Get all users from ServletContext
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, UserData> allUsers =
                (ConcurrentMap<String, UserData>) getServletContext().getAttribute("USERS");

        // Convert programs to DTOs using convertToProgramInfoDTO
        List<ProgramInfoDTO> programDTOs = new ArrayList<>();
        if (allPrograms != null)
        {
            allPrograms.values().forEach(wrapper -> {
                programDTOs.add(wrapper.convertToProgramInfoDTO());
            });
        }

        // Convert functions to DTOs using convertToProgramInfoDTO
        List<FunctionInfoDTO> functionDTOs = new ArrayList<>();
        if (allFunctions != null)
        {
            allFunctions.values().forEach(wrapper -> {
                functionDTOs.add(wrapper.convertToProgramInfoDTO());
            });
        }

        // Convert users to DTOs
        List<UserInfoDTO> userDTOs = new ArrayList<>();
        if (allUsers != null)
        {
            allUsers.values().forEach(userData -> {
                userDTOs.add(userData.toDTO());
            });
        }

        // Create the response DTO
        UpdateDataDTO updateData = new UpdateDataDTO(programDTOs, functionDTOs,  userDTOs);

        // Send response
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(GSON.toJson(updateData));
    }
}
