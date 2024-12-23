package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private static final String DEFAULT_DATE_TIME = "未定";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Application_approval servlet is called.");

        String applicationIdParam = request.getParameter("id");
        if (applicationIdParam == null || applicationIdParam.isEmpty()) {
            handleError(request, response, "申請IDが指定されていません");
            return;
        }

        try {
            int applicationId = Integer.parseInt(applicationIdParam);
            System.out.println("Application ID received: " + applicationId);

            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
            LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

            if (applicationDetails == null) {
                handleError(request, response, "指定された申請データが見つかりません");
                return;
            }

            // 日時のフォーマット
            LocalDateTime dateTime = applicationDetails.getDatetime();
            String formattedDateTime = (dateTime != null) ? dateTime.format(DATE_TIME_FORMATTER) : DEFAULT_DATE_TIME;
            System.out.println("Formatted Datetime: " + formattedDateTime);

            // リクエストスコープにデータをセット
            request.setAttribute("application", applicationDetails);
            request.setAttribute("formattedDateTime", formattedDateTime);

            // JSP にフォワード
            request.getRequestDispatcher("WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            handleError(request, response, "無効な申請ID形式です");
            e.printStackTrace();
        } catch (Exception e) {
            handleError(request, response, "エラーが発生しました");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * エラー処理を共通化
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        System.err.println("Error: " + errorMessage);
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
    }
}
