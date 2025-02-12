package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;
import model.Member;

@WebServlet("/Application_list")
public class Application_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
        Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);

        // セッションからユーザーIDを取得
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            System.out.println("[ERROR] ログインユーザーIDが取得できませんでした。");
            response.sendRedirect(request.getContextPath() + "/Top");
            return;
        }

        System.out.println("[DEBUG] 取得したユーザーID: " + userId);

        // ライブハウスIDの取得
        Integer livehouseInformationId = livehouseApplicationDAO.getLivehouseIdByUserId(userId);
        if (livehouseInformationId == -1) {
            System.out.println("[WARN] 該当するライブハウス情報が見つかりません。userId: " + userId);
            request.setAttribute("errorMessage", "ライブハウス情報が見つかりません");
            request.setAttribute("reservationStatus", "{}");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
            return;
        }

        // ライブハウス情報をログに出力
        System.out.println("[DEBUG] Found livehouseInformationId: " + livehouseInformationId + " for userId: " + userId);

        // パラメータ取得
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");

        // パラメータを整数に変換
        int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : -1;
        int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : -1;
        int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;

        System.out.println("[DEBUG] Received parameters - year: " + year + ", month: " + month + ", day: " + day);

        List<LivehouseApplicationWithGroup> soloApplications = new ArrayList<>();
        List<LivehouseApplicationWithGroup> cogigApplications = new ArrayList<>();
        Map<Integer, String> pictureImageMap = new HashMap<>(); // groupIdごとの画像マッピング

        int totalReservations = 0; // 予約件数をカウントする変数

        if (year != -1 && month != -1 && day != -1) {
            // livehouseInformationId を渡してデータを取得
            soloApplications = livehouseApplicationDAO.getReservationsWithTrueFalseZero(livehouseInformationId, year, month, day);
            cogigApplications = livehouseApplicationDAO.getReservationsByCogigOrSoloTrueFalseZero(livehouseInformationId, year, month, day);

            // 対バンの場合の追加処理
            Map<Integer, Integer> applicationCogigOrSoloMap = new HashMap<>();
            for (LivehouseApplicationWithGroup application : cogigApplications) {
                int applicationId = application.getApplicationId();
                int cogigOrSolo = livehouseApplicationDAO.getCogigOrSoloByApplicationId(applicationId); // ここでcogigOrSoloを取得
                applicationCogigOrSoloMap.put(applicationId, cogigOrSolo);
                
                // 対バングループ情報を取得
                retrieveArtistGroupDetails(applicationId, pictureImageMap, livehouseApplicationDAO, artistGroupDAO, request);
            }

            // 予約件数をカウント
            totalReservations = soloApplications.size() + cogigApplications.size();
            System.out.println("[DEBUG] Retrieved soloApplications: " + soloApplications.size() + " cogigApplications: " + cogigApplications.size());
            System.out.println("[DEBUG] Total reservations: " + totalReservations);

            // soloApplications の詳細ログ
            for (LivehouseApplicationWithGroup app : soloApplications) {
                System.out.println("[DEBUG] Solo Application details: " + app.getApplicationId() + ", groupId: " + app.getGroupId() + ", accountName: " + app.getAccountName());
            }

            // cogigApplications の詳細ログ
            for (LivehouseApplicationWithGroup app : cogigApplications) {
                System.out.println("[DEBUG] Cogig Application details: " + app.getApplicationId() + ", groupId: " + app.getGroupId() + ", accountName: " + app.getAccountName());
            }

            // 画像マッピング処理
            for (LivehouseApplicationWithGroup app : soloApplications) {
                int groupId = app.getGroupId();
                if (!pictureImageMap.containsKey(groupId)) {
                    String pictureImageMovie = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                    if (pictureImageMovie == null || pictureImageMovie.isEmpty()) {
                        pictureImageMovie = "/uploads/default_image.png"; // デフォルト画像を設定
                    }
                    pictureImageMap.put(groupId, pictureImageMovie);
                }
            }

            // 画像マッピング処理（cogigApplicationsにも適用）
            for (LivehouseApplicationWithGroup app : cogigApplications) {
                int groupId = app.getGroupId();
                if (!pictureImageMap.containsKey(groupId)) {
                    String pictureImageMovie = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                    if (pictureImageMovie == null || pictureImageMovie.isEmpty()) {
                        pictureImageMovie = "/uploads/default_image.png"; // デフォルト画像を設定
                    }
                    pictureImageMap.put(groupId, pictureImageMovie);
                }
            }

            // 最終データの確認ログ
            System.out.println("[DEBUG] final soloApplications: " + soloApplications);
            System.out.println("[DEBUG] final cogigApplications: " + cogigApplications);
            System.out.println("[DEBUG] final pictureImageMap: " + pictureImageMap);
            System.out.println("[DEBUG] final totalReservations: " + totalReservations);

            // データを JSP に渡す
            request.setAttribute("soloApplications", soloApplications);
            request.setAttribute("cogigApplications", cogigApplications);
            request.setAttribute("pictureImageMap", pictureImageMap); // JSP に画像マップを渡す
            request.setAttribute("totalReservations", totalReservations); // 予約件数を渡す
        } else {
            // パラメータエラー時の処理
            System.out.println("[DEBUG] Invalid or missing date parameters");
            request.setAttribute("errorMessage", "年、月、日を正しく入力してください。");
        }

        // JSP へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
    }

    // user_id に基づく artist_group の情報を取得するメソッド
    private void retrieveArtistGroupDetails(int applicationId, Map<Integer, String> pictureImageMap,
            Livehouse_applicationDAO livehouseApplicationDAO, Artist_groupDAO artistGroupDAO, HttpServletRequest request) {
        // まず、cogig_or_solo が 2 の場合、対バングループ情報を取得
        int cogigOrSolo = livehouseApplicationDAO.getCogigOrSoloByApplicationId(applicationId); // applicationId に基づき cogig_or_solo を取得

        if (cogigOrSolo == 2) {
            // 対バングループの ID を取得
            int artistGroupId = livehouseApplicationDAO.getArtistGroupIdByApplicationId(applicationId);
            if (artistGroupId > 0) {
                // 対バングループのメンバー情報を取得
                List<Member> artistMembers = livehouseApplicationDAO.getMembersByGroupId(artistGroupId);
                request.setAttribute("artistMembers", artistMembers);  // メンバー情報をリクエストにセット

                // 対バングループの詳細情報を取得
                LivehouseApplicationWithGroup artistGroup = livehouseApplicationDAO.getGroupDetailsById(artistGroupId);
                request.setAttribute("artistGroup", artistGroup);  // グループ情報をリクエストにセット

                // 対バングループの画像情報を取得
                if (!pictureImageMap.containsKey(artistGroupId)) {
                    String artistPictureImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(artistGroupId);
                    if (artistPictureImage == null || artistPictureImage.isEmpty()) {
                        artistPictureImage = "/uploads/default_image.png"; // デフォルト画像
                    }
                    pictureImageMap.put(artistGroupId, artistPictureImage);  // 画像情報をマップに追加
                }

                // デバッグ用ログ
                System.out.println("[DEBUG] artistGroupId: " + artistGroupId);
                System.out.println("[DEBUG] artistGroup Name: " + (artistGroup != null ? artistGroup.getAccountName() : "NULL"));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
