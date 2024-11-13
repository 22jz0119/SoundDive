package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logic.AuthLogic;
import model.User;

@WebServlet("/Top")
public class Top extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/jsp/top.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tel_number = request.getParameter("tel_number");
        String password = request.getParameter("password");

        System.out.println("Tel Number: " + tel_number);
        System.out.println("Entered Password: " + password);

        AuthLogic logic = new AuthLogic();
        User user = logic.login(tel_number, password);

        if (user != null) {
            HttpSession session = request.getSession();
            logic.loginUserSession(session, user);

            response.sendRedirect(request.getContextPath() + "/At_Home");
        } else {
            request.setAttribute("msg", "ログインに失敗しました");
            request.getRequestDispatcher("WEB-INF/jsp/top.jsp").forward(request, response);
        }
    }
}
