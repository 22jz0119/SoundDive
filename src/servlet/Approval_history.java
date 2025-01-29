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
 * 承認履歴を管理するサーブレット
 */
@WebServlet("/Approval_history")
public class Approval_history extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        // パラメータ取得（livehouse_information_id を取得）
        String livehouseIdParam = request.getParameter("livehouseInformationId");
        Integer livehouseInformationId = null;
        
        if (livehouseIdParam != null && !livehouseIdParam.isEmpty()) {
            try {
                livehouseInformationId = Integer.parseInt(livehouseIdParam);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 無効な livehouseInformationId: " + livehouseIdParam);
                request.setAttribute("errorMessage", "無効なライブハウスIDです。");
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_history.jsp").forward(request, response);
                return;
            }
        }

        System.out.println("[DEBUG] Fetching reservations for livehouseInformationId: " + (livehouseInformationId != null ? livehouseInformationId : "ALL"));

        // 承認済みのソロ & 対バンデータ取得（ライブハウスIDがない場合は全件取得）
        List<LivehouseApplicationWithGroup> soloApplications = livehouseApplicationDAO.getReservationsWithTrueFalseOne(livehouseInformationId);
        List<LivehouseApplicationWithGroup> cogigApplications = livehouseApplicationDAO.getReservationsByCogigOrSoloTrueFalseOne(livehouseInformationId);

        System.out.println("[DEBUG] soloApplications size: " + soloApplications.size());
        System.out.println("[DEBUG] cogigApplications size: " + cogigApplications.size());

        // データを JSP に渡す
        request.setAttribute("soloApplications", soloApplications);
        request.setAttribute("cogigApplications", cogigApplications);

        // JSP へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
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

        // 削除後に承認履歴ページへリダイレクト
        response.sendRedirect(request.getContextPath() + "/Approval_history");
    }
}
