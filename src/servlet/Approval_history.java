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

 // Approval_history.java

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        int year = (yearParam != null) ? Integer.parseInt(yearParam) : java.time.LocalDate.now().getYear();
        int month = (monthParam != null) ? Integer.parseInt(monthParam) : java.time.LocalDate.now().getMonthValue();

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // List<LivehouseApplicationWithGroup>型に変更
        List<LivehouseApplicationWithGroup> approvedReservations = livehouseApplicationDAO.getApprovedReservations(year, month, userId);

        if (approvedReservations != null && !approvedReservations.isEmpty()) {
            System.out.println("[DEBUG] 承認済みの予約データ件数: " + approvedReservations.size());

            for (LivehouseApplicationWithGroup app : approvedReservations) {
                System.out.println("[DEBUG] 予約: " + app.getAccountName() + " | ジャンル: " + app.getGroupGenre());
            }
        } else {
            System.out.println("[DEBUG] 承認済みの予約データが存在しません。");
        }

        request.setAttribute("approvedReservations", approvedReservations);
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
