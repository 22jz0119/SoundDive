package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            String userId = request.getParameter("userId");
            String applicationId = null;

            // マルチの場合のみ applicationId を取得
            if ("multi".equalsIgnoreCase(livehouseType)) {
                applicationId = request.getParameter("applicationId");
            }

            // 各値のログ出力
            System.out.println("[DEBUG] Parsed parameters:");
            System.out.println("  Year: " + year);
            System.out.println("  Month: " + month);
            System.out.println("  Day: " + day);
            System.out.println("  Time: " + time);
            System.out.println("  LivehouseId: " + livehouseId);
            System.out.println("  LivehouseType: " + livehouseType);
            System.out.println("  UserId: " + userId);
            System.out.println("  ApplicationId: " + applicationId);

            // 必須パラメータの検証
            if (isNullOrEmpty(year, month, day, time, livehouseId, livehouseType)) {
                System.err.println("[ERROR] doGet: Missing parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            if ("multi".equalsIgnoreCase(livehouseType)) {
                // マルチの場合は applicationId と userId を検証
                if (isNullOrEmpty(userId, applicationId)) {
                    System.err.println("[ERROR] doGet: Missing userId or applicationId for multi.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "マルチ予約のためのユーザーIDまたは申請IDが不足しています。");
                    return;
                }
            }

            // 年月日と時間をリクエストスコープに設定
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("selectedTime", time);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);

            // マルチの場合のみ追加データを設定
            if ("multi".equalsIgnoreCase(livehouseType)) {
                request.setAttribute("userId", userId);
                request.setAttribute("applicationId", applicationId);
            }

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
                System.out.println("  Parameter: " + key + " => Value(s): " + String.join(", ", value));
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

            // 各値のログ出力
            System.out.println("[DEBUG] Parsed parameters:");
            System.out.println("  Year: " + year);
            System.out.println("  Month: " + month);
            System.out.println("  Day: " + day);
            System.out.println("  Time: " + time);
            System.out.println("  LivehouseId: " + livehouseId);
            System.out.println("  LivehouseType: " + livehouseType);
            System.out.println("  UserId: " + userId);
            System.out.println("  ApplicationId: " + applicationId);

            // 必須パラメータの検証
            if (isNullOrEmpty(year, month, day, time, livehouseId, livehouseType)) {
                System.err.println("[ERROR] doPost: Missing parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            // 数値変換のログを追加
            int livehouseInformationId = Integer.parseInt(livehouseId);
            System.out.println("[DEBUG] Converted livehouseInformationId: " + livehouseInformationId);

            LocalDateTime startTime = parseDateTime(year, month, day, time);
            if (startTime == null) {
                System.err.println("[ERROR] doPost: Invalid datetime format.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な日時形式です。");
                return;
            }
            System.out.println("[DEBUG] Parsed startTime: " + startTime);

            // マルチライブの場合の詳細ログ
            if ("multi".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] Multi-live type detected.");
                if (isNullOrEmpty(userId, applicationId)) {
                    System.err.println("[ERROR] doPost: Missing userId or applicationId.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDまたは申請IDが指定されていません。");
                    return;
                }
                System.out.println("[DEBUG] UserId: " + userId);
                System.out.println("[DEBUG] ApplicationId: " + applicationId);
            }

            // 後続処理...

        } catch (NumberFormatException e) {
            System.err.println("[ERROR] doPost: Invalid number format for parameters: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な数値形式のパラメータが含まれています。");
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
