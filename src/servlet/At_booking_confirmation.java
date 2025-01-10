package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_informationDAO;
import dao.UserDAO;
import model.Livehouse_information;

@WebServlet("/At_booking_confirmation")
public class At_booking_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[DEBUG] doGet: Preparing confirmation page.");

        try {
            // パラメータ取得
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String time = request.getParameter("time");
            String livehouseIdStr = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");
            String userId = request.getParameter("userId");
            String applicationId = null;
            
            System.out.println("[DEBUG] -----------------------------");
            System.out.println("[DEBUG] --- Request Parameters ---");
            System.out.println("[DEBUG] year: " + year);
            System.out.println("[DEBUG] month: " + month);
            System.out.println("[DEBUG] day: " + day);
            System.out.println("[DEBUG] time: " + time);
            System.out.println("[DEBUG] livehouseId: " + livehouseIdStr);  // ✅ 修正
            System.out.println("[DEBUG] livehouseType: " + livehouseType);
            System.out.println("[DEBUG] userId: " + userId);

            // マルチ予約なら applicationId を取得
            if ("multi".equalsIgnoreCase(livehouseType)) {
                applicationId = request.getParameter("applicationId");
            }

            // デバッグログ（全パラメータ）
            debugAllParams(year, month, day, time, livehouseIdStr, livehouseType, userId, applicationId);

            // 必須パラメータの検証
            if (isNullOrEmpty(year, month, day, time, livehouseIdStr, livehouseType, userId)) {
                System.err.println("[ERROR] doGet: Missing required parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            // 日時情報の変換（★ここでparseDateTimeを活用）
            LocalDateTime reservationDateTime = parseDateTime(year, month, day, time);
            if (reservationDateTime == null) {
                System.err.println("[ERROR] Invalid datetime format.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な日時形式です。");
                return;
            }
            System.out.println("[DEBUG] Parsed reservationDateTime: " + reservationDateTime);
            
            // livehouseId の数値変換
            int livehouseId;
            try {
                livehouseId = Integer.parseInt(livehouseIdStr);
            } catch (NumberFormatException e) {
                System.err.println("[ERROR] Invalid livehouseId format: " + livehouseIdStr);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスID形式です。");
                return;
            }

            // ライブハウス情報取得
            Livehouse_informationDAO livehouseDAO = new Livehouse_informationDAO(DBManager.getInstance());
            Livehouse_information livehouse = livehouseDAO.getLivehouse_informationById(livehouseId);
            if (livehouse == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ライブハウス情報が見つかりませんでした。");
                return;
            }
            request.setAttribute("livehouse", livehouse);

            // ユーザー情報取得
            UserDAO userDAO = new UserDAO(DBManager.getInstance());
            model.User user = userDAO.getUserById(Integer.parseInt(userId));
            if (user != null) {
                request.setAttribute("userName", user.getName());
                request.setAttribute("telNumber", user.getTel_number());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたユーザーが見つかりませんでした。");
                return;
            }

            // リクエストスコープにパラメータを設定
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("selectedTime", time);
            request.setAttribute("reservationDateTime", reservationDateTime);  // ★追加
            request.setAttribute("userId", userId);
            request.setAttribute("applicationId", applicationId);
            request.setAttribute("livehouseId", livehouseId);        // ★ livehouseId を追加
            request.setAttribute("livehouseType", livehouseType);    // ★ livehouseType を追加

            // 確認画面へフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-booking-confirmation.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[ERROR] doGet: Error occurred: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
        }
    }


    // 日時変換
    private LocalDateTime parseDateTime(String year, String month, String day, String time) {
        try {
            String dateTimeStr = year + "-" + String.format("%02d", Integer.parseInt(month)) + "-" +
                                 String.format("%02d", Integer.parseInt(day)) + " " + time;
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            System.err.println("[ERROR] parseDateTime: Invalid format: " + e.getMessage());
            return null;
        }
    }

    // パラメータのデバッグ出力
    private void debugAllParams(String... params) {
        String[] paramNames = {"year", "month", "day", "time", "livehouseId", "livehouseType", "userId", "applicationId"};
        for (int i = 0; i < params.length; i++) {
            System.out.println("[DEBUG] " + paramNames[i] + ": " + params[i]);
        }
    }

    // 空チェック
    private boolean isNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
