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
            // userId パラメータを取得
            String userIdParam = request.getParameter("userId");
            Integer userId = null;
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

            // ライブハウス情報の取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.get();

            // リクエストスコープにセット
            if (userId != null) {
                request.setAttribute("userId", userId);
            }
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

            // userId パラメータを取得
            String userIdParam = request.getParameter("userId");
            Integer userId = null;
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

            // 検索クエリの取得
            String searchQuery = request.getParameter("q");
            System.err.println("検索クエリ: " + searchQuery);

            // 検索結果を取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.searchLivehouses(searchQuery);

            // リクエストスコープにセット
            request.setAttribute("livehouseList", livehouseList);

            // ライブハウスの詳細ページへリダイレクト
            if (userId != null) {
                String livehouseIdParam = request.getParameter("livehouseId");
                if (livehouseIdParam != null) {
                    try {
                        Integer livehouseId = Integer.parseInt(livehouseIdParam);
                        String nextPageUrl = request.getContextPath() + "/At_details?userId=" + userId + "&livehouseId=" + livehouseId;
                        System.out.println("[DEBUG] Redirecting to: " + nextPageUrl);
                        response.sendRedirect(nextPageUrl);
                        return;
                    } catch (NumberFormatException e) {
                        System.err.println("[ERROR] Invalid livehouseId format: " + livehouseIdParam);
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な livehouseId 形式です。");
                        return;
                    }
                }
            }

            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("ライブハウス検索中にエラーが発生しました。");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス検索中にエラーが発生しました。");
        }
    }
}
