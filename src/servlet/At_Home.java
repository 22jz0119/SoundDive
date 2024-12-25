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
        // ユーザーがログインしているか確認
        if (!isLoggedIn(request, response)) {
            return;
        }

        // セッションからユーザーIDを取得
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/Top");
            return;
        }

        try {
            // DBManagerのインスタンスを取得し、Livehouse_applicationDAOを使ってデータを取得
            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO dao = new Livehouse_applicationDAO(dbManager);

            // true_falseがtrueの場合の申請情報を取得
            List<Livehouse_application> applicationsTrue = dao.getApplicationsByUserIdTrue(userId);

            // true_falseがfalseの場合の申請情報を取得
            List<Livehouse_application> applicationsFalse = dao.getApplicationsByUserIdFalse(userId);

            // Livehouse_informationDAOを使って、各申請に関連するライブハウス情報を取得
            Livehouse_informationDAO livehouseInfoDAO = new Livehouse_informationDAO(dbManager);

            // applicationsTrue の情報を取得して関連するライブハウス情報をセット
            for (Livehouse_application app : applicationsTrue) {
                Livehouse_information livehouseInfo = livehouseInfoDAO.findLivehouseInformationById(app.getLivehouse_information_id());
                if (livehouseInfo != null) {
                    app.setLivehouse_information(livehouseInfo);
                }
            }

            // applicationsFalse の情報を取得して関連するライブハウス情報をセット
            for (Livehouse_application app : applicationsFalse) {
                Livehouse_information livehouseInfo = livehouseInfoDAO.findLivehouseInformationById(app.getLivehouse_information_id());
                if (livehouseInfo != null) {
                    app.setLivehouse_information(livehouseInfo);
                } 
            }

            // 取得した情報をリクエストにセット
            request.setAttribute("applicationsTrue", applicationsTrue);  // true_false = true の申請情報
            request.setAttribute("applicationsFalse", applicationsFalse); // true_false = false の申請情報

            // at_home.jspに転送
            System.out.println("[DEBUG] User is logged in. Forwarding to at_home.jsp.");
            request.getRequestDispatcher("WEB-INF/jsp/at_home.jsp").forward(request, response);

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
        if ("solo".equals(action)) {
            System.out.println("[DEBUG] Solo live action triggered.");
            response.sendRedirect(request.getContextPath() + "/At_livehouse_search?livehouse_type=solo");
        } else if ("multi".equals(action)) {
            System.out.println("[DEBUG] Multi live action triggered.");
            response.sendRedirect(request.getContextPath() + "/At_Cogig?livehouse_type=multi");
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
        return true;
    }
}
