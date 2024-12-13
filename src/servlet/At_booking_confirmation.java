package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.UserDAO;
import model.Artist_group;
import model.User;

@WebServlet("/At_booking_confirmation")
public class At_booking_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public At_booking_confirmation() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // リクエストパラメータからユーザーIDを取得
        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int userId = Integer.parseInt(idStr);

                // DBManagerのシングルトンインスタンスを取得
                DBManager dbManager = DBManager.getInstance();

                // DAOのインスタンスを取得
                Artist_groupDAO artistGroupDao = Artist_groupDAO.getInstance(dbManager);
                UserDAO userDao = new UserDAO(dbManager);
                Livehouse_applicationDAO livehouseApplicationDao = new Livehouse_applicationDAO(dbManager);

                // user_idに紐づくアーティストグループを取得
                Artist_group group = artistGroupDao.getGroupByUserId(userId);

                // user_idに紐づくユーザー情報を取得
                User user = userDao.getUserById(userId);

                // user_idに紐づくライブハウス申請情報を取得
 
                if (group != null && user != null) {
                    // アーティストグループ情報、ユーザー情報、ライブハウス申請情報をリクエストスコープに保存
                    request.setAttribute("artistGroup", group);
                    request.setAttribute("user", user);


                    // JSPにフォワード
                    RequestDispatcher dispatcher = request.getRequestDispatcher("artist_group_details.jsp");
                    dispatcher.forward(request, response);
                } else {
                    // データが見つからない場合
                    response.getWriter().println("No data found for user ID: " + userId);
                }
            } catch (NumberFormatException e) {
                // 無効なID形式の場合
                response.getWriter().println("Invalid user ID format.");
            }
        } else {
            // IDが指定されていない場合
            response.getWriter().println("User ID is missing.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}