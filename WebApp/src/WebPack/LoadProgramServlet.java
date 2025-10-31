package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Function;
import Engine.Programs.Program;
import Out.LoadResultDTO;
import Server_UTILS.FunctionHolderWrapper;
import Server_UTILS.ProgramHolderWrapper;
import Server_UTILS.UserData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "LoadProgramServlet", urlPatterns = "/api/load")
@MultipartConfig(
        fileSizeThreshold = 1,
        maxFileSize = -1L,
        maxRequestSize = -1L
)
public class LoadProgramServlet extends BaseServlet
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("application/json; charset=UTF-8");

        Part filePart = request.getPart("programFile");
        if (filePart == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"error\":\"programFile is required (multipart/form-data)\"}");
            return;
        }

        Path tempFile = Files.createTempFile("program-", ".xml");
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        //EngineFacade facade = requireFacade(request, response);
        EngineFacade facade = new EngineFacade();

        ConcurrentMap<String, FunctionHolderWrapper> Functions =
                (ConcurrentMap<String, FunctionHolderWrapper>) getServletContext().getAttribute("ALL_FUNCTIONS");
        ConcurrentMap<String, Function> functionMap = Functions.entrySet().stream()
                .collect(Collectors.toConcurrentMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getFunction()
                ));

        ConcurrentMap<String, ProgramHolderWrapper> allProgramKeySet =
                (ConcurrentMap<String, ProgramHolderWrapper>) getServletContext().getAttribute("ALL_PROGRAMS");
        Set<String> inputSet = allProgramKeySet.keySet();

        try {
            LoadResultDTO res = facade.loadFromXML(tempFile.toFile(), functionMap, inputSet);

            if(res.isLoaded())
            {
                @SuppressWarnings("unchecked")
                ConcurrentMap<String, ProgramHolderWrapper> allPrograms =
                        (ConcurrentMap<String, ProgramHolderWrapper>) getServletContext().getAttribute("ALL_PROGRAMS");
                @SuppressWarnings("unchecked")
                ConcurrentMap<String, FunctionHolderWrapper> allFunctions =
                        (ConcurrentMap<String, FunctionHolderWrapper>) getServletContext().getAttribute("ALL_FUNCTIONS");
                @SuppressWarnings("unchecked")
                ConcurrentMap<String, UserData> users =
                        (ConcurrentMap<String, UserData>) getServletContext().getAttribute("USERS");

                String uploadedUsername = getUsername(request, response);
                UserData thisUser = users.get(uploadedUsername);


                Program mainProgram = facade.getProgram();
                allPrograms.put(mainProgram.getName(), new ProgramHolderWrapper(mainProgram, uploadedUsername));


                thisUser.incrementProgramCount();
                List<Function> functions = mainProgram.getFunctions();
                if (functions != null) {
                    functions.forEach(function -> {
                        allFunctions.put(function.getName(),
                                new FunctionHolderWrapper(function, uploadedUsername, mainProgram.getName()));
                        thisUser.incrementFunctionCount();
                    });
                }

            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(GSON.toJson(res));

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"error\":\"internal error while loading program\"}");
            System.out.println("Load Program Error: " + ex.getMessage());
        } finally {
            try { Files.deleteIfExists(tempFile); } catch (Exception ignore) {}
        }
    }


}