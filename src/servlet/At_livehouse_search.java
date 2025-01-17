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

 // GETメソッド（ライブハウス情報の取得）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // userId と applicationId のパラメータを取得
            String userIdParam = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");
            Integer userId = null;
            Integer applicationId = null;
            
         // サーブレット内で artistId を取得
            Integer artistId = (Integer) request.getAttribute("artistId");

            if (artistId != null) {
                System.out.println("Artist ID from request: " + artistId);
            } else {
                System.out.println("Artist ID not found in request.");
            }


            // パラメータのデバッグ
            System.out.println("[DEBUG] Received userIdParam: " + userIdParam);
            System.out.println("[DEBUG] Received applicationIdParam: " + applicationIdParam);

            // userId と applicationId のパラメータを適切にパース
            if (userIdParam != null && !userIdParam.isEmpty()) {
                try {
                    userId = Integer.parseInt(userIdParam);
                    System.out.println("[DEBUG] Parsed userId: " + userId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid userId format: " + userIdParam);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な userId 形式です。");
                    return;
                }
            }

            if (applicationIdParam != null && !applicationIdParam.isEmpty()) {
                try {
                    applicationId = Integer.parseInt(applicationIdParam);
                    System.out.println("[DEBUG] Parsed applicationId: " + applicationId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid applicationId format: " + applicationIdParam);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な applicationId 形式です。");
                    return;
                }
            }

            // ライブハウス情報をデータベースから取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.get();
            System.out.println("[DEBUG] Retrieved livehouseList size: " + livehouseList.size());

            // リクエストスコープにデータをセット
            request.setAttribute("livehouseList", livehouseList);

            // livehouse_typeの判定
            String livehouseType = request.getParameter("livehouse_type");

            // ソロまたはマルチライブの処理
            if ("solo".equals(livehouseType)) {
                // ソロライブの場合はlivehouseIdのみ渡す
                System.out.println("[DEBUG] ソロライブ処理開始");
                System.out.println("[DEBUG] Received livehouse_type: solo");
                request.setAttribute("livehouseType", "solo");
            } else if ("multi".equals(livehouseType)) {
                // マルチライブの場合は他のパラメータも渡す
                System.out.println("[DEBUG] マルチライブ処理開始");
                request.setAttribute("livehouseType", "multi");
                request.setAttribute("userId", userId);
                request.setAttribute("applicationId", applicationId);
            } else {
                // livehouse_typeが無効な場合
                System.out.println("[DEBUG] Unknown livehouse_type received: " + livehouseType);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスタイプが指定されました。");
                return;
            }

            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[ERROR] ライブハウス情報の取得中にエラーが発生しました。");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス情報の取得中にエラーが発生しました。");
        }
    }

    // POSTメソッド（検索処理）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            // userId と applicationId のパラメータを取得
            String userIdParam = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");
            Integer userId = null;
            Integer applicationId = null;

            // userId と applicationId のパラメータを適切にパース
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

            // 検索クエリの取得（ライブハウス名等）
            String searchQuery = request.getParameter("q");
            System.out.println("[DEBUG] Search query: " + searchQuery);

            // 検索結果を取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.searchLivehouses(searchQuery);

            // リクエストスコープに検索結果をセット
            request.setAttribute("livehouseList", livehouseList);

            // livehouse_typeの判定
            String livehouseType = request.getParameter("livehouse_type");

            if ("solo".equals(livehouseType)) {
                // ソロライブの場合
                System.out.println("[DEBUG] ソロライブ処理開始");
                request.setAttribute("livehouseType", "solo");
            } else if ("multi".equals(livehouseType)) {
                // マルチライブの場合
                System.out.println("[DEBUG] マルチライブ処理開始");
                request.setAttribute("livehouseType", "multi");
                request.setAttribute("userId", userId);
                request.setAttribute("applicationId", applicationId);
            }

            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[ERROR] ライブハウス検索中にエラーが発生しました。");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス検索中にエラーが発生しました。");
        }
    }
}
