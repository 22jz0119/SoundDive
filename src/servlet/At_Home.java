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
            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);
            Livehouse_informationDAO informationDAO = new Livehouse_informationDAO(dbManager);
            NotificationService notificationService = new NotificationService(dbManager);
            
            List<model.Notice> notifications = notificationService.getUserNotifications(userId);
            request.setAttribute("notifications", notifications);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            List<Livehouse_application> applicationsTrue = applicationDAO.getApplicationsByUserId(userId, true);
            List<Livehouse_application> applicationsFalse = applicationDAO.getApplicationsByUserId(userId, false);

            Set<Integer> livehouseIds = new HashSet<>();
            applicationsTrue.forEach(app -> livehouseIds.add(app.getLivehouse_information_id()));
            applicationsFalse.forEach(app -> livehouseIds.add(app.getLivehouse_information_id()));

            Map<Integer, Livehouse_information> livehouseInfoMap = informationDAO.findLivehouseInformationByIds(new ArrayList<>(livehouseIds));

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

            request.setAttribute("applicationsTrue", applicationsTrue);
            request.setAttribute("applicationsFalse", applicationsFalse);

            request.getRequestDispatcher("WEB-INF/jsp/artist/at_home.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "データベースエラーが発生しました。管理者にお問い合わせください。");
            request.getRequestDispatcher("WEB-INF/jsp/artist/at_home.jsp").forward(request, response);
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

            switch (action) {
                case "markAsRead":
                    handleMarkAsRead(request, response, notificationService);
                    break;
                case "logout":
                    logout(request.getSession());
                    response.sendRedirect(request.getContextPath() + "/Top");
                    break;
                case "solo":
                    response.sendRedirect(request.getContextPath() + "/At_livehouse_search?livehouse_type=solo");
                    break;
                case "multi":
                    response.sendRedirect(request.getContextPath() + "/At_Cogig?livehouse_type=multi");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/At_Home");
                    break;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
        }
    }

    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response, NotificationService notificationService) throws IOException {
        String noticeIdParam = request.getParameter("noticeId");

        if (noticeIdParam != null) {
            try {
                int noticeId = Integer.parseInt(noticeIdParam);
                notificationService.markAsRead(noticeId);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":\"success\"}");
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な通知IDです。");
            } catch (RuntimeException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "通知の更新に失敗しました。");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "通知IDが指定されていません。");
        }
    }

    private void logout(HttpSession session) {
        if (isLoggedIn(session)) {
            session.removeAttribute("userId");
            session.invalidate();
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
