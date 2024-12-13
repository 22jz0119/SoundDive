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
            // クエリパラメータで送信された userId を取得
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
            } else {
                System.err.println("[WARN] userId が指定されていません。");
            }

            // ライブハウス情報を取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.get();

            if (livehouseList == null || livehouseList.isEmpty()) {
                System.err.println("ライブハウス情報が見つかりませんでした。");
            } else {
                System.err.println("ライブハウス情報が取得されました: " + livehouseList.size() + " 件");
            }

            // userId とライブハウス情報をリクエストスコープに保存
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

            // クエリパラメータで送信された userId を取得
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

            String searchQuery = request.getParameter("q");
            System.err.println("検索クエリ: " + searchQuery);

            // 検索クエリを使用
            List<Livehouse_information> livehouseList = livehouseInformationDAO.searchLivehouses(searchQuery);

            request.setAttribute("livehouseList", livehouseList);

            // 次の画面にリダイレクト
            if (userId != null) {
                String nextPageUrl = request.getContextPath() + "/At_details?userId=" + userId;
                System.out.println("[DEBUG] Redirecting to: " + nextPageUrl);
                response.sendRedirect(nextPageUrl);
                return;
            }

            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("ライブハウス検索中にエラーが発生しました。");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス検索中にエラーが発生しました。");
        }
    }

}
