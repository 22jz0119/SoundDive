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

@WebServlet("/Application_list")
public class Application_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        // 申請リストを常に取得
        List<LivehouseApplicationWithGroup> applicationList = livehouseApplicationDAO.getApplicationsWithGroups();
        request.setAttribute("applicationList", applicationList);

        // リクエストパラメータからIDを取得
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int applicationId = Integer.parseInt(idParam);

                // 指定されたIDの詳細情報を取得
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);
                if (applicationDetails != null) {
                    // 詳細情報をリクエストスコープに設定
                    request.setAttribute("applicationDetails", applicationDetails);

                    // 詳細ページにフォワード
                    request.getRequestDispatcher("/WEB-INF/jsp/application_confirmation.jsp").forward(request, response);
                    return;
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Application not found");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid application ID format");
                return;
            }
         // リストページを表示
        }
            request.getRequestDispatcher("/WEB-INF/jsp/application_list.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}



