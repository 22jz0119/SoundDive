package servlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

@WebServlet("/At_Home")
public class At_Home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isLoggedIn(request, response)) {
            return;
        }

        // ログイン中のユーザーIDをセッションから取得
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // デバッグ: ユーザーIDの確認
        System.out.println("[DEBUG] userId from session: " + userId);

        if (userId == null) {
            // ユーザーIDがセッションに存在しない場合
            System.out.println("[DEBUG] userId is null, redirecting to /Top");
            response.sendRedirect(request.getContextPath() + "/Top");
            return;
        }

        // DBManagerを使ってシングルトンインスタンスを取得
        DBManager dbManager = DBManager.getInstance(); // getInstance() を使ってインスタンスを取得

        // ユーザーIDに基づいてライブハウス申請情報を取得
        try {
            // Livehouse_applicationDAOをDBManagerを渡してインスタンス化
            Livehouse_applicationDAO dao = new Livehouse_applicationDAO(dbManager);
            System.out.println("[DEBUG] DAO instance created: " + dao);
            
            List<Livehouse_application> applications = dao.getApplicationsByUserId(userId);

            // Livehouse_informationDAOを使用して、各申請に関連するライブハウス情報を取得
            Livehouse_informationDAO livehouseInfoDAO = new Livehouse_informationDAO(dbManager);

            // 申請情報に関連するライブハウス情報を追加
            for (Livehouse_application app : applications) {
                // livehouse_information_idを使用してLivehouse_informationを取得
                Livehouse_information livehouseInfo = livehouseInfoDAO.getLivehouse_informationById(app.getLivehouse_information_id());
                app.setLivehouse_information(livehouseInfo);  // Livehouse_informationをセット
            }

            // デバッグ: 取得した申請情報の確認
            System.out.println("[DEBUG] Retrieved " + applications.size() + " applications for userId: " + userId);
            if (!applications.isEmpty()) {
                for (Livehouse_application app : applications) {
                    System.out.println("[DEBUG] Application ID: " + app.getId() + ", Date: " + app.getDate_time());
                    
                    // Livehouse_informationがnullでない場合のみ、ライブハウス情報を表示
                    if (app.getLivehouse_information() != null) {
                        System.out.println("[DEBUG] Livehouse Info: " + app.getLivehouse_information().getLivehouse_name());
                    } else {
                        System.out.println("[DEBUG] No Livehouse Information available for Application ID: " + app.getId());
                    }
                }
            } else {
                System.out.println("[DEBUG] No applications found for userId: " + userId);
            }

            // 取得した情報をリクエストにセット
            request.setAttribute("applications", applications);

            // リクエストをJSPに転送
            System.out.println("[DEBUG] Forwarding to at_home.jsp.");
            request.getRequestDispatcher("WEB-INF/jsp/at_home.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Database error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースエラー");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isLoggedIn(request, response)) {
            return;
        }

        String action = request.getParameter("action");

        // デバッグ: リクエストパラメータの確認
        System.out.println("[DEBUG] action from request: " + action);

        if ("solo".equals(action)) {
            System.out.println("[DEBUG] Solo live action triggered.");
            response.sendRedirect(request.getContextPath() + "/SoloLiveServlet");
        } else if ("multi".equals(action)) {
            System.out.println("[DEBUG] Multi live action triggered.");
            response.sendRedirect(request.getContextPath() + "/MultiLiveServlet");
        } else {
            System.out.println("[DEBUG] Unknown action: " + action);
            response.sendRedirect(request.getContextPath() + "/At_Home");
        }
    }

    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            System.err.println("[ERROR] User is not logged in. Redirecting to Top.");
            response.sendRedirect(request.getContextPath() + "/Top");
            return false;
        }

        System.out.println("[DEBUG] User is logged in: userId=" + session.getAttribute("userId"));
        return true;
    }
}
