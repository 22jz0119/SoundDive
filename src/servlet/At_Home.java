package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/At_Home")
public class At_Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isLoggedIn(request, response)) {
            return;
        }

        System.out.println("[DEBUG] User is logged in. Forwarding to at_home.jsp.");
        request.getRequestDispatcher("WEB-INF/jsp/at_home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isLoggedIn(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if ("solo".equals(action)) {
            System.out.println("[DEBUG] Solo live action triggered.");
            response.sendRedirect(request.getContextPath() + "/SoloLiveServlet");
        } else if ("multi".equals(action)) {
            System.out.println("[DEBUG] Multi live action triggered.");
            response.sendRedirect(request.getContextPath() + "/MultiLiveServlet");
        } else {
            System.out.println("[DEBUG] Unknown action: " + action);
            response.sendRedirect(request.getContextPath() + "/At_Home");
        }
    }

    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            System.err.println("[ERROR] User is not logged in. Redirecting to Top.");
            response.sendRedirect(request.getContextPath() + "/Top");
            return false;
        }

        System.out.println("[DEBUG] User is logged in: userId=" + session.getAttribute("userId"));
        return true;
    }
}