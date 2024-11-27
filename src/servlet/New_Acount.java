package servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.UserDAO;
import model.User;

@WebServlet("/New_Acount")
public class New_Acount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GETリクエスト処理 - 登録ページにフォワード
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/login/new_acount.jsp").forward(request, response);
    }

    // POSTリクエスト処理 - フォームデータを受け取り、新規ユーザーを登録
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // フォームからデータを取得
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String userType = request.getParameter("account_type"); // アカウントの種類（artist/livehouse）
        String telNumber = request.getParameter("tel");
        String prefecture = request.getParameter("prefecture");
        String addressDetail = request.getParameter("address_detail");

        // 住所を一つのフィールドとして結合
        String address = prefecture + " " + addressDetail;

        // 現在のタイムスタンプを取得
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        // Userオブジェクトを作成（idは自動生成と仮定）
        User user = new User(0, name, password, telNumber, address, currentTimestamp, currentTimestamp, userType);

        // UserDAOのインスタンスを作成し、ユーザーをデータベースに登録
        UserDAO userDao = new UserDAO(DBManager.getInstance());
        boolean isRegistered = userDao.insertUser(user);

        if (isRegistered) {
            // 登録成功時 - メッセージを設定して登録完了ページへフォワード
            request.setAttribute("successMessage", "登録が完了しました。");
            request.getRequestDispatcher("WEB-INF/jsp/top/top.jsp").forward(request, response);
        } else {
            // 登録失敗時 - エラーメッセージを設定し、再度登録ページへフォワード
            request.setAttribute("errorMessage", "登録に失敗しました。再度お試しください。");
            request.getRequestDispatcher("WEB-INF/jsp/login/new_acount.jsp").forward(request, response);
        }
    }
}
