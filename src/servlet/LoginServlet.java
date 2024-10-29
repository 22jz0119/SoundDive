package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.UserDAO;
import model.User;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = DBManager.getInstance();
        userDAO = new UserDAO(dbManager);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // リクエストパラメータからログインIDとパスワードを取得
        String loginId = request.getParameter("login_id");
        String password = request.getParameter("password");

        // ログインを確認
        User user = userDAO.findByLoginIdAndPassword(loginId, password);

        // レスポンスとして結果を表示
        out.println("<html>");
        out.println("<head><title>Login Result</title></head>");
        out.println("<body>");

        if (user != null) {
            out.println("<h2>ログイン成功</h2>");
            out.println("<p>ユーザーID: " + user.getId() + "</p>");
            out.println("<p>ユーザー名: " + user.getName() + "</p>");
        } else {
            out.println("<h2>ログイン失敗</h2>");
            out.println("<p>ログインIDまたはパスワードが正しくありません。</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
