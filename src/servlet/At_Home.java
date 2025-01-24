package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.Livehouse_informationDAO;
import dao.NoticeDAO;
import model.Livehouse_application;
import model.Livehouse_information;
import model.Notice;
import service.NotificationService;

@WebServlet("/At_Home")
public class At_Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ログイン状態確認
        if (!isLoggedIn(request, response)) {
            return;
        }

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/Top");
            return;
        }

        try {
            // DAOインスタンスを取得
            DBManager dbManager = DBManager.getInstance();
            NoticeDAO noticeDAO = new NoticeDAO(dbManager);
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);
            Livehouse_informationDAO informationDAO = new Livehouse_informationDAO(dbManager);

            // true/false に基づく申請情報を一括取得
            List<Livehouse_application> applicationsTrue = applicationDAO.getApplicationsByUserId(userId, true);
            List<Livehouse_application> applicationsFalse = applicationDAO.getApplicationsByUserId(userId, false);

            // サービス層で処理
            NotificationService notificationService = new NotificationService(dbManager);
            List<Notice> notifications = notificationService.getUserNotifications(userId);
            request.setAttribute("notifications", notifications);

            // 通知の取得状況をログ出力
            System.out.println("[DEBUG] Retrieved " + notifications.size() + " notifications for user ID: " + userId);

            // 必要なライブハウス情報IDを抽出
            Set<Integer> livehouseIds = new HashSet<>();
            applicationsTrue.forEach(app -> livehouseIds.add(app.getLivehouse_information_id()));
            applicationsFalse.forEach(app -> livehouseIds.add(app.getLivehouse_information_id()));

            // ライブハウス情報をバッチで取得
            Map<Integer, Livehouse_information> livehouseInfoMap = informationDAO.findLivehouseInformationByIds(new ArrayList<>(livehouseIds));

            // 申請情報に対応するライブハウス情報をセット
            applicationsTrue.forEach(app -> app.setLivehouse_information(livehouseInfoMap.get(app.getLivehouse_information_id())));
            applicationsFalse.forEach(app -> app.setLivehouse_information(livehouseInfoMap.get(app.getLivehouse_information_id())));

            // リクエスト属性にセット
            request.setAttribute("applicationsTrue", applicationsTrue);
            request.setAttribute("applicationsFalse", applicationsFalse);

            // JSPに転送
            request.getRequestDispatcher("WEB-INF/jsp/artist/at_home.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースエラー");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isLoggedIn(request, response)) {
            return;
        }

        String action = request.getParameter("action");

        try {
            DBManager dbManager = DBManager.getInstance();
            NotificationService notificationService = new NotificationService(dbManager);

            // 各アクションに対応する処理
            switch (action) {
                case "markAsRead": // 通知を既読にする処理
                    handleMarkAsRead(request, response, notificationService);
                    break;

                case "logout": // ログアウト処理
                    logout(request.getSession());
                    response.sendRedirect(request.getContextPath() + "/Top");
                    break;

                case "solo": // SOLO LIVE への遷移
                    response.sendRedirect(request.getContextPath() + "/At_livehouse_search?livehouse_type=solo");
                    break;

                case "multi": // MULTI LIVE への遷移
                    response.sendRedirect(request.getContextPath() + "/At_Cogig?livehouse_type=multi");
                    break;

                default: // デフォルトのリダイレクト
                    response.sendRedirect(request.getContextPath() + "/At_Home");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
        }
    }

    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response, NotificationService notificationService) throws IOException {
        String noticeIdParam = request.getParameter("noticeId");
        System.out.println("[DEBUG] Received noticeId: " + noticeIdParam); // デバッグ出力

        if (noticeIdParam != null) {
            try {
                int noticeId = Integer.parseInt(noticeIdParam);

                // 通知を既読にする
                notificationService.markAsRead(noticeId);

                System.out.println("[DEBUG] Notification ID " + noticeId + " marked as read.");
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":\"success\"}");
                response.setStatus(HttpServletResponse.SC_OK); // 成功レスポンス
            } catch (NumberFormatException e) {
                System.err.println("[ERROR] Invalid notice ID: " + noticeIdParam);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な通知IDです。");
            } catch (RuntimeException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "通知の更新に失敗しました。");
            }
        } else {
            System.err.println("[ERROR] No noticeId parameter provided");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "通知IDが指定されていません。");
        }
    }

    
    private void logout(HttpSession session) {
        if (isLoggedIn(session)) {
            Integer userId = (Integer) session.getAttribute("userId");
            System.out.println("Logging out user with ID: " + userId + ". Session ID: " + session.getId());

            session.removeAttribute("userId");
            session.invalidate();

            System.out.println("User with ID: " + userId + " logged out successfully. Session invalidated.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/Top");
            return false;
        }
        return true;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }
}
