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

@WebServlet("/At_livehouse_reservation_completed")
public class At_livehouse_reservation_completed extends HttpServlet {
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
            String livehouseType = request.getParameter("livehouse_type");
            String userId = request.getParameter("userId");

            // 必須パラメータの検証
            if (isNullOrEmpty(year, month, day, time, livehouseIdParam, livehouseType, userId)) {
                System.err.println("[ERROR] doPost: Missing parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            // 数値形式のパラメータを変換
            int livehouseId;
            try {
                livehouseId = Integer.parseInt(livehouseIdParam);
            } catch (NumberFormatException e) {
                System.err.println("[ERROR] doPost: Invalid number format for livehouseId.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な数値形式のライブハウスIDが含まれています。");
                return;
            }

            LocalDateTime startTime = parseDateTime(year, month, day, time);
            if (startTime == null) {
                System.err.println("[ERROR] doPost: Invalid datetime format.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な日時形式が指定されています。");
                return;
            }

            // データベース操作
            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);

            if ("solo".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] doPost: Processing solo reservation.");

                // userId を数値に変換
                int userIdInt;
                try {
                    userIdInt = Integer.parseInt(userId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] doPost: Invalid number format for userId.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なユーザーIDが指定されています。");
                    return;
                }

                // saveSoloReservation 呼び出し
                boolean saveResult = applicationDAO.saveSoloReservation(livehouseId, userIdInt, startTime, startTime);
                if (!saveResult) {
                    System.err.println("[ERROR] doPost: Failed to save solo reservation.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ソロライブ予約の保存に失敗しました。");
                    return;
                }
            } else if ("multi".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] doPost: Processing multi reservation.");

                // applicationId の取得と検証
                String applicationIdParam = request.getParameter("applicationId");
                if (isNullOrEmpty(applicationIdParam)) {
                    System.err.println("[ERROR] doPost: Missing applicationId.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "申請IDが指定されていません。");
                    return;
                }

                int applicationId;
                try {
                    applicationId = Integer.parseInt(applicationIdParam);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] doPost: Invalid number format for applicationId.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な数値形式の申請IDが含まれています。");
                    return;
                }

                boolean updateResult = applicationDAO.updateLivehouseApplication(applicationId, livehouseId, startTime, startTime);
                if (!updateResult) {
                    System.err.println("[ERROR] doPost: Failed to save multi reservation.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "マルチライブ予約の保存に失敗しました。");
                    return;
                }
            } else {
                System.err.println("[ERROR] doPost: Invalid livehouseType.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスタイプが指定されました。");
                return;
            }

            // 完了メッセージ
            String reservationMessage = "予約が完了しました。";

            // 必要なデータをリクエストスコープに設定
            request.setAttribute("reservationMessage", reservationMessage);

            // 完了ページにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-reservation-completed.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[ERROR] doPost: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました: " + e.getMessage());
        }
    }

    private LocalDateTime parseDateTime(String year, String month, String day, String time) {
        try {
            String dateTimeStr = year + "-" + String.format("%02d", Integer.parseInt(month)) + "-" +
                                 String.format("%02d", Integer.parseInt(day)) + " " + time;
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            System.err.println("[ERROR] parseDateTime: Invalid datetime format: " + e.getMessage());
            return null;
        }
    }

    private boolean isNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}