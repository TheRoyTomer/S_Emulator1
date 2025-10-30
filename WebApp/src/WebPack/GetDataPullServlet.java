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

        // Get username from session
        String username = null;
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session != null) {
            username = (String) session.getAttribute("username");
        }

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

        // Get current user's credits
        int userCredits = 0;
        if (username != null && allUsers != null)
        {
            UserData userData = allUsers.get(username);

            if (userData != null) {userCredits = userData.getCurrentCredits();}
        }

        // Convert programs to DTOs using convertToProgramInfoDTO
        List<ProgramInfoDTO> programDTOs = new ArrayList<>();
        if (allPrograms != null)
        {
            System.out.println("=== GetDataPullServlet: Converting Programs to DTOs ===");
            System.out.println("Total programs in map: " + allPrograms.size());
            allPrograms.forEach((key, wrapper) -> {
                System.out.println("  Map Key: " + key + " | Program Name: " + wrapper.getProgram().getName());
            });

            allPrograms.values().forEach(wrapper -> {
                ProgramInfoDTO dto = wrapper.convertToProgramInfoDTO();
                programDTOs.add(dto);
                System.out.println("  Added DTO: " + dto.getName());
            });

            System.out.println("Total DTOs created: " + programDTOs.size());
            programDTOs.forEach(dto -> System.out.println("  Final DTO: " + dto.getName()));
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

        // Create the response DTO with user credits
        UpdateDataDTO updateData = new UpdateDataDTO(programDTOs, functionDTOs, userDTOs, userCredits);

        // Log the JSON before sending
        String jsonResponse = GSON.toJson(updateData);
        System.out.println("=== GetDataPullServlet: JSON Response ===");
        System.out.println(jsonResponse);

        // Send response
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(jsonResponse);
    }
}