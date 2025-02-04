package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

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
        HttpSession session = request.getSession();

        // セッションからユーザーIDを取得
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            System.out.println("[ERROR] ログインユーザーIDが取得できませんでした。");
            response.sendRedirect(request.getContextPath() + "/Top");
            return;
        }

        System.out.println("[DEBUG] 取得したユーザーID: " + userId);

        // `applicationId` が指定されている場合は詳細ページに遷移
        String applicationIdStr = request.getParameter("applicationId");
        if (applicationIdStr != null && !applicationIdStr.isEmpty()) {
            try {
                int applicationId = Integer.parseInt(applicationIdStr);
                LivehouseApplicationWithGroup applicationDetail = livehouseApplicationDAO.getApplicationById(applicationId);

                if (applicationDetail != null) {
                    request.setAttribute("applicationDetail", applicationDetail);
                    request.getRequestDispatcher("/WEB-INF/jsp/livehouse/approval_history_detail.jsp").forward(request, response);
                    return;
                } else {
                    System.out.println("[WARN] 指定された予約IDのデータが見つかりません。 applicationId: " + applicationId);
                    request.setAttribute("errorMessage", "指定された予約情報が見つかりません。");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 無効な予約IDが指定されました: " + applicationIdStr);
                request.setAttribute("errorMessage", "無効な予約IDが指定されました。");
            }
        }

        // ライブハウスIDの取得
        int livehouseId = livehouseApplicationDAO.getLivehouseIdByUserId(userId);
        if (livehouseId == -1) {
            System.out.println("[WARN] 該当するライブハウス情報が見つかりません。userId: " + userId);

            if ("json".equals(request.getParameter("format"))) {
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(Map.of("message", "ライブハウス情報が見つかりません", "data", null)));
                return;
            }

            request.setAttribute("errorMessage", "ライブハウス情報が見つかりません");
            request.setAttribute("reservationStatus", "{}");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
            return;
        }

        System.out.println("[DEBUG] 取得したライブハウスID: " + livehouseId);
        
        // 承認済みの予約を取得（ログインユーザーのライブハウスIDに紐づくもののみ）
        List<LivehouseApplicationWithGroup> soloApplications = livehouseApplicationDAO.getReservationsWithTrueFalseOne(livehouseId);
        List<LivehouseApplicationWithGroup> cogigApplications = livehouseApplicationDAO.getReservationsByCogigOrSoloTrueFalseOne(livehouseId);

        System.out.println("[DEBUG] soloApplications size: " + soloApplications.size());
        System.out.println("[DEBUG] cogigApplications size: " + cogigApplications.size());

        // JSP にデータを渡す
        request.setAttribute("soloApplications", soloApplications);
        request.setAttribute("cogigApplications", cogigApplications);
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/approval_history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[INFO] Approval_history Servlet: doPost() started");
        String applicationIdStr = request.getParameter("applicationId");

        if (applicationIdStr != null && !applicationIdStr.isEmpty()) {
            try {
                int applicationId = Integer.parseInt(applicationIdStr);
                Livehouse_applicationDAO dao = new Livehouse_applicationDAO(DBManager.getInstance());
                boolean isDeleted = dao.deleteReservationById(applicationId);

                if (isDeleted) {
                    System.out.println("[DEBUG] 予約ID " + applicationId + " を削除しました。");
                } else {
                    System.out.println("[ERROR] 予約ID " + applicationId + " の削除に失敗しました。");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 不正な予約IDが指定されました: " + applicationIdStr);
            }
        } else {
            System.out.println("[ERROR] 予約IDが送信されていません。");
        }

        response.sendRedirect(request.getContextPath() + "/Approval_history");
    }
}
