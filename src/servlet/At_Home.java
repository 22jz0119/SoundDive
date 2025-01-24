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
            
            List<Notice> notifications = noticeDAO.getNotificationsByUserId(userId);
            
            // 通知の取得状況をログ出力
            System.out.println("[DEBUG] Retrieved " + notifications.size() + " notifications for user ID: " + userId);
            for (Notice notice : notifications) {
                System.out.println("[DEBUG] Notification ID: " + notice.getId());
                System.out.println("[DEBUG] Message: " + notice.getMessage());
                System.out.println("[DEBUG] Create Date: " + notice.getCreateDate());
                System.out.println("[DEBUG] Update Date: " + notice.getUpdateDate());
                System.out.println("[DEBUG] Is Read: " + notice.isRead());
            }

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
            
            request.setAttribute("notifications", notifications);

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

        // ログアウト処理の追加
        if ("logout".equals(action)) {
            logout(request.getSession());  // ログアウト実行
            response.sendRedirect(request.getContextPath() + "/Top");  // ログアウト後にトップページへリダイレクト
            return;
        }

        String redirectPath = switch (action) {
            case "solo" -> "/At_livehouse_search?livehouse_type=solo";
            case "multi" -> "/At_Cogig?livehouse_type=multi";
            default -> "/At_Home";
        };

        response.sendRedirect(request.getContextPath() + redirectPath);
    }

    private void logout(HttpSession session) {
        if (isLoggedIn(session)) {
            // ログアウト前のログ
            Integer userId = (Integer) session.getAttribute("userId");
            System.out.println("Logging out user with ID: " + userId + ". Session ID: " + session.getId());

            // ユーザーIDをセッションから削除し、セッション無効化
            session.removeAttribute("userId");
            session.invalidate();

            // ログアウト後のログ
            System.out.println("User with ID: " + userId + " logged out successfully. Session invalidated.");
        } else {
            // ログインしていない場合のログ
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
