package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;
import model.Member;

@WebServlet("/Application_confirmation")
public class Application_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        // リクエストパラメータの取得
        String idParam = request.getParameter("id");
        String action = request.getParameter("action");

        if (idParam != null) {
            try {
                int applicationId = Integer.parseInt(idParam);

                // 承認ボタンが押された場合の処理
                if ("approval".equals(action)) {
                    // `true_false`を1に更新
                    updateTrueFalse(applicationId, dbManager);

                    // 承認ページに遷移
                    request.setAttribute("applicationId", applicationId);
                    request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
                    return;
                }

                // アプリケーション詳細データを取得
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

                if (applicationDetails != null) {
                    // アプリケーションデータをリクエストスコープにセット
                    request.setAttribute("application", applicationDetails);

                    // applicationDetailsからgroupIdを取得
                    int groupId = applicationDetails.getGroupId();

                    // Livehouse_applicationDAOを使ってメンバー情報を取得
                    List<Member> members = livehouseApplicationDAO.getMembersByGroupId(groupId);
                    request.setAttribute("members", members);

                    // JSPにフォワード
                    request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_confirmation.jsp").forward(request, response);
                } else {
                    System.err.println("Application not found for ID: " + applicationId);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "アプリケーションが見つかりません");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid application ID format: " + idParam);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なアプリケーションID形式です");
            }
        } else {
            System.err.println("No application ID provided in the request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "アプリケーションIDがリクエストに含まれていません");
        }

        // デフォルトアクション
        if ("list".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
        } else {
            response.sendRedirect("navigate?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * `true_false`を1に更新するメソッド
     */
    private void updateTrueFalse(int applicationId, DBManager dbManager) {
        String updateQuery = "UPDATE livehouse_application_table SET true_false = 1 WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setInt(1, applicationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update true_false in the database", e);
        }
    }
}
