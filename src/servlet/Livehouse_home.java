package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.DBManager;
import dao.Livehouse_applicationDAO;

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
        DBManager dbManager = DBManager.getInstance();
        dao = new Livehouse_applicationDAO(dbManager);
        log("[DEBUG] DAO initialized successfully.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("[DEBUG] doGet method called.");

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");  // 追加: dayパラメータを取得

        log("[DEBUG] Received parameters - year: " + yearParam + ", month: " + monthParam + ", day: " + dayParam);

        try {
            // 年と月のデフォルト値を現在の日付から設定
            int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : LocalDate.now().getMonthValue();

            // 日付が指定されていれば、それを取得
            int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;

            log("[DEBUG] Parsed year: " + year + ", month: " + month + ", day: " + day);

            // 月の値が有効範囲かチェック
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            // DAOメソッドの呼び出し (正しいメソッドを使用)
            log("[DEBUG] Calling DAO method: getReservationCountsByDay with year: " + year + ", month: " + month);
            Map<String, Integer> reservationCounts = dao.getReservationCountsByDay(year, month);

            if (reservationCounts == null) {
                log("[DEBUG] DAO returned null for reservationCounts.");
            } else if (reservationCounts.isEmpty()) {
                log("[DEBUG] DAO returned an empty map for reservationCounts.");
            } else {
                log("[DEBUG] DAO returned reservationCounts: " + reservationCounts);
            }

            // JSON形式に変換してJSPに渡す
            String reservationStatusJson = new Gson().toJson(reservationCounts);
            request.setAttribute("reservationStatus", reservationStatusJson);

            // JSPに渡すデータをリクエストスコープに設定
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("day", day); // 日付も渡す

         // 日付が指定されていれば、Application_listにリダイレクト
            if (day != -1) {
                // Application_listにリダイレクトするURLを構築
                String redirectUrl = String.format("/Application_list?year=%d&month=%d&day=%d", year, month, day);
                log("[DEBUG] Redirecting to: " + redirectUrl);
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return; // ここでリダイレクトするので、後続の処理は実行しない
            }


            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
            log("[DEBUG] Successfully forwarded to JSP.");

        } catch (NumberFormatException e) {
            log("[ERROR] Invalid parameter format: year or month is not a valid number.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日付パラメータが不正です。正しい値を入力してください。");
        } catch (IllegalArgumentException e) {
            log("[ERROR] Invalid parameter value: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log("[ERROR] Unexpected error occurred while calling DAO method.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "システムエラーが発生しました。時間をおいて再度お試しください。");
        }
    }
}
