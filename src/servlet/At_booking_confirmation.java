package servlet;

import java.io.IOException;
import java.time.LocalDate;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // リクエストパラメータの取得
        	String year = request.getParameter("year");
        	String month = request.getParameter("month");
        	String day = request.getParameter("day");
        	String time = request.getParameter("time");
        	String livehouseIdParam = request.getParameter("livehouseId");
        	String userIdParam = request.getParameter("userId");
        	String applicationIdParam = request.getParameter("applicationId");

        	System.out.println("[DEBUG] Received year: " + year);
        	System.out.println("[DEBUG] Received month: " + month);
        	System.out.println("[DEBUG] Received day: " + day);
        	System.out.println("[DEBUG] Received time: " + time);
        	System.out.println("[DEBUG] Received livehouseId: " + livehouseIdParam);
        	System.out.println("[DEBUG] Received userId: " + userIdParam);
        	System.out.println("[DEBUG] Received applicationId: " + applicationIdParam);


            // 入力チェック
            if (userIdParam == null || userIdParam.trim().isEmpty() || 
                livehouseIdParam == null || livehouseIdParam.trim().isEmpty() || 
                applicationIdParam == null || applicationIdParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            int userId;
            int livehouseId;
            int applicationId;
            try {
                userId = Integer.parseInt(userIdParam);
                livehouseId = Integer.parseInt(livehouseIdParam);
                applicationId = Integer.parseInt(applicationIdParam); // 既存レコードのIDを整数に変換
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なID形式です。");
                return;
            }

            // 日付と時間の変換
            LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            LocalDateTime startTime = null;
            try {
                startTime = LocalDateTime.parse(date + "T" + time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な時間形式です。");
                return;
            }

            // DBManager初期化
            DBManager dbManager = DBManager.getInstance();

            // Livehouse_application テーブルを更新
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);
            boolean updateResult = applicationDAO.updateLivehouseApplication(applicationId, true, startTime);

            if (updateResult) {
                // 更新後の情報をリクエストスコープにセット
                request.setAttribute("selectedYear", year);
                request.setAttribute("selectedMonth", month);
                request.setAttribute("selectedDay", day);
                request.setAttribute("applicationId", applicationId); // 更新したapplicationIdを渡す

                // 予約完了ページへリダイレクト
                request.getRequestDispatcher("/WEB-INF/jsp/artist/at_reservation_completed.jsp").forward(request, response);
            } else {
                // エラーページを表示
                System.err.println("[ERROR] Failed to update booking application in the database.");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースの更新に失敗しました。");
            }
        } catch (Exception e) {
            // エラー処理
            System.err.println("[ERROR] Error while processing booking confirmation");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーでエラーが発生しました: " + e.getMessage());
        }
    }
}