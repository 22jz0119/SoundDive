package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.Livehouse_application;
import model.Livehouse_information;

@WebServlet("/At_livehouse_reservation_completed")
public class At_livehouse_reservation_completed extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // パラメータ取得
            String applicationIdParam = request.getParameter("applicationId");
            if (applicationIdParam == null || applicationIdParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "申請IDが指定されていません。");
                return;
            }

            int applicationId = Integer.parseInt(applicationIdParam);

            // DAOの初期化
            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);

            // 申請情報を取得
            Livehouse_application application = applicationDAO.getLivehouse_applicationById(applicationId);
            if (application == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された申請情報が見つかりません。");
                return;
            }

            // ライブハウス情報を取得
            Livehouse_information livehouse = applicationDAO.getLivehouseInformationById(application.getLivehouse_information_id());
            if (livehouse == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたライブハウス情報が見つかりません。");
                return;
            }

            // 固定メッセージとデータをリクエストスコープにセット
            request.setAttribute("reservationMessage", "予約が完了しました。");
            request.setAttribute("application", application);
            request.setAttribute("livehouse", livehouse);

            // JSPへフォワード
            request.getRequestDispatcher("reservation_completed.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な申請IDです。");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました: " + e.getMessage());
        }
    }
}
