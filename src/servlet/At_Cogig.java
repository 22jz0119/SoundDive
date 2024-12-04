package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.NoticeDAO;
import model.Artist_group;
import model.Notice;

@WebServlet("/At_Cogig")
public class At_Cogig extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Artist_groupDAO artistGroupDAO;
    private NoticeDAO noticeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = DBManager.getInstance();
        artistGroupDAO = Artist_groupDAO.getInstance(dbManager);
        noticeDAO = NoticeDAO.getInstance(dbManager); // NoticeDAO の初期化を追加
        System.out.println("[DEBUG] At_Cogig Servlet Initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String query = request.getParameter("q");
            System.out.println("[DEBUG] Received search query: " + query);
            List<Artist_group> artistGroups;

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

            request.getRequestDispatcher("WEB-INF/jsp/artist/at_cogig.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[ERROR] Exception in doGet method.");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/At_Cogig?error=エラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("apply".equals(action)) {
            String applicationIdParam = request.getParameter("applicationId");
            System.out.println("[DEBUG] Received applicationId (id): " + applicationIdParam);

            if (applicationIdParam != null) {
                try {
                    int artistId = Integer.parseInt(applicationIdParam);
                    System.out.println("[DEBUG] Parsed artistId: " + artistId);

                    // DAO初期化
                    DBManager dbManager = DBManager.getInstance();
                    Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);
                    Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

                    // アーティスト情報を取得
                    Artist_group artist = artistGroupDAO.getGroupById(artistId);
                    if (artist != null) {
                        System.out.println("[DEBUG] Artist found: " + artist.getAccount_name() + " (id: " + artist.getId() + ")");

                        // ライブハウス申請をデータベースに保存
                        int livehouseApplicationId = livehouseApplicationDAO.createApplication(
                        	    artist.getUser_id(), // user_id
                        	    artistId,            // livehouseInformationId
                        	    null,                // datetime: nullにする
                        	    false,               // trueFalse
                        	    null,                // startTime: nullにする
                        	    null                 // finishTime: nullにする
                        	);

                        if (livehouseApplicationId > 0) {
                            System.out.println("[DEBUG] Created livehouse application with ID: " + livehouseApplicationId);

                            // 通知を作成
                            String message = artist.getAccount_name() + " から対バン申請がありました。";
                            Notice notice = new Notice(0, livehouseApplicationId, LocalDate.now(), LocalDate.now(), message, false);

                            NoticeDAO noticeDAO = NoticeDAO.getInstance(dbManager);
                            boolean success = noticeDAO.addNotice(notice);

                            if (success) {
                                System.out.println("[DEBUG] Notice added successfully.");
                                response.sendRedirect(request.getContextPath() + "/At_Cogig?success=通知を送信しました。");
                            } else {
                                System.err.println("[ERROR] Failed to add notice.");
                                response.sendRedirect(request.getContextPath() + "/At_Cogig?error=通知の送信に失敗しました。");
                            }
                        } else {
                            System.err.println("[ERROR] Failed to create livehouse application.");
                            response.sendRedirect(request.getContextPath() + "/At_Cogig?error=ライブハウス申請の作成に失敗しました。");
                        }
                    } else {
                        System.err.println("[ERROR] Artist not found for id: " + artistId);
                        response.sendRedirect(request.getContextPath() + "/At_Cogig?error=対象のアーティストが見つかりません。");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid applicationId format: " + applicationIdParam);
                    response.sendRedirect(request.getContextPath() + "/At_Cogig?error=無効なIDです。");
                } catch (Exception e) {
                    System.err.println("[ERROR] Exception occurred while processing the request.");
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/At_Cogig?error=処理中にエラーが発生しました。");
                }
            } else {
                System.err.println("[ERROR] applicationId is null.");
                response.sendRedirect(request.getContextPath() + "/At_Cogig?error=IDが指定されていません。");
            }
        } else {
            doGet(request, response);
        }
    }
}
