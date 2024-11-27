package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;  // Livehouse_applicationDAOをインポート
import model.LivehouseApplicationWithGroup;
import model.Member;

@WebServlet("/Application_confirmation")
public class Application_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        // リクエストパラメータの取得
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                int applicationId = Integer.parseInt(idParam);

                // アプリケーション詳細データを取得
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

                if (applicationDetails != null) {
                    // アプリケーションデータをリクエストスコープにセット
                    request.setAttribute("application", applicationDetails);

                    // applicationDetailsからgroupIdを取得
                    int groupId = applicationDetails.getGroupId();  // グループIDを取得する

                    // Livehouse_applicationDAOを使ってメンバー情報を取得
                    List<Member> members = livehouseApplicationDAO.getMembersByGroupId(groupId); // メンバー情報をDAOから取得
                    request.setAttribute("members", members); // メンバー情報をリクエストスコープに設定

                    // JSPにフォワード
                    request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_confirmation.jsp").forward(request, response);
                } else {
                    System.err.println("Application not found for ID: " + applicationId);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "アプリケーションが見つかりません");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid application ID format: " + idParam);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なアプリケーションID形式です");
            }
        } else {
            System.err.println("No application ID provided in the request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "アプリケーションIDがリクエストに含まれていません");
        }
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            // application_list.jsp へ遷移
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
        } else if ("approval".equals(action)) {
            // application_approval.jsp へ遷移
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
        } else {
            // デフォルトは application_list.jsp
            response.sendRedirect("navigate?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
