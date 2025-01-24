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

        // パラメータの取得
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day"); // 日付のパラメータも取得

        // デフォルト値は現在の日付
        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();
        int day = java.time.LocalDate.now().getDayOfMonth();

        try {
            if (yearParam != null) {
                year = Integer.parseInt(yearParam);
            }
            if (monthParam != null) {
                month = Integer.parseInt(monthParam);
            }
            if (dayParam != null) {
                day = Integer.parseInt(dayParam);
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] パラメータが不正です: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "不正なパラメータが指定されました。");
            return;
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // デバッグログ: パラメータ確認
        System.out.println("[DEBUG] パラメータ: year=" + year + ", month=" + month + ", day=" + day + ", userId=" + userId);

        // DAOメソッドでデータ取得
        List<LivehouseApplicationWithGroup> approvedReservations = livehouseApplicationDAO.getApprovedReservations(year, month, day);

        if (approvedReservations != null && !approvedReservations.isEmpty()) {
            System.out.println("[DEBUG] 承認済みの予約データ件数: " + approvedReservations.size());
            for (LivehouseApplicationWithGroup app : approvedReservations) {
                System.out.println("[DEBUG] 予約: " + app.getAccountName() + " | ジャンル: " + app.getGroupGenre());
            }
        } else {
            System.out.println("[DEBUG] 指定された条件で承認済みの予約データはありません。");
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
