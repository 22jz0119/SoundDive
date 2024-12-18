package servlet;

import java.io.IOException;
import java.sql.SQLException; // SQLExceptionをインポート

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.Livehouse_application;

@WebServlet("/Application_approval")
public class Application_approval extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        String applicationIdParam = request.getParameter("id");

        if (applicationIdParam != null) {
            try {
                int applicationId = Integer.parseInt(applicationIdParam);

                // applicationIdに基づいて申請データを取得
                Livehouse_application application = livehouseApplicationDAO.getLivehouse_applicationById(applicationId);

                if (application != null) {
                    String userName = livehouseApplicationDAO.getUserNameByUserId(application.getUser_id());
                    application.setUs_name(userName);

                    request.setAttribute("application", application);
                } else {
                    request.setAttribute("error", "指定された申請データが見つかりません");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "無効な申請ID形式です");
                System.err.println("[ERROR] NumberFormatException: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                request.setAttribute("error", "データベースエラーが発生しました");
                System.err.println("[ERROR] SQLException occurred while processing the application: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            request.setAttribute("error", "申請IDが指定されていません");
        }

        request.getRequestDispatcher("WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
