package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
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
                    // `true_false`を1に更新
                    updateTrueFalse(applicationId, dbManager);

                    // artistGroupIdを取得
                    int groupId = applicationDetails.getGroupId();
                    request.setAttribute("artistGroupId", groupId); // ここでartistGroupIdをリクエスト属性にセット

                    // 承認ページに遷移
                    request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
                    return;
                }

                // 申請者のグループ情報
                int groupId = applicationDetails.getGroupId();
                List<Member> members = livehouseApplicationDAO.getMembersByGroupId(groupId);
                request.setAttribute("members", members);

                // `cogig_or_solo` の取得
                int cogigOrSolo = livehouseApplicationDAO.getCogigOrSoloByApplicationId(applicationId);
                request.setAttribute("cogigOrSolo", cogigOrSolo);

                // 画像マッピング用のMap
                Map<Integer, String> pictureImageMap = new HashMap<>();

                // 申請者グループの画像を取得
                if (!pictureImageMap.containsKey(groupId)) {
                    String pictureImageMovie = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                    if (pictureImageMovie == null || pictureImageMovie.isEmpty()) {
                        pictureImageMovie = "/uploads/default_image.png"; // デフォルト画像を設定
                    }
                    pictureImageMap.put(groupId, pictureImageMovie);
                }

                // **対バン（cogig_or_solo = 2）の場合、相手のグループ情報を取得**
                if (cogigOrSolo == 2) {
                    int artistGroupId = livehouseApplicationDAO.getArtistGroupIdByApplicationId(applicationId);
                    if (artistGroupId > 0) {
                        // 対バングループのメンバー情報を取得
                        List<Member> artistMembers = livehouseApplicationDAO.getMembersByGroupId(artistGroupId);
                        request.setAttribute("artistMembers", artistMembers);

                        // 対バングループの詳細情報を取得
                        LivehouseApplicationWithGroup artistGroup = livehouseApplicationDAO.getGroupDetailsById(artistGroupId);
                        request.setAttribute("artistGroup", artistGroup);

                        // 対バングループの画像を取得
                        if (!pictureImageMap.containsKey(artistGroupId)) {
                            String artistPictureImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(artistGroupId);
                            if (artistPictureImage == null || artistPictureImage.isEmpty()) {
                                artistPictureImage = "/uploads/default_image.png"; // デフォルト画像
                            }
                            pictureImageMap.put(artistGroupId, artistPictureImage);
                        }

                        // デバッグ用ログ
                        System.out.println("[DEBUG] artistGroupId: " + artistGroupId);
                        System.out.println("[DEBUG] artistGroup Name: " + (artistGroup != null ? artistGroup.getAccountName() : "NULL"));
                    }
                }

                // 画像マップをJSPに渡す
                request.setAttribute("pictureImageMap", pictureImageMap);
                
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_confirmation.jsp").forward(request, response);
                return;

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
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update true_false in the database", e);
        }
    }
}

