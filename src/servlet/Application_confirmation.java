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

@WebServlet("/Application_confirmation")
public class Application_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                int applicationId = Integer.parseInt(idParam);

                // 詳細データを取得
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

                if (applicationDetails != null) {
                    // データをリクエストスコープにセット
                    request.setAttribute("applicationDetails", applicationDetails);

                    // JSPにフォワード
                    request.getRequestDispatcher("/WEB-INF/jsp/application_confirmation.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Application not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid application ID format");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No application ID provided");
        }
        String applicationId = request.getParameter("applicationId"); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
