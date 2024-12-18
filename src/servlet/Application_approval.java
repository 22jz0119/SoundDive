package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;

@WebServlet("/Application_approval")
public class Application_approval extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DBManagerインスタンスを取得
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        // リクエストパラメータからapplicationIdを取得
        String applicationIdParam = request.getParameter("id");

        if (applicationIdParam != null) {
            try {
                int applicationId = Integer.parseInt(applicationIdParam);

                // applicationIdに基づいて申請データを取得
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

                if (applicationDetails != null) {
                    // ユーザーIDからus_nameを取得
                    String userName = livehouseApplicationDAO.getUserNameByUserId(applicationDetails.getUserId());
                    applicationDetails.setUs_name(userName);  // LivehouseApplicationWithGroupオブジェクトにユーザー名を設定

                    // applicationDetails をリクエストスコープに渡す
                    request.setAttribute("application", applicationDetails);
                } else {
                    request.setAttribute("error", "指定された申請データが見つかりません");
                }
            } catch (NumberFormatException e) {
                // IDの形式が正しくない場合はエラーメッセージを設定
                request.setAttribute("error", "無効な申請ID形式です");
                System.err.println("[ERROR] NumberFormatException: " + e.getMessage());
                e.printStackTrace();
            } 
        } else {
            request.setAttribute("error", "申請IDが指定されていません");
        }

        // JSPページにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
