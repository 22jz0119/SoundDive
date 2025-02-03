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
import dao.Livehouse_informationDAO;
import dao.UserDAO;
import model.Livehouse_information;

@WebServlet("/At_booking_confirmation")
public class At_booking_confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // パラメータ取得
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");
            String dayParam = request.getParameter("day");
            String timeParam = request.getParameter("time"); // 開始時間
            String finishTimeParam = request.getParameter("finish_time"); // 終了時間
            String livehouseIdParam = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");
            String userIdParam = request.getParameter("userId");

            // デバッグ: 取得したパラメータの出力
            debugRequestParams(yearParam, monthParam, dayParam, timeParam, finishTimeParam, livehouseIdParam, livehouseType, userIdParam);

            // 入力チェック（applicationIdは除外）
            if (isNullOrEmpty(yearParam, monthParam, dayParam, timeParam, finishTimeParam, livehouseIdParam, livehouseType, userIdParam)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが不足しています。");
                return;
            }

            // 型変換とエラーハンドリング
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int day = Integer.parseInt(dayParam);
            int livehouseId = Integer.parseInt(livehouseIdParam);
            int userId = Integer.parseInt(userIdParam);

            // 日時の変換（開始時間 & 終了時間）
            LocalDateTime reservationStartTime = parseDateTime(year, month, day, timeParam);
            LocalDateTime reservationFinishTime = parseDateTime(year, month, day, finishTimeParam);

            if (reservationStartTime == null || reservationFinishTime == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な日時形式です。");
                return;
            }

            // **開始時間が終了時間より後になっていないかチェック**
            if (reservationStartTime.isAfter(reservationFinishTime) || reservationStartTime.equals(reservationFinishTime)) {
                request.setAttribute("errorMessage", "終了時間は開始時間より後に設定してください。");
                request.getRequestDispatcher("/WEB-INF/jsp/artist/at-booking-confirmation.jsp").forward(request, response);
                return;
            }

            // ライブハウス情報の取得
            Livehouse_informationDAO livehouseDAO = new Livehouse_informationDAO(DBManager.getInstance());
            Livehouse_information livehouse = livehouseDAO.getLivehouse_informationByUserId(livehouseId);
            if (livehouse == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ライブハウス情報が見つかりませんでした。");
                return;
            }

            // ユーザー情報の取得
            UserDAO userDAO = new UserDAO(DBManager.getInstance());
            model.User user = userDAO.getUserById(userId);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ユーザー情報が見つかりませんでした。");
                return;
            }

            // マルチライブ判定の場合のみ applicationId を処理
            if ("multi".equalsIgnoreCase(livehouseType)) {
                String applicationIdParam = request.getParameter("applicationId");

                // applicationId の必須チェック
                if (isNullOrEmpty(applicationIdParam)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "マルチライブには申請IDが必要です。");
                    return;
                }

                int applicationId;
                try {
                    applicationId = Integer.parseInt(applicationIdParam);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "申請IDの形式が正しくありません。");
                    return;
                }

                // 申請IDからアーティスト情報を取得
                Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(DBManager.getInstance());
                String artistName = applicationDAO.getArtistNameByApplicationId(applicationId);
                if (artistName != null) {
                    request.setAttribute("artistName", artistName);
                } else {
                    request.setAttribute("errorMessage", "アーティスト情報が見つかりませんでした。");
                }

                request.setAttribute("applicationId", applicationId);
            }

            // **JSP に送るデータを設定**
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("selectedTime", timeParam);
            request.setAttribute("selectedFinishTime", finishTimeParam);
            request.setAttribute("reservationStartTime", reservationStartTime);
            request.setAttribute("reservationFinishTime", reservationFinishTime);
            request.setAttribute("livehouse", livehouse);
            request.setAttribute("userName", user.getName());
            request.setAttribute("telNumber", user.getTel_number());
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);
            request.setAttribute("userId", userId);

            // 確認画面へフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-booking-confirmation.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なパラメータ形式です。");
        } catch (Exception e) {
            System.err.println("[ERROR] doGet: Error occurred: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
        }
    }

    // デバッグ用: リクエストパラメータの出力
    private void debugRequestParams(String year, String month, String day, String time, String finishTime, String livehouseId, String livehouseType, String userId) {
        System.out.println("[DEBUG] year: " + year);
        System.out.println("[DEBUG] month: " + month);
        System.out.println("[DEBUG] day: " + day);
        System.out.println("[DEBUG] time: " + time);
        System.out.println("[DEBUG] finishTime: " + finishTime);
        System.out.println("[DEBUG] livehouseId: " + livehouseId);
        System.out.println("[DEBUG] livehouseType: " + livehouseType);
        System.out.println("[DEBUG] userId: " + userId);
    }

    // 日時変換メソッド
    private LocalDateTime parseDateTime(int year, int month, int day, String time) {
        try {
            String dateTimeStr = String.format("%04d-%02d-%02d %s", year, month, day, time);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            System.err.println("[ERROR] parseDateTime: 無効な日時形式です - " + e.getMessage());
            return null;
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
