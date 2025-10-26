package WebPack;

import Engine.EngineFacade;
import Server_UTILS.ServerConstants;
import jakarta.servlet.http.*;

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
}
