package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        String cogigOrSoloParam = request.getParameter("cogig_or_solo");

        log("[DEBUG] Received parameters - year: " + yearParam + ", month: " + monthParam + ", day: " + dayParam + ", cogig_or_solo: " + cogigOrSoloParam);

        try {
            // パラメータの解析
            int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : LocalDate.now().getMonthValue();
            int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;
            int cogigOrSolo = (cogigOrSoloParam != null && !cogigOrSoloParam.isEmpty()) ? Integer.parseInt(cogigOrSoloParam) : 1;

            log("[DEBUG] Parsed year: " + year + ", month: " + month + ", day: " + day + ", cogig_or_solo: " + cogigOrSolo);

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            // セッションの確認
            HttpSession session = request.getSession(false);
            if (session == null) {
                log("[ERROR] セッションが存在しません。");
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(new Gson().toJson(Map.of("error", "セッションが存在しません。ログインしてください。")));
                return;
            }

            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                log("[ERROR] ログインユーザーIDが取得できませんでした。");
                response.sendRedirect(request.getContextPath() + "/Top");
                return;
            }

            log("[DEBUG] 取得したユーザーID: " + userId);

            // ライブハウスIDの取得
         // ライブハウスIDの取得
            int livehouseId = dao.getLivehouseIdByUserId(userId);
            if (livehouseId == -1) {
                log("[WARN] 該当するライブハウス情報が見つかりません。userId: " + userId);

                // もしJSONレスポンスを求める場合
                if ("json".equals(request.getParameter("format"))) {
                    response.setContentType("application/json; charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_OK); // 200 OKで返す
                    response.getWriter().write(new Gson().toJson(Map.of("message", "ライブハウス情報が見つかりません", "data", null)));
                    return;
                }

                // JSPにフォワードする場合（情報がない場合の画面表示を続行）
                request.setAttribute("errorMessage", "ライブハウス情報が見つかりません");
                request.setAttribute("reservationStatus", "{}"); // 空のデータを設定
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
                return;
            }

            log("[DEBUG] 取得したライブハウスID: " + livehouseId);
            
            // リクエストがJSONを求めている場合
            if ("json".equals(request.getParameter("format"))) {
                try {
                    Map<String, Integer> reservationCounts = dao.getReservationCountsByLivehouse(year, month, userId);
                    log("[DEBUG] DAO method実行後 - reservationCounts: " + reservationCounts);

                    // JSONに変換してレスポンスを返却
                    String reservationStatusJson = new Gson().toJson(reservationCounts);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(reservationStatusJson);
                } catch (Exception e) {
                    log("[ERROR] JSONレスポンス処理中のエラー: " + e.getMessage(), e);
                    response.setContentType("application/json; charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write(new Gson().toJson(Map.of("error", "システムエラーが発生しました。", "details", e.getMessage())));
                }
                return; // JSONレスポンスを返したら処理終了
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

            List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
            List<LivehouseApplicationWithGroup> cogigOrSolo1Reservations = new ArrayList<>();
            List<LivehouseApplicationWithGroup> cogigOrSolo2Reservations = new ArrayList<>();

            if (cogigOrSolo == 1) {
                log("[DEBUG] cogig_or_solo = 1 での処理");
                cogigOrSolo1Reservations = dao.getReservationsWithTrueFalseZero(year, month, day);
                log("[DEBUG] Retrieved cogig_or_solo = 1 reservations: " + cogigOrSolo1Reservations);
            } else if (cogigOrSolo == 2) {
                log("[DEBUG] cogig_or_solo = 2 での処理");
                cogigOrSolo2Reservations = dao.getReservationsByCogigOrSolo(year, month, day);
                log("[DEBUG] Retrieved cogig_or_solo = 2 reservations: " + cogigOrSolo2Reservations);
            }

            request.setAttribute("reservations", reservations);
            request.setAttribute("cogigOrSolo1Reservations", cogigOrSolo1Reservations);
            request.setAttribute("cogigOrSolo2Reservations", cogigOrSolo2Reservations);

            if (day != -1) {
                String redirectUrl = String.format("/Application_list?year=%d&month=%d&day=%d&cogig_or_solo=%d", year, month, day, cogigOrSolo);
                log("[DEBUG] Redirecting to: " + redirectUrl);
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return;
            }

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
}
