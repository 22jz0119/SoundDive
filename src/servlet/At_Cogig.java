package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.NoticeDAO;
import model.Artist_group;
import model.Notice;

@WebServlet("/At_Cogig")
public class At_Cogig extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Artist_groupDAO artistGroupDAO;
    private NoticeDAO noticeDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DAO を初期化
        DBManager dbManager = DBManager.getInstance();
        
        
        artistGroupDAO = Artist_groupDAO.getInstance(dbManager);
        noticeDAO = NoticeDAO.getInstance(dbManager);

        String query = request.getParameter("q"); // 検索クエリを取得
        List<Artist_group> artistGroups;

        if (query != null && !query.isEmpty()) {
            // 名前検索を実行
            artistGroups = artistGroupDAO.searchGroupsByName(query);
        } else {
            // 全グループを取得
            artistGroups = artistGroupDAO.getAllGroups();
        }

        // 各グループのメンバー数を取得
        Map<Integer, Integer> memberCounts = artistGroupDAO.getMemberCounts();

        // リクエスト属性にデータを設定
        request.setAttribute("artistGroups", artistGroups);
        request.setAttribute("memberCounts", memberCounts);

        request.getRequestDispatcher("WEB-INF/jsp/artist/at_cogig.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("[DEBUG] Action received: " + action);

        if ("apply".equals(action)) {
            String userIdParam = request.getParameter("applicationId");
            System.out.println("[DEBUG] applicationId received: " + userIdParam);

            if (userIdParam != null) {
                try {
                    int userId = Integer.parseInt(userIdParam);
                    System.out.println("[DEBUG] Parsed userId: " + userId);

                    // 対象のアーティストを取得
                    Artist_group artist = artistGroupDAO.getGroupByUserId(userId);
                    if (artist != null) {
                        System.out.println("[DEBUG] Artist found: " + artist.getAccount_name());

                        // ライブハウス申請を作成（申請IDは後で通知に使う）
                        int livehouseApplicationId = livehouseApplicationDAO.createApplication(userId, artist.getId());

                        // 通知を作成して挿入（まだlivehouse_application_idはnull）
                        String message = artist.getAccount_name() + " から対バン申請がありました。";
                        Notice notice = new Notice(0, null, null, null, message, false); // `livehouse_application_id` はまだnull
                        boolean success = noticeDAO.addNotice(notice);

                        if (success) {
                            System.out.println("[DEBUG] Notice added successfully.");

                            // ライブハウス申請IDが決まったら通知を更新
                            noticeDAO.updateNoticeWithApplicationId(notice.getId(), livehouseApplicationId);

                            response.sendRedirect(request.getContextPath() + "/At_Cogig?success=通知を送信しました。");
                            return;
                        } else {
                            System.err.println("[ERROR] Failed to add notice.");
                            response.sendRedirect(request.getContextPath() + "/At_Cogig?error=通知の送信に失敗しました。");
                            return;
                        }
                    } else {
                        System.err.println("[ERROR] Artist not found for userId: " + userId);
                        response.sendRedirect(request.getContextPath() + "/At_Cogig?error=対象のアーティストが見つかりません。");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid userId format: " + userIdParam);
                    response.sendRedirect(request.getContextPath() + "/At_Cogig?error=無効なユーザーIDです。");
                    return;
                } catch (Exception e) {
                    System.err.println("[ERROR] Exception occurred while processing the request.");
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/At_Cogig?error=処理中にエラーが発生しました。");
                    return;
                }
            } else {
                System.err.println("[ERROR] applicationId is null.");
                response.sendRedirect(request.getContextPath() + "/At_Cogig?error=ユーザーIDが指定されていません。");
                return;
            }
        }

        // 他のアクションは GET にリダイレクト
        doGet(request, response);
    }
}
