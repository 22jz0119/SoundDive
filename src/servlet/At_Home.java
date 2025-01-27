package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
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
import model.Livehouse_application;
import model.Livehouse_information;
import service.NotificationService;

@WebServlet("/At_Home")
public class At_Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);
            Livehouse_informationDAO informationDAO = new Livehouse_informationDAO(dbManager);
            NotificationService notificationService = new NotificationService(dbManager);
            
            List<model.Notice> notifications = notificationService.getUserNotifications(userId);

            // 通知をログに出力
            if (notifications.isEmpty()) {
                System.out.println("[DEBUG] User ID " + userId + " has no notifications.");
            } else {
                System.out.println("[DEBUG] Notifications for User ID " + userId + ":");
                for (model.Notice notice : notifications) {
                    System.out.println("  - ID: " + notice.getId());
                    System.out.println("    Message: " + notice.getMessage());
                    System.out.println("    Create Date: " + notice.getCreateDate());
                    System.out.println("    Is Read: " + notice.isRead());
                }
            }

            // リクエスト属性に通知をセット
            request.setAttribute("notifications", notifications);

            // DateTimeFormatterを定義
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // true/false に基づく申請情報を一括取得
            List<Livehouse_application> applicationsTrue = applicationDAO.getApplicationsByUserId(userId, true);
            List<Livehouse_application> applicationsFalse = applicationDAO.getApplicationsByUserId(userId, false);

            // ライブハウス情報IDを収集
            Set<Integer> livehouseIds = new HashSet<>();
            applicationsTrue.forEach(app -> livehouseIds.add(app.getLivehouse_information_id()));
            applicationsFalse.forEach(app -> livehouseIds.add(app.getLivehouse_information_id()));

            // デバッグ: 収集したライブハウスIDを確認
            System.out.println("[DEBUG] Collected Livehouse IDs: " + livehouseIds);

            // ライブハウス情報をバッチで取得
            Map<Integer, Livehouse_information> livehouseInfoMap = informationDAO.findLivehouseInformationByIds(new ArrayList<>(livehouseIds));

            // 申請情報に対応するライブハウス情報をセット
            applicationsTrue.forEach(app -> {
                Livehouse_information livehouseInfo = livehouseInfoMap.get(app.getLivehouse_information_id());
                if (livehouseInfo != null) {
                    app.setLivehouse_information(livehouseInfo);
                }
            });

            applicationsFalse.forEach(app -> {
                Livehouse_information livehouseInfo = livehouseInfoMap.get(app.getLivehouse_information_id());
                if (livehouseInfo != null) {
                    app.setLivehouse_information(livehouseInfo);
                }
            });

            // 必要なリクエスト属性にセット
            request.setAttribute("applicationsTrue", applicationsTrue);
            request.setAttribute("applicationsFalse", applicationsFalse);

            // JSPに転送
            request.getRequestDispatcher("WEB-INF/jsp/artist/at_home.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "データベースエラーが発生しました。管理者にお問い合わせください。");
            request.getRequestDispatcher("WEB-INF/jsp/artist/at_home.jsp").forward(request, response);
            return;
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
