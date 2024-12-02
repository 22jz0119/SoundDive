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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ログイン済みの場合はホーム画面にリダイレクト
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect(request.getContextPath() + "/At_Home");
            return;
        }

        // ログイン画面を表示
        request.getRequestDispatcher("WEB-INF/jsp/top/top.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tel_number = request.getParameter("tel_number");
        String password = request.getParameter("password");

        System.out.println("Tel Number: " + tel_number);
        System.out.println("Entered Password: " + password);

        AuthLogic logic = new AuthLogic();
        User user = logic.login(tel_number, password);

        if (user != null) {
            HttpSession session = request.getSession(); // セッションを取得
            session.setAttribute("userId", user.getId()); // ユーザーIDをセッションに保存
            session.setAttribute("userName", user.getName()); // 必要に応じて他の情報も保存
            session.setMaxInactiveInterval(30 * 60); // セッションの有効期限を30分に設定

            response.sendRedirect(request.getContextPath() + "/At_Home");
        } else {
            request.setAttribute("msg", "ログインに失敗しました。ユーザー名またはパスワードを確認してください。");
            request.getRequestDispatcher("WEB-INF/jsp/top/top.jsp").forward(request, response);
        }
    }

    // ログアウト処理
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // セッションがある場合のみ取得
        if (session != null) {
            session.invalidate(); // セッションを無効化
        }

        // ログアウト後、ログイン画面にリダイレクト
        response.sendRedirect(request.getContextPath() + "/Top");
    }
}
