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
        // 初回アクセス時にtop.jspにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/top.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ユーザー入力のログインIDとパスワードを取得
        String login_id = request.getParameter("loginId"); // 正しいパラメータ名を確認
        String password = request.getParameter("password");
        
        System.out.println("Login ID: " + login_id);
        System.out.println("Entered Password: " + password); // 追加


        // AuthLogicクラスでログイン認証を実施
        AuthLogic logic = new AuthLogic();
        User user = logic.login(login_id, password);

        if (user != null) {
            // 認証成功：セッションにユーザー情報を保存
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);

            // at_home.jspにリダイレクト
            response.sendRedirect(request.getContextPath() + "/WEB-INF/jsp/at_home.jsp"); // at_home.jspの正しいパスに修正
        } else {
            // 認証失敗：エラーメッセージを設定し、top.jspに戻る
            request.setAttribute("msg", "ログインに失敗しました");
            doGet(request, response);
        }
    }
}
