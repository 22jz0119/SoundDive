package servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.UserDAO; // DAOのインポート
import model.User; // ユーザーモデルのインポート

@WebServlet("/New_Acount")
public class New_Acount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GETリクエスト処理
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 何らかのメッセージが設定されている場合、リクエスト属性を取得
        String errorMessage = (String) request.getAttribute("errorMessage");
        String successMessage = (String) request.getAttribute("successMessage");
        
        // new_acount.jspにフォワード
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("successMessage", successMessage);
        request.getRequestDispatcher("new_acount.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // フォームからデータを取得
        String name = request.getParameter("name");
        String loginId = request.getParameter("login");
        String password = request.getParameter("password");
        String telNumber = request.getParameter("tel"); // 変数名を修正
        String address = request.getParameter("address");
        
        // 現在のタイムスタンプを取得
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        // Userオブジェクトを作成（idは自動生成と仮定）
        User user = new User(0, loginId, name, password, telNumber, address, currentTimestamp, currentTimestamp);

        // UserDAOのインスタンスを作成
        UserDAO userDao = new UserDAO(DBManager.getInstance()); // シングルトンインスタンスを渡す

        // ユーザーをデータベースに登録するためのDAOを使用
        boolean isRegistered = userDao.insertUser(user); // insertUserメソッドを使用

        if (isRegistered) {
            // 登録成功時、成功メッセージを設定して適切なページにリダイレクト
            request.setAttribute("successMessage", "登録が完了しました。");
//            response.sendRedirect("success.jsp"); // 成功ページにリダイレクト（success.jspは適宜作成）
        } else {
            // 登録失敗時、エラーメッセージを設定して元のフォームに戻る
            request.setAttribute("errorMessage", "登録に失敗しました。再度お試しください。");
            doGet(request, response); // doGetメソッドを呼び出してエラーメッセージを表示
        }
    }
}
