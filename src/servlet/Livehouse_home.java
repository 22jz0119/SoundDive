package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;

/**
 * Servlet implementation class Livehouse_home
 */
@WebServlet("/Livehouse_home")
public class Livehouse_home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Livehouse_applicationDAO dao;

    @Override
    public void init() throws ServletException {
        log("[DEBUG] Initializing Livehouse_home servlet.");
        dao = new Livehouse_applicationDAO(DBManager.getInstance());
        log("[DEBUG] DAO initialized successfully.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("[DEBUG] doGet method called.");

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");
        String cogigOrSoloParam = request.getParameter("cogig_or_solo");

        try {
            int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : LocalDate.now().getMonthValue();
            int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;
            int cogigOrSolo = (cogigOrSoloParam != null && !cogigOrSoloParam.isEmpty()) ? Integer.parseInt(cogigOrSoloParam) : 1;

            log("[DEBUG] Received parameters - year: " + year + ", month: " + month + ", day: " + day + ", cogig_or_solo: " + cogigOrSolo);

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect(request.getContextPath() + "/Top");
                return;
            }
            int userId = (int) session.getAttribute("userId");
            int livehouseId = dao.getLivehouseIdByUserId(userId);
            if (livehouseId == -1) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ライブハウス情報が見つかりません。");
                return;
            }

            // データ取得
            List<LivehouseApplicationWithGroup> cogigOrSolo1Reservations = dao.getReservationsWithTrueFalseZero(year, month, day);
            List<LivehouseApplicationWithGroup> cogigOrSolo2Reservations = dao.getReservationsByCogigOrSolo(year, month, day);

            log("[DEBUG] Retrieved reservations - cogig_or_solo=1: " + cogigOrSolo1Reservations.size() +
                ", cogig_or_solo=2: " + cogigOrSolo2Reservations.size());

            request.setAttribute("cogigOrSolo1Reservations", cogigOrSolo1Reservations);
            request.setAttribute("cogigOrSolo2Reservations", cogigOrSolo2Reservations);

            if (day > 0) {
                response.sendRedirect(String.format("%s/Application_list?year=%d&month=%d&day=%d&cogig_or_solo=%d",
                    request.getContextPath(), year, month, day, cogigOrSolo));
                return;
            }

            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
        } catch (Exception e) {
            log("[ERROR] An error occurred: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "システムエラーが発生しました。");
        }
    }

}


