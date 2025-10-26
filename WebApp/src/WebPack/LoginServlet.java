package WebPack;

import Engine.EngineFacade;
import Engine.Programs.Program;
import Server_UTILS.ServerConstants;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static Server_UTILS.ServerConstants.GSON;
import static Server_UTILS.ServerConstants.REGISTRY_ATTR;

@WebServlet(name = "LoginServlet", urlPatterns = "/login", loadOnStartup = 1)
public class LoginServlet extends BaseServlet
{

    private ConcurrentMap<String, String> usernameToSession;

    @Override
    public void init() {
        ServletContext ctx = getServletContext();
        synchronized (ctx) {
            @SuppressWarnings("unchecked")
            ConcurrentMap<String, String> map =
                    (ConcurrentMap<String, String>) ctx.getAttribute(REGISTRY_ATTR);
            if (map == null)
            {
                map = new ConcurrentHashMap<>();
                ctx.setAttribute(REGISTRY_ATTR, map);
            }
            this.usernameToSession = map;

            @SuppressWarnings("unchecked")
            ConcurrentMap<String, Program> programs =
                    (ConcurrentMap<String, Program>) ctx.getAttribute("ALL_PROGRAMS");
            if (programs == null)
            {
                programs = new ConcurrentHashMap<>();
                ctx.setAttribute("ALL_PROGRAMS", programs);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        final String username;
        try {
            JsonObject json = GSON.fromJson(req.getReader(), JsonObject.class);
            username = (json != null && json.has("username")) ? json.get("username").getAsString().trim() : null;
        } catch (JsonSyntaxException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"invalid json\"}");
            return;
        }

        if (username == null || username.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"username required\"}");
            return;
        }

        HttpSession session = req.getSession(true);
        String prev = usernameToSession.putIfAbsent(username, session.getId());
        if (prev != null) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"username already in use\"}");
            return;
        }

        EngineFacade facade = new EngineFacade();
        session.setAttribute(ServerConstants.SESSION_FACADE_KEY, facade);
        session.setAttribute("username", username);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print("{\"status\":\"success\",\"username\":\"" + username + "\"}");
    }
}
