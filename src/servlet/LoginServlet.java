package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logic.AuthLogic;
import model.User;

@WebServlet("/loginTest")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // フォームから取得するデータ
        String loginId = request.getParameter("login_id");
        String password = request.getParameter("password");

        // AuthLogicのインスタンス化
        AuthLogic authLogic = new AuthLogic();
        User user = authLogic.login(loginId, password);

        out.println("<html><body>");
        if (user != null) {
            // ログイン成功時にセッションにユーザー情報を保存
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
            out.println("<h2>ログイン成功</h2>");
            out.println("<p>ユーザーID: " + user.getId() + "</p>");
            out.println("<p>ユーザー名: " + user.getName() + "</p>");
        } else {
            out.println("<h2>ログイン失敗</h2>");
            out.println("<p>ログインIDまたはパスワードが正しくありません。</p>");
        }
        out.println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
