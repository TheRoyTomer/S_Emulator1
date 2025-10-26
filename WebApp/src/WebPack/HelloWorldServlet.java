package WebPack;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='he'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>יעלה ויבוא השרת 🚀</title>");
        out.println("<style>");
        out.println("body {");
        out.println("  margin: 0;");
        out.println("  padding: 0;");
        out.println("  overflow: hidden;");
        out.println("  background: radial-gradient(ellipse at bottom, #1b2735 0%, #090a0f 100%);");
        out.println("  height: 100vh;");
        out.println("  display: flex;");
        out.println("  justify-content: center;");
        out.println("  align-items: center;");
        out.println("  font-family: Arial, sans-serif;");
        out.println("}");
        out.println(".message {");
        out.println("  position: relative;");
        out.println("  color: #00e0ff;");
        out.println("  font-size: 48px;");
        out.println("  font-weight: bold;");
        out.println("  text-shadow: 0 0 20px #00e0ff, 0 0 40px #00e0ff;");
        out.println("  z-index: 2;");
        out.println("}");
        out.println(".stars {");
        out.println("  position: absolute;");
        out.println("  width: 1px;");
        out.println("  height: 1px;");
        out.println("  background: transparent;");
        out.println("  box-shadow:");

        // יוצרים כוכבים במיקומים רנדומליים
        for (int i = 0; i < 200; i++) {
            int x = (int)(Math.random() * 2000);
            int y = (int)(Math.random() * 1000);
            out.print(x + "px " + y + "px white" + (i < 199 ? ", " : ";"));
        }

        out.println("  animation: moveStars 200s linear infinite;");
        out.println("}");
        out.println("@keyframes moveStars {");
        out.println("  from { transform: translateY(0px); }");
        out.println("  to { transform: translateY(-1000px); }");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='stars'></div>");
        out.println("<div class='message'>יעלה ויבוא, יעלה ויבוא, השרת 🚀</div>");        out.println("</body>");
        out.println("</html>");

        out.flush();
    }
}



