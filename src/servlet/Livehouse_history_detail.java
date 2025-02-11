package servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

/**
 * Servlet implementation class Livehouse_history_detail
 */
@WebServlet("/Livehouse_history_detail")
public class Livehouse_history_detail extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Livehouse_history_detail.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("[START] Livehouse_history_detail Servlet - Processing GET request");

        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
        Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);  // 追加: Artist_groupDAO

        String idParam = request.getParameter("applicationId");
        String action = request.getParameter("action");
        
        LOGGER.info("[INFO] Received request with parameters: id=" + idParam + ", action=" + action);

        if (idParam != null) {
            try {
                int applicationId = Integer.parseInt(idParam);
                LOGGER.info("[INFO] Parsed applicationId: " + applicationId);

                LOGGER.info("[INFO] Fetching application details for ID: " + applicationId);
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

                if (applicationDetails == null) {
                    LOGGER.warning("[ERROR] Application not found for ID: " + applicationId);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "アプリケーションが見つかりません");
                    return;
                }

                request.setAttribute("application", applicationDetails);
                LOGGER.info("[SUCCESS] Retrieved application details for ID: " + applicationId);

                // 申請者のグループ画像処理
                int groupId = applicationDetails.getGroupId();
                String groupImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                if (groupImage == null || groupImage.isEmpty()) {
                    groupImage = "/uploads/default_image.png";  // デフォルト画像設定
                }
                request.setAttribute("groupImage", groupImage);  // 画像をJSPに渡す

                // 申請者のメンバー情報
                LOGGER.info("[INFO] Fetching members for group ID: " + groupId);
                List<Member> members = livehouseApplicationDAO.getMembersByGroupId(groupId);
                request.setAttribute("members", members);

                // `cogig_or_solo` の取得
                LOGGER.info("[INFO] Fetching cogigOrSolo status for application ID: " + applicationId);
                int cogigOrSolo = livehouseApplicationDAO.getCogigOrSoloByApplicationId(applicationId);
                request.setAttribute("cogigOrSolo", cogigOrSolo);

                // **対バン（cogig_or_solo = 2）の場合、相手のグループ情報を取得**
                if (cogigOrSolo == 2) {
                    LOGGER.info("[INFO] Application ID " + applicationId + " is a co-gig request. Fetching opponent group info.");

                    int artistGroupId = livehouseApplicationDAO.getArtistGroupIdByApplicationId(applicationId);
                    LOGGER.info("[INFO] Retrieved opponent artist group ID: " + artistGroupId);

                    if (artistGroupId > 0) {
                        // 対バングループのメンバー情報を取得
                        LOGGER.info("[INFO] Fetching members for opponent group ID: " + artistGroupId);
                        List<Member> artistMembers = livehouseApplicationDAO.getMembersByGroupId(artistGroupId);
                        request.setAttribute("artistMembers", artistMembers);

                        // **対バングループの詳細情報を取得**
                        LOGGER.info("[INFO] Fetching details for opponent group ID: " + artistGroupId);
                        LivehouseApplicationWithGroup artistGroup = livehouseApplicationDAO.getGroupDetailsById(artistGroupId);
                        request.setAttribute("artistGroup", artistGroup);

                        // 対バングループの画像も取得
                        String artistGroupImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(artistGroupId);
                        if (artistGroupImage == null || artistGroupImage.isEmpty()) {
                            artistGroupImage = "/uploads/default_image.png";  // デフォルト画像
                        }
                        request.setAttribute("artistGroupImage", artistGroupImage);  // 対バングループの画像をJSPに渡す

                        // **デバッグ用ログ**
                        LOGGER.info("[DEBUG] Opponent Group ID: " + artistGroupId);
                        LOGGER.info("[DEBUG] Opponent Group Name: " + (artistGroup != null ? artistGroup.getAccountName() : "NULL"));
                    }
                }

                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/approval_history_detail.jsp").forward(request, response);
                return;

            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "[ERROR] Invalid application ID format: " + idParam, e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なアプリケーションID形式です");
                return;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "[ERROR] Unexpected error occurred while processing application ID: " + idParam, e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました");
                return;
            }
        } else {
            LOGGER.warning("[ERROR] No application ID provided in the request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "アプリケーションIDがリクエストに含まれていません");
            return;
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
