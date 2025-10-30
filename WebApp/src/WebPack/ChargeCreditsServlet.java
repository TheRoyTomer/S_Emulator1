package WebPack;

import Server_UTILS.ServerConstants;
import Server_UTILS.UserData;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import static Server_UTILS.ServerConstants.GSON;

@WebServlet(name = "ChargeCreditsServlet", urlPatterns = "/chargeCredits")
public class ChargeCreditsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"Not logged in\"}");
            return;
        }

        String username = (String) session.getAttribute("username");
        if (username == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"No username in session\"}");
            return;
        }

        // Parse request
        int amount;
        try {
            JsonObject json = GSON.fromJson(req.getReader(), JsonObject.class);
            if (json == null || !json.has("amount")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("{\"status\":\"error\",\"message\":\"Amount required\"}");
                return;
            }
            amount = json.get("amount").getAsInt();
        } catch (JsonSyntaxException | NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"Invalid JSON or amount\"}");
            return;
        }

        if (amount <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"Amount must be positive\"}");
            return;
        }

        // Get users map
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, UserData> users =
                (ConcurrentMap<String, UserData>) getServletContext().getAttribute("USERS");

        if (users == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"Users map not found\"}");
            return;
        }

        UserData userData = users.get(username);
        if (userData == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print("{\"status\":\"error\",\"message\":\"User not found\"}");
            return;
        }

        // Add credits
        userData.chargeCredits(amount);
        int newTotal = userData.getCurrentCredits();

        // Return success
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("totalCredits", newTotal);
        response.addProperty("added", amount);
        resp.getWriter().print(GSON.toJson(response));
    }
}