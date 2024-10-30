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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ユーザー情報や必要なデータを取得する処理を追加することができます。
        // ここで必要なデータをリクエストにセットすることができます。
        
        // 例: ユーザー名をセッションから取得する場合
        // String userName = (String) request.getSession().getAttribute("loginUserName");
        // request.setAttribute("userName", userName);
        
        // artist_home.jspにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/at_home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // POSTリクエストに対する処理を追加することができます。
        // 例: フォームからデータを取得して処理を行う
        
        // フォームデータの取得
        String action = request.getParameter("action"); // ボタンやアクションに基づく処理を追加
        
        if ("solo".equals(action)) {
            // ソロライブの処理
            // 例えば、予約処理など
        } else if ("multi".equals(action)) {
            // マルチライブの処理
            // 例えば、予約処理など
        }

        // GETリクエストの処理に戻す
        doGet(request, response);
    }
}
