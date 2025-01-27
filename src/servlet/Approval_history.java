package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;

/**
 * Servlet implementation class Approval_history
 */
@WebServlet("/Approval_history")
public class Approval_history extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        // URLパラメータからdate_timeを取得（例: 2025-01-14 08:00:00）
        String dateTimeParam = request.getParameter("date_time");

        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();
        int day = java.time.LocalDate.now().getDayOfMonth();

        if (dateTimeParam != null && !dateTimeParam.isEmpty()) {
            try {
                // date_time パラメータを LocalDateTime に変換
                java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(dateTimeParam);

                // year, month, day を date_time から取得
                year = dateTime.getYear();
                month = dateTime.getMonthValue();
                day = dateTime.getDayOfMonth();

                // デバッグログ
                System.out.println("[DEBUG] Extracted parameters from date_time: year=" + year + ", month=" + month + ", day=" + day);
            } catch (Exception e) {
                System.out.println("[ERROR] 無効な date_time パラメータ: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な日付形式です。");
                return;
            }
        } else {
            System.out.println("[DEBUG] date_time パラメータが指定されていません。デフォルトの日付が使用されます。");
        }

        // ログインしたユーザーIDを取得
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // ユーザーIDに基づいて livehouse_information_id を取得
        int livehouseInformationId = livehouseApplicationDAO.getLivehouseInformationIdForUser(userId);
        System.out.println("[DEBUG] Retrieved livehouse_information_id: " + livehouseInformationId);

        if (livehouseInformationId == -1) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "関連するライブハウス情報が見つかりませんでした。");
            return;
        }

        // 予約データを取得（livehouse_information_id を追加）
        List<LivehouseApplicationWithGroup> approvedReservations = livehouseApplicationDAO.getApprovedReservationsForUser(year, month, day, livehouseInformationId);

        if (approvedReservations != null && !approvedReservations.isEmpty()) {
            System.out.println("[DEBUG] 承認済みの予約データ件数: " + approvedReservations.size());
        } else {
            System.out.println("[DEBUG] 承認済みの予約データが存在しません。");
        }

        // JSPにデータを渡す
        request.setAttribute("approvedReservations", approvedReservations);
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/approval_history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        }

        // 削除後に承認履歴ページにリダイレクト
        response.sendRedirect(request.getContextPath() + "/Approval_history");
    }
}
