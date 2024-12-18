package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/At_livehouse_search")
public class At_livehouse_search extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Livehouse_informationDAO livehouseInformationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = DBManager.getInstance();
        livehouseInformationDAO = new Livehouse_informationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // userId と applicationId パラメータを取得
            String userIdParam = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");
            Integer userId = null;
            Integer applicationId = null;

            if (userIdParam != null && !userIdParam.isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdParam);
                    System.out.println("[DEBUG] Received userId: " + userId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid userId format: " + userIdParam);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な userId 形式です。");
                    return;
                }
            }

            if (applicationIdParam != null && !applicationIdParam.isEmpty()) {
                try {
                    applicationId = Integer.parseInt(applicationIdParam);
                    System.out.println("[DEBUG] Received applicationId: " + applicationId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid applicationId format: " + applicationIdParam);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な applicationId 形式です。");
                    return;
                }
            }

            // ライブハウス情報の取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.get();

            // リクエストスコープにセット
            if (userId != null) request.setAttribute("userId", userId);
            if (applicationId != null) request.setAttribute("applicationId", applicationId);
            request.setAttribute("livehouseList", livehouseList);

            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("ライブハウス情報の取得中にエラーが発生しました。");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス情報の取得中にエラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            // userId と applicationId パラメータを取得
            String userIdParam = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");
            Integer userId = null;
            Integer applicationId = null;

            if (userIdParam != null && !userIdParam.isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdParam);
                    System.out.println("[DEBUG] Received userId: " + userId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid userId format: " + userIdParam);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な userId 形式です。");
                    return;
                }
            }

            if (applicationIdParam != null && !applicationIdParam.isEmpty()) {
                try {
                    applicationId = Integer.parseInt(applicationIdParam);
                    System.out.println("[DEBUG] Received applicationId: " + applicationId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid applicationId format: " + applicationIdParam);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な applicationId 形式です。");
                    return;
                }
            }

            // 検索クエリの取得
            String searchQuery = request.getParameter("q");
            System.out.println("[DEBUG] Search query: " + searchQuery);

            // 検索結果を取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.searchLivehouses(searchQuery);

            // リクエストスコープにセット
            request.setAttribute("livehouseList", livehouseList);
            if (userId != null) request.setAttribute("userId", userId);
            if (applicationId != null) request.setAttribute("applicationId", applicationId);

            // 次のページにパラメータを渡してフォワードまたはリダイレクト
            if (userId != null && applicationId != null) {
                // リダイレクトして次の画面に userId と applicationId を渡す
                String nextPageUrl = request.getContextPath() + "/At_next_page?userId=" + userId + "&applicationId=" + applicationId;
                System.out.println("[DEBUG] Redirecting to: " + nextPageUrl);
                response.sendRedirect(nextPageUrl);
            } else {
                // userId または applicationId がない場合はエラーページを表示
                request.setAttribute("errorMessage", "必要なパラメータが指定されていません。");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("ライブハウス検索中にエラーが発生しました。");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス検索中にエラーが発生しました。");
        }
    }
}
