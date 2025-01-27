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
        DBManager dbManager = DBManager.getInstance();
        dao = new Livehouse_applicationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");
        String cogigOrSoloParam = request.getParameter("cogig_or_solo");

        log("[DEBUG] Received parameters - year: " + yearParam + ", month: " + monthParam + ", day: " + dayParam + ", cogig_or_solo: " + cogigOrSoloParam);

        try {
            // ログイン状態を確認
            if (!isLoggedIn(request, response)) {
                return; // 未ログインの場合は処理終了
            }

            // パラメータの解析
            int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : LocalDate.now().getMonthValue();
            int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;
            int cogigOrSolo = (cogigOrSoloParam != null && !cogigOrSoloParam.isEmpty()) ? Integer.parseInt(cogigOrSoloParam) : 1;

            log("[DEBUG] Parsed year: " + year + ", month: " + month + ", day: " + day + ", cogig_or_solo: " + cogigOrSolo);

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            // セッションからユーザーIDを取得
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                log("[ERROR] ログインユーザーIDが取得できませんでした。");
                response.sendRedirect(request.getContextPath() + "/Top");
                return;
            }

            log("[DEBUG] 取得したユーザーID: " + userId);

            // ライブハウスIDの取得
            int livehouseId = dao.getLivehouseIdByUserId(userId);
            if (livehouseId == -1) {
                log("[WARN] 該当するライブハウス情報が見つかりません。userId: " + userId);

                if ("json".equals(request.getParameter("format"))) {
                    response.setContentType("application/json; charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(new Gson().toJson(Map.of("message", "ライブハウス情報が見つかりません", "data", null)));
                    return;
                }

                request.setAttribute("errorMessage", "ライブハウス情報が見つかりません");
                request.setAttribute("reservationStatus", "{}");
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
                return;
            }

            log("[DEBUG] 取得したライブハウスID: " + livehouseId);

            if ("json".equals(request.getParameter("format"))) {
                try {
                    Map<String, Integer> reservationCounts = dao.getReservationCountsByLivehouse(year, month, userId);
                    log("[DEBUG] DAO method実行後 - reservationCounts: " + reservationCounts);

                    String reservationStatusJson = new Gson().toJson(reservationCounts);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(reservationStatusJson);
                } catch (Exception e) {
                    log("[ERROR] JSONレスポンス処理中のエラー: " + e.getMessage(), e);
                    response.setContentType("application/json; charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write(new Gson().toJson(Map.of("error", "システムエラーが発生しました。", "details", e.getMessage())));
                }
                return;
            }

            // JSPへのフォワード
            Map<String, Integer> reservationCounts = dao.getReservationCountsByLivehouse(year, month, userId);
            String reservationStatusJson = new Gson().toJson(reservationCounts);
            log("[DEBUG] JSON変換後 - reservationStatusJson: " + reservationStatusJson);

            request.setAttribute("reservationStatus", reservationStatusJson);
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("day", day);
            request.setAttribute("cogig_or_solo", cogigOrSolo);

            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
            log("[DEBUG] Successfully forwarded to JSP.");

        } catch (NumberFormatException e) {
            log("[ERROR] Invalid parameter format: year or month is not a valid number.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日付パラメータが不正です。正しい値を入力してください。");
        } catch (SQLException e) {
            log("[ERROR] SQLエラー: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQLエラーが発生しました。");
        } catch (Exception e) {
            log("[ERROR] その他のエラー: " + e.getMessage(), e);
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(Map.of("error", "システムエラーが発生しました。", "details", e.getMessage())));
        }
    }

    // ログイン状態を確認
    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); // 既存のセッションを取得
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/Top");
            return false;
        }
        return true;
    }

    // ログアウト処理
    private void logout(HttpSession session) {
        if (session != null && session.getAttribute("userId") != null) { // ログイン済みか確認
            Integer userId = (Integer) session.getAttribute("userId");
            log("[DEBUG] Logging out user with ID: " + userId + ". Session ID: " + session.getId());

            session.removeAttribute("userId"); // userId 属性を削除
            session.invalidate(); // セッションを無効化

            log("[DEBUG] User with ID: " + userId + " logged out successfully. Session invalidated.");
        } else {
            log("[DEBUG] No user is currently logged in.");
        }
    }
}
