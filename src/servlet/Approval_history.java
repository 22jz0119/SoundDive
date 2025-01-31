package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;
import model.User;

/**
 * 承認履歴を管理するサーブレット
 */
@WebServlet("/Approval_history")
public class Approval_history extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
        HttpSession session = request.getSession();

        // ログインユーザーの取得
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Integer loggedInLivehouseId = null;

        if (loggedInUser != null) {
            loggedInLivehouseId = livehouseApplicationDAO.getSingleLivehouseInformationIdByUserId(loggedInUser.getId());
        }

        if (loggedInLivehouseId == null) {
            System.out.println("[ERROR] ログインユーザーに紐づく livehouseInformationId が取得できませんでした。");
            request.setAttribute("errorMessage", "ライブハウス情報が見つかりません。");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/approval_history.jsp").forward(request, response);
            return;
        }

        System.out.println("[DEBUG] Fetched livehouseInformationId: " + loggedInLivehouseId);

        // 承認済みの予約を取得（ログインユーザーのライブハウスIDに紐づくもののみ）
        List<LivehouseApplicationWithGroup> soloApplications = livehouseApplicationDAO.getReservationsWithTrueFalseOne(loggedInLivehouseId);
        List<LivehouseApplicationWithGroup> cogigApplications = livehouseApplicationDAO.getReservationsByCogigOrSoloTrueFalseOne(loggedInLivehouseId);

        System.out.println("[DEBUG] soloApplications size: " + soloApplications.size());
        System.out.println("[DEBUG] cogigApplications size: " + cogigApplications.size());

        // JSP にデータを渡す
        request.setAttribute("soloApplications", soloApplications);
        request.setAttribute("cogigApplications", cogigApplications);
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
        } else {
            System.out.println("[ERROR] 予約IDが送信されていません。");
        }

        response.sendRedirect(request.getContextPath() + "/Approval_history");
    }
}
