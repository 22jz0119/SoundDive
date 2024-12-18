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
import dao.Livehouse_applicationDAO;

@WebServlet("/At_booking_confirmation")
public class At_booking_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // パラメータ取得
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String time = request.getParameter("time");
            String livehouseIdParam = request.getParameter("livehouseId");
            String userIdParam = request.getParameter("userId"); // ソロでも必要
            String applicationIdParam = request.getParameter("applicationId");
            String livehouseType = request.getParameter("livehouse_type"); // ソロ/マルチの判定用

            System.out.println("[DEBUG] Received parameters:");
            System.out.println("year: " + year + ", month: " + month + ", day: " + day);
            System.out.println("time: " + time + ", livehouseId: " + livehouseIdParam);
            System.out.println("userId: " + userIdParam + ", applicationId: " + applicationIdParam);
            System.out.println("livehouseType: " + livehouseType);

            // 必須パラメータのバリデーション
            if (isNullOrEmpty(year, month, day, time, livehouseIdParam, livehouseType)) {
                System.err.println("[ERROR] Missing required parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            // パラメータの変換
            int livehouseInformationId;
            try {
                livehouseInformationId = Integer.parseInt(livehouseIdParam);
            } catch (NumberFormatException e) {
                System.err.println("[ERROR] Invalid ID format: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なID形式です。");
                return;
            }

            // LocalDateTime の生成
            LocalDateTime startTime = parseDateTime(year, month, day, time);
            if (startTime == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な日付または時間形式です。");
                return;
            }

            System.out.println("[DEBUG] Parsed startTime: " + startTime);

            // DBManager初期化
            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);

            // ソロ/マルチの処理分岐
            if ("solo".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] ソロライブモード");
                
                // ソロの処理: userId が必要
                if (isNullOrEmpty(userIdParam)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDが指定されていません。");
                    return;
                }

                int userId;
                try {
                    userId = Integer.parseInt(userIdParam);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid userId format: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なユーザーID形式です。");
                    return;
                }

                // ソロライブ予約データを保存
                System.out.println("[DEBUG] Saving solo reservation for userId: " + userId);
                boolean saveResult = applicationDAO.saveSoloReservation(
                        livehouseInformationId, // livehouseId
                        userId,                 // userId
                        dateTime,               // dateTime
                        startTime,              // startTime
                        dbManager               // DBManager
                );

                if (saveResult) {
                    System.out.println("[INFO] Solo reservation saved successfully.");
                } else {
                    System.err.println("[ERROR] Failed to save solo reservation.");
                }

            } else if ("multi".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] マルチライブモード");

                // マルチの場合 applicationId が必要
                if (isNullOrEmpty(applicationIdParam)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "申請IDが指定されていません。");
                    return;
                }

                int applicationId;
                try {
                    applicationId = Integer.parseInt(applicationIdParam);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid applicationId format: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な申請ID形式です。");
                    return;
                }

                // データベース更新処理
                System.out.println("[DEBUG] Updating database for applicationId: " + applicationId);
                boolean updateResult = applicationDAO.updateLivehouseApplication(
                        applicationId,
                        livehouseInformationId,
                        startTime,
                        startTime
                );

                if (updateResult) {
                    System.out.println("[DEBUG] Multi reservation updated successfully.");
                    forwardToCompletedPage(request, response, year, month, day, livehouseInformationId, livehouseType);
                } else {
                    System.err.println("[ERROR] Failed to update the database for applicationId: " + applicationId);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースの更新に失敗しました。");
                }
            } else {
                System.err.println("[ERROR] Invalid livehouseType: " + livehouseType);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスタイプが指定されました。");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error while processing booking confirmation: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーでエラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * 日付と時間を LocalDateTime に変換
     */
    private LocalDateTime parseDateTime(String year, String month, String day, String time) {
        try {
            String normalizedMonth = String.format("%02d", Integer.parseInt(month));
            String normalizedDay = String.format("%02d", Integer.parseInt(day));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String dateTimeStr = year + "-" + normalizedMonth + "-" + normalizedDay + " " + time;

            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            System.err.println("[ERROR] Invalid datetime format: " + e.getMessage());
            return null;
        }
    }

    /**
     * 任意の文字列が null または空白かを判定
     */
    private boolean isNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 完了ページへフォワード
     */
    private void forwardToCompletedPage(HttpServletRequest request, HttpServletResponse response, String year, String month, String day, int livehouseId, String livehouseType) throws ServletException, IOException {
        request.setAttribute("selectedYear", year);
        request.setAttribute("selectedMonth", month);
        request.setAttribute("selectedDay", day);
        request.setAttribute("livehouseId", livehouseId);
        request.setAttribute("livehouseType", livehouseType);

        request.getRequestDispatcher("/At_livehouse_reservation_completed").forward(request, response);
    }
}

