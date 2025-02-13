package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;

@WebServlet("/Application_confirmation")
public class Application_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
        Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);

        String idParam = request.getParameter("id");
        String action = request.getParameter("action");

        if (idParam != null) {
            try {
                int applicationId = Integer.parseInt(idParam);
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

                if (applicationDetails == null) {
                    System.err.println("[ERROR] Application not found for ID: " + applicationId);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "アプリケーションが見つかりません");
                    return;
                }

                request.setAttribute("application", applicationDetails);

                // 承認ボタンが押された場合の処理
                if ("approval".equals(action)) {
                    // ログ出力：承認処理開始
                    System.out.println("[INFO] Approval action triggered for application ID: " + applicationId);

                    // `true_false`を1に更新
                    updateTrueFalse(applicationId, dbManager);

                    // ログ出力：承認処理完了
                    System.out.println("[INFO] Approval completed for application ID: " + applicationId);

                    // artistGroupIdを取得
                    int groupId = applicationDetails.getGroupId();
                    request.setAttribute("artistGroupId", groupId); // ここでartistGroupIdをリクエスト属性にセット

                    // 承認ページに遷移
                    request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
                    return;
                }

                // その他の処理（略）

            } catch (NumberFormatException e) {
                System.err.println("[ERROR] Invalid application ID format: " + idParam);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なアプリケーションID形式です");
                return;
            }
        } else {
            System.err.println("[ERROR] No application ID provided in the request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "アプリケーションIDがリクエストに含まれていません");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void updateTrueFalse(int applicationId, DBManager dbManager) {
        String updateQuery = "UPDATE livehouse_application_table SET true_false = 1 WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setInt(1, applicationId);

            // ログ出力：更新前
            System.out.println("[INFO] Executing update query for application ID: " + applicationId);

            stmt.executeUpdate();

            // ログ出力：更新後
            System.out.println("[INFO] Updated true_false to 1 for application ID: " + applicationId);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Failed to update true_false for application ID: " + applicationId);
            throw new RuntimeException("Failed to update true_false in the database", e);
        }
    }
}


