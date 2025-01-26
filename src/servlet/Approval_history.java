package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

        // 年月を取得（パラメータがない場合は現在の年月）
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        int year = (yearParam != null) ? Integer.parseInt(yearParam) : java.time.LocalDate.now().getYear();
        int month = (monthParam != null) ? Integer.parseInt(monthParam) : java.time.LocalDate.now().getMonthValue();

        // ユーザーIDをセッションから取得
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        

        // cogig_or_solo を考慮した承認済みデータを取得
        Map<Integer, List<LivehouseApplicationWithGroup>> approvedReservations = livehouseApplicationDAO.getAllApprovedReservations(year, month, userId);

        // cogig_or_solo = 1（個人）のリスト
        List<LivehouseApplicationWithGroup> approvedReservations1 = approvedReservations.get(1);

        // cogig_or_solo = 2（バンド）のリスト
        List<LivehouseApplicationWithGroup> approvedReservations2 = approvedReservations.get(2);

        // デバッグ用出力
        System.out.println("[DEBUG] cogig_or_solo=1 のデータ件数: " + (approvedReservations1 != null ? approvedReservations1.size() : 0));
        System.out.println("[DEBUG] cogig_or_solo=2 のデータ件数: " + (approvedReservations2 != null ? approvedReservations2.size() : 0));

        // リクエストスコープに設定
        request.setAttribute("approvedReservations1", approvedReservations1);
        request.setAttribute("approvedReservations2", approvedReservations2);

        // JSP にフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/approval_history.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationIdStr = request.getParameter("applicationId");

        if (applicationIdStr != null && !applicationIdStr.isEmpty()) {
            int applicationId = Integer.parseInt(applicationIdStr);

            Livehouse_applicationDAO dao = new Livehouse_applicationDAO(DBManager.getInstance());
            boolean isDeleted = dao.deleteReservationById(applicationId);

            if (isDeleted) {
                System.out.println("[DEBUG] 予約ID " + applicationId + " を削除しました。");
            } else {
                System.out.println("[ERROR] 予約ID " + applicationId + " の削除に失敗しました。");
            }
        }

        // 削除後に承認履歴ページにリダイレクト
        response.sendRedirect(request.getContextPath() + "/Approval_history");
    }
}
