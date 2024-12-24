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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[DEBUG] doGet: Preparing confirmation page.");

        try {
            // パラメータ取得
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String time = request.getParameter("time");
            String livehouseId = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");

            // データ検証
            if (isNullOrEmpty(year, month, day, time, livehouseId, livehouseType)) {
                System.err.println("[ERROR] doGet: Missing parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            // JSPにデータを渡す
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("selectedTime", time);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);

            // 確認画面にフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-booking-confirmation.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[ERROR] doGet: Error while preparing confirmation page: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーでエラーが発生しました: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[DEBUG] doPost: Entering doPost for saving reservation.");

        try {
            // リクエストのすべてのパラメータをログ出力
            System.out.println("[DEBUG] Received request parameters:");
            request.getParameterMap().forEach((key, value) -> {
                System.out.println("  " + key + ": " + String.join(", ", value));
            });

            // 保存処理のためのパラメータ取得
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String time = request.getParameter("time");
            String livehouseId = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");

            String userId = request.getParameter("userId");
            String applicationId = request.getParameter("applicationId");

            // パラメータの検証
            if (isNullOrEmpty(year, month, day, time, livehouseId, livehouseType)) {
                System.err.println("[ERROR] doPost: Missing parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            // パラメータのログ出力
            System.out.println("[DEBUG] doPost: Processed parameters:");
            System.out.println("  year: " + year + ", month: " + month + ", day: " + day);
            System.out.println("  time: " + time + ", livehouseId: " + livehouseId);
            System.out.println("  livehouseType: " + livehouseType);
            System.out.println("  userId: " + userId + ", applicationId: " + applicationId);

            int livehouseInformationId = Integer.parseInt(livehouseId);
            LocalDateTime startTime = parseDateTime(year, month, day, time);

            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);

            if ("solo".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] doPost: Processing solo reservation.");
                boolean saveResult = applicationDAO.saveSoloReservation(livehouseInformationId, startTime, startTime);

                if (saveResult) {
                    System.out.println("[DEBUG] doPost: Solo reservation saved successfully.");
                    request.setAttribute("confirmationMessage", "ソロライブの予約が完了しました！");
                } else {
                    System.err.println("[ERROR] doPost: Failed to save solo reservation.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ソロライブ予約の保存に失敗しました。");
                    return;
                }
            } else if ("multi".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] doPost: Processing multi reservation.");
                if (isNullOrEmpty(userId, applicationId)) {
                    System.err.println("[ERROR] doPost: Missing userId or applicationId.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDまたは申請IDが指定されていません。");
                    return;
                }

                boolean updateResult = applicationDAO.updateLivehouseApplication(
                    Integer.parseInt(applicationId),
                    livehouseInformationId,
                    startTime,
                    startTime
                );

                if (updateResult) {
                    System.out.println("[DEBUG] doPost: Multi reservation saved successfully.");
                    request.setAttribute("confirmationMessage", "マルチライブの予約が完了しました！");
                } else {
                    System.err.println("[ERROR] doPost: Failed to save multi reservation.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "マルチライブ予約の保存に失敗しました。");
                    return;
                }
            } else {
                System.err.println("[ERROR] doPost: Invalid livehouseType.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスタイプが指定されました。");
                return;
            }

            // 成功した場合のフォワード
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("selectedTime", time);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);

            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-booking-confirmation.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[ERROR] doPost: Error while saving reservation: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーでエラーが発生しました: " + e.getMessage());
        }
        System.out.println("[DEBUG] doPost: Exit point.");
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
