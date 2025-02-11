package servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.Artist_group;

@WebServlet("/At_Cogig")
public class At_Cogig extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Artist_groupDAO artistGroupDAO;
    private Livehouse_applicationDAO livehouseApplicationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = DBManager.getInstance();
        artistGroupDAO = Artist_groupDAO.getInstance(dbManager);
        livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
        System.out.println("[DEBUG] At_Cogig Servlet Initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	if (!isLoggedIn(request, response)) {
	            return;
	        }
	
	        HttpSession session = request.getSession();
	        Integer loggedInUserId = (Integer) session.getAttribute("userId");
	
	        if (loggedInUserId == null) {
	            response.sendRedirect(request.getContextPath() + "/Top");
	            return;
	        }
	        try {
	            // livehouse_type をパラメータとして受け取る
	            String livehouseType = request.getParameter("livehouse_type");
	            System.out.println("[DEBUG] Received livehouse_type: " + livehouseType);
	
	            // livehouse_type が null の場合は "multi" に設定
	            if (livehouseType == null) {
	                livehouseType = "multi"; // デフォルト値
	                System.out.println("[DEBUG] livehouse_type is null, setting default value to 'multi'.");
	            }

            // 'multi' の場合のみ処理
            if (livehouseType.equals("multi")) {
                // 検索クエリを受け取る
                String query = request.getParameter("q");
                System.out.println("[DEBUG] Received search query: " + query);
                List<Artist_group> artistGroups;

                // 検索クエリがあれば、それでフィルタリング
                if (query != null && !query.isEmpty()) {
                    artistGroups = artistGroupDAO.searchGroupsByName(query);
                } else {
                    artistGroups = artistGroupDAO.getAllGroupsNotMyUserId(loggedInUserId);
                }

                // 重複を排除するために user_id でフィルタリング
                Map<Integer, Artist_group> uniqueArtists = new HashMap<>();
                for (Artist_group artist : artistGroups) {
                    uniqueArtists.putIfAbsent(artist.getUser_id(), artist);
                }

                // JSTLで利用できる形でデータをリクエストスコープに設定
                request.setAttribute("artistGroups", uniqueArtists.values());

                // メンバー数データもリクエストスコープに設定
                Map<Integer, Integer> memberCounts = artistGroupDAO.getMemberCounts();
                request.setAttribute("memberCounts", memberCounts);

                // 'multi' に関連するアーティストグループがあれば表示
                request.getRequestDispatcher("WEB-INF/jsp/artist/at_cogig.jsp").forward(request, response);
            } else {
                // 'multi' 以外の livehouse_type が指定された場合、エラー処理なしに通常の処理を続ける
                System.out.println("[ERROR] Invalid livehouse_type or not specified. Proceeding to default behavior.");

                // 検索クエリを受け取る
                String query = request.getParameter("q");
                System.out.println("[DEBUG] Received search query: " + query);
                List<Artist_group> artistGroups;

                // 検索クエリがあれば、それでフィルタリング
                if (query != null && !query.isEmpty()) {
                    artistGroups = artistGroupDAO.searchGroupsByName(query);
                } else {
                    artistGroups = artistGroupDAO.getAllGroups();
                }

                // 重複を排除するために user_id でフィルタリング
                Map<Integer, Artist_group> uniqueArtists = new HashMap<>();
                for (Artist_group artist : artistGroups) {
                    uniqueArtists.putIfAbsent(artist.getUser_id(), artist);
                }

                // JSTLで利用できる形でデータをリクエストスコープに設定
                request.setAttribute("artistGroups", uniqueArtists.values());

                // メンバー数データもリクエストスコープに設定
                Map<Integer, Integer> memberCounts = artistGroupDAO.getMemberCounts();
                request.setAttribute("memberCounts", memberCounts);

                // URLエンコードを適用してからリダイレクト
                try {
                    // クエリパラメータがnullでないことを確認してからエンコード
                    String redirectUrl = request.getRequestURI();
                    if (query != null && !query.isEmpty()) {
                        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
                        redirectUrl = redirectUrl + "?livehouse_type=multi&q=" + encodedQuery;
                    } else {
                        redirectUrl = redirectUrl + "?livehouse_type=multi"; // qがnullの場合
                    }

                    System.out.println("[DEBUG] Redirecting to: " + redirectUrl);  // リダイレクト先URLをログに出力
                    response.sendRedirect(redirectUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/At_Cogig?error=エラーが発生しました。");
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception in doGet method.");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/At_Cogig?error=エラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isLoggedIn(request, response)) {
            return; // ログインしていなければ処理を中断
        }

        String action = request.getParameter("action");

        if ("apply".equals(action)) {
            String applicationIdParam = request.getParameter("applicationId");
            String livehouseType = request.getParameter("livehouse_type"); // livehouse_type を取得
            if (livehouseType == null) {
                livehouseType = "multi"; // デフォルト値を設定
            }
            System.out.println("[DEBUG] livehouse_type in doPost: " + livehouseType);

            if (applicationIdParam != null) {
                try {
                    int artistId = Integer.parseInt(applicationIdParam); // 申請先のアーティストID
                    System.out.println("[DEBUG] Parsed artistId: " + artistId);
                    
                    // ログイン中のユーザーIDをセッションから取得
                    Integer userId = (Integer) request.getSession().getAttribute("userId");
                    System.out.println("[DEBUG] Logged-in userId: " + userId);

                    // アーティスト情報を取得
                    Artist_group artist = artistGroupDAO.getGroupById(artistId);
                    if (artist != null) {
                        System.out.println("[DEBUG] Artist found: " + artist.getAccount_name() + " (id: " + artist.getId() + ")");

                        // ライブハウス申請をデータベースに保存
                        int applicationId = livehouseApplicationDAO.createApplication(
                            userId,                  // ログイン中のユーザーID
                            null,                    // livehouseInformationId
                            null,                    // datetime: nullの場合
                            false,                   // trueFalse
                            null,                    // startTime: nullの場合
                            null,                    // finishTime: nullの場合
                            2,                       // cogigOrSolo: 固定値
                            artist.getId()           // artist_group_id（申請先のアーティストID）
                        );
                        
                     // リクエストスコープに artistId を設定
                        request.setAttribute("artistId", artistId);  // リクエストスコープに設定

                        if (applicationId > 0) {
                            System.out.println("[DEBUG] Created livehouse application with ID: " + applicationId);

                            // セッションに applicationId を保存
                            HttpSession session = request.getSession();
                            session.setAttribute("applicationId", applicationId);

                            // リダイレクトで userId と applicationId と livehouse_type を渡す
                            String redirectUrl = String.format(
                                "%s/At_livehouse_search?userId=%d&applicationId=%d&livehouse_type=%s",
                                request.getContextPath(),
                                userId,
                                applicationId,
                                URLEncoder.encode(livehouseType, StandardCharsets.UTF_8.name())
                            );

                            System.out.println("[DEBUG] Redirecting to: " + redirectUrl);
                            response.sendRedirect(redirectUrl);
                            return; // 処理を終了
                        } else {
                            System.err.println("[ERROR] Failed to create livehouse application.");
                            request.setAttribute("errorMessage", "ライブハウス申請の作成に失敗しました。");
                        }
                    } else {
                        System.err.println("[ERROR] Artist not found for id: " + artistId);
                        request.setAttribute("errorMessage", "対象のアーティストが見つかりません。");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid applicationId format: " + applicationIdParam);
                    request.setAttribute("errorMessage", "無効なIDです。");
                } catch (Exception e) {
                    System.err.println("[ERROR] Exception occurred while processing the request.");
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "処理中にエラーが発生しました。");
                }
            } else {
                System.err.println("[ERROR] applicationId is null.");
                request.setAttribute("errorMessage", "IDが指定されていません。");
            }
        }

        // 現在のページを再表示
        doGet(request, response);
    }


    /**
     * ログイン状態をチェックする共通メソッド
     */
    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false); // セッションが存在するか確認
        if (session == null) {
            System.err.println("[ERROR] No session found. Redirecting to top page.");
            response.sendRedirect(request.getContextPath() + "/Top"); // セッションがない場合はログインページへ
            return false;
        }

        Integer userId = (Integer) session.getAttribute("userId"); // セッションから userId を取得
        if (userId == null) {
            System.err.println("[ERROR] User is not logged in. Redirecting to top page.");
            response.sendRedirect(request.getContextPath() + "/Top"); // ログインしていない場合はトップページへリダイレクト
            return false;
        }

        return true;
    }
}
