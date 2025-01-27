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
        // ログイン画面を表示
        request.getRequestDispatcher("WEB-INF/jsp/top/top.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tel_number = request.getParameter("tel_number");
        String password = request.getParameter("password");

        System.out.println("[DEBUG] Tel Number: " + tel_number);
        System.out.println("[DEBUG] Entered Password: " + password);

        AuthLogic logic = new AuthLogic();
        User user = logic.login(tel_number, password);

        if (user != null) {
            HttpSession session = request.getSession(); // セッションを取得
            session.setAttribute("userId", user.getId()); // ユーザーIDをセッションに保存
            session.setAttribute("userType", user.getUser_type()); // user_typeをセッションに保存
            session.setAttribute("userName", user.getName()); // 必要に応じて他の情報も保存
            session.setMaxInactiveInterval(30 * 60); // セッションの有効期限を30分に設定

            // user_typeのログを記録
            System.out.println("[DEBUG] User ID: " + user.getId());
            System.out.println("[DEBUG] User Type: " + user.getUser_type());

            // user_typeで遷移先を分岐
            if ("livehouse".equals(user.getUser_type())) {
                response.sendRedirect(request.getContextPath() + "/Livehouse_home");
            } else if ("artist".equals(user.getUser_type())) {
                response.sendRedirect(request.getContextPath() + "/At_Home");
            } else {
                System.out.println("[DEBUG] Undefined user type: " + user.getUser_type());
                response.sendRedirect(request.getContextPath() + "/Top"); // その他の場合はログイン画面にリダイレクト
            }
        } else {
            System.out.println("[DEBUG] Login failed for tel_number: " + tel_number);
            request.setAttribute("msg", "ログインに失敗しました。電話番号またはパスワードを確認してください。");
            System.out.println("[DEBUG] Error message: " + request.getAttribute("msg"));
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
