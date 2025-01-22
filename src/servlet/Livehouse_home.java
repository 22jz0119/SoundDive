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

        log("[DEBUG] Received parameters - year: " + yearParam + ", month: " + monthParam + ", day: " + dayParam);
        log("[DEBUG] Received parameters - year: " + yearParam + ", month: " + monthParam + ", day: " + dayParam + ", cogig_or_solo: " + cogigOrSoloParam);

        try {
            int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : LocalDate.now().getMonthValue();
            int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;

            log("[DEBUG] Parsed year: " + year + ", month: " + month + ", day: " + day);

            // cogig_or_soloをデータに基づいて設定
            int cogigOrSolo = (cogigOrSoloParam != null && !cogigOrSoloParam.isEmpty()) ? Integer.parseInt(cogigOrSoloParam) : 1;
            log("[DEBUG] Parsed year: " + year + ", month: " + month + ", day: " + day + ", cogig_or_solo: " + cogigOrSolo);

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            // セッションからログインユーザー情報を取得
            HttpSession session = request.getSession(false);  // 既存のセッションのみ取得

            if (session == null) {
                log("[ERROR] セッションが存在しません。");
                response.sendRedirect(request.getContextPath() + "/Top");
                return;
            }

            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                log("[ERROR] ログインユーザーIDが取得できませんでした。");
                response.sendRedirect(request.getContextPath() + "/Top");
                return;
            }

            log("[DEBUG] 取得したユーザーID: " + userId);

            // ライブハウスIDを取得
            int livehouseId = dao.getLivehouseIdByUserId(userId);

            if (livehouseId == -1) {
                log("[ERROR] 該当するライブハウス情報が見つかりません。userId: " + userId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ライブハウス情報が見つかりません。");
                return;
            }

            log("[DEBUG] 取得したライブハウスID: " + livehouseId);

            // 予約データの取得
            Map<String, Integer> reservationCounts = dao.getReservationCountsByLivehouse(year, month, userId);
            log("[DEBUG] DAO method実行後 - reservationCounts: " + reservationCounts);

            String reservationStatusJson = new Gson().toJson(reservationCounts);
            request.setAttribute("reservationStatus", reservationStatusJson);
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("day", day);
            request.setAttribute("cogig_or_solo", cogigOrSolo);

            // cogig_or_soloによる処理分岐
            List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();

            if (cogigOrSolo == 1) {
                log("[DEBUG] cogig_or_solo = 1 での処理");

                // 1の場合: getReservationsWithTrueFalseZeroメソッドを使用してデータを取得
                reservations = dao.getReservationsWithTrueFalseZero(year, month, day);
                log("[DEBUG] getReservationsWithTrueFalseZero method result: " + reservations);

                // 追加で必要な処理があれば記述

            } else if (cogigOrSolo == 2) {
                log("[DEBUG] cogig_or_solo = 2 での処理");

                // 2の場合: getReservationsWithTrueFalseZero と LivehouseApplicationWithGroup のデータ両方を表示
                reservations = dao.getReservationsWithTrueFalseZero(year, month, day);
                log("[DEBUG] getReservationsWithTrueFalseZero method result: " + reservations);

                // LivehouseApplicationWithGroupのデータを取得
                List<LivehouseApplicationWithGroup> groupReservations = dao.getReservationsByCogigOrSolo(year, month, day);
                log("[DEBUG] getReservationsByCogigOrSolo method result: " + groupReservations);

                // 両方のデータをリクエスト属性に設定
                request.setAttribute("groupReservations", groupReservations);
            }

            // 結果の表示
            request.setAttribute("reservations", reservations);

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
        } catch (NullPointerException e) {
            log("[ERROR] NullPointerException: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データ取得中にエラーが発生しました。");
        } catch (IllegalArgumentException e) {
            log("[ERROR] Invalid parameter value: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log("[ERROR] その他のエラー: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "システムエラーが発生しました。");
        }
    }
}
