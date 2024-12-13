package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/At_Home")
public class At_Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ログイン状態をチェック
        if (!isLoggedIn(request, response)) {
            return; // 未ログインの場合はリダイレクト
        }

        // ログインしている場合の処理
        request.getRequestDispatcher("WEB-INF/jsp/at_home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ログイン状態をチェック
        if (!isLoggedIn(request, response)) {
            return; // 未ログインの場合はリダイレクト
        }

        // フォームデータの取得
        String action = request.getParameter("action");

        if ("solo".equals(action)) {
            // ソロライブの処理
        } else if ("multi".equals(action)) {
            // マルチライブの処理
        }

        // GETリクエストの処理に戻す
        doGet(request, response);
    }

    /**
     * ログイン状態をチェックするメソッド
     */
    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId"); // 属性名を 'userId' に変更
        if (userId == null) {
            System.err.println("[ERROR] User is not logged in. Redirecting to login page.");
            response.sendRedirect(request.getContextPath() + "/Top"); // JSPではなくサーブレットにリダイレクト
            return false;
        }
        return true;
    }
}
