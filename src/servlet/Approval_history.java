package servlet;

import java.io.IOException;
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

/**
 * 承認履歴を管理するサーブレット
 */
@WebServlet("/Approval_history")
public class Approval_history extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[INFO] Approval_history Servlet: doGet() started");
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
        Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);  // Artist_groupDAOを追加
        HttpSession session = request.getSession();

        // セッションからユーザーIDを取得
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            System.out.println("[ERROR] ログインユーザーIDが取得できませんでした。");
            response.sendRedirect(request.getContextPath() + "/Top");
            return;
        }
        

        System.out.println("[DEBUG] 取得したユーザーID: " + userId);

        // ライブハウスIDの取得
        int livehouseId = livehouseApplicationDAO.getLivehouseIdByUserId(userId);
        if (livehouseId == -1) {
            System.out.println("[WARN] 該当するライブハウス情報が見つかりません。userId: " + userId);
            request.setAttribute("errorMessage", "ライブハウス情報が見つかりません");
            request.setAttribute("reservationStatus", "{}");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
            return;
        }

        // 承認済みの予約を取得
        List<LivehouseApplicationWithGroup> soloApplications = livehouseApplicationDAO.getReservationsWithTrueFalseOne(livehouseId);
        List<LivehouseApplicationWithGroup> cogigApplications = livehouseApplicationDAO.getReservationsByCogigOrSoloTrueFalseOne(livehouseId);

        System.out.println("[DEBUG] soloApplications size: " + soloApplications.size());
        System.out.println("[DEBUG] cogigApplications size: " + cogigApplications.size());

        // 画像マッピング
        Map<Integer, String> pictureImageMap = new HashMap<>();  // グループID -> 画像のパス

        // ソロアプリケーションに対する画像処理
        for (LivehouseApplicationWithGroup app : soloApplications) {
            int groupId = app.getGroupId();
            if (!pictureImageMap.containsKey(groupId)) {
                String pictureImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                if (pictureImage == null || pictureImage.isEmpty()) {
                    pictureImage = "/uploads/default_image.png"; // デフォルト画像
                }
                pictureImageMap.put(groupId, pictureImage);
            }
        }

        // 対バンアプリケーションに対する画像処理
        for (LivehouseApplicationWithGroup app : cogigApplications) {
            int groupId = app.getGroupId();
            if (!pictureImageMap.containsKey(groupId)) {
                String pictureImage = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                if (pictureImage == null || pictureImage.isEmpty()) {
                    pictureImage = "/uploads/default_image.png"; // デフォルト画像
                }
                pictureImageMap.put(groupId, pictureImage);
            }
        }

        // JSPにデータを渡す
        request.setAttribute("soloApplications", soloApplications);
        request.setAttribute("cogigApplications", cogigApplications);
        request.setAttribute("pictureImageMap", pictureImageMap); // 画像マップを渡す

        // JSPへフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/approval_history.jsp").forward(request, response);
    }
}

