package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        String dayParam = request.getParameter("day");

        log("[DEBUG] Received parameters - year: " + yearParam + ", month: " + monthParam + ", day: " + dayParam);

        try {
            int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : LocalDate.now().getMonthValue();
            int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;

            log("[DEBUG] Parsed year: " + year + ", month: " + month + ", day: " + day);

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            // セッションからログインユーザー情報を取得
         // セッションからログインユーザー情報を取得
            HttpSession session = request.getSession(false);  // 既存のセッションのみ取得

            if (session == null) {
                log("[ERROR] セッションが存在しません。");
                response.sendRedirect(request.getContextPath() + "/Top");
                return;
            }

            // 修正: セッションキーを "userId" に変更
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                log("[ERROR] ログインユーザーIDが取得できませんでした。");
                response.sendRedirect(request.getContextPath() + "/Top");
                return;
            }

            log("[DEBUG] 取得したユーザーID: " + userId);

            // userIdからライブハウスIDを取得
            int livehouseId = dao.getLivehouseIdByUserId(userId);

            if (livehouseId == -1) {
                log("[ERROR] 該当するライブハウス情報が見つかりません。userId: " + userId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ライブハウス情報が見つかりません。");
                return;
            }

            log("[DEBUG] 取得したライブハウスID: " + livehouseId);


            log("[DEBUG] DAO method呼び出し直前 - year: " + year + ", month: " + month + ", userId: " + userId);
         // 予約データを取得
            Map<String, Integer> reservationCounts = dao.getReservationCountsByLivehouse(year, month, userId);
            
            if (reservationCounts == null) {
                log("[DEBUG] DAO returned null for reservationCounts.");
                reservationCounts = Map.of(); // 空のマップを使用
            }

            // JSON形式に変換
            Gson gson = new Gson();
            String reservationStatusJson = gson.toJson(reservationCounts);
            log("[DEBUG] DAO method実行後 - reservationCounts: " + reservationCounts);

            if (reservationCounts == null) {
                log("[DEBUG] DAO returned null for reservationCounts.");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(reservationStatusJson);  // reservationStatusJsonがnullの場合でも、JSONを返しているか
                log("[DEBUG] Sending JSON response: " + reservationStatusJson);
                response.getWriter().write("{\"error\": \"エラーが発生しました\"}");
                return;
            }


         // 重複定義を避けて再利用する
            reservationStatusJson = new Gson().toJson(reservationCounts);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(reservationStatusJson);
            request.setAttribute("reservationStatus", reservationStatusJson);
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("day", day);

            if (day == -1) {
                log("[DEBUG] JSPを表示します。reservationStatus: " + reservationStatusJson);
                request.setAttribute("reservationStatus", reservationStatusJson);
                request.setAttribute("year", year);
                request.setAttribute("month", month);
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
                return;
            }


        } catch (NumberFormatException e) {
            log("[ERROR] Invalid parameter format: year or month is not a valid number.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日付パラメータが不正です。正しい値を入力してください。");
        } catch (SQLException e) {
            log("[ERROR] SQLエラー: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"SQLエラーが発生しました。\"}");
        }
        catch (NullPointerException e) {
            log("[ERROR] NullPointerException: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データ取得中にエラーが発生しました。");
        } catch (IllegalArgumentException e) {
            log("[ERROR] Invalid parameter value: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log("[ERROR] Internal Server Error: " + e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"Internal Server Error\", \"message\": \"" + e.getMessage() + "\"}");
        }

    }
}
