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
import model.Livehouse_application;
import model.Livehouse_information;

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
            String livehouseId = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");

            String userId = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");

         // パラメータの検証
            if (isNullOrEmpty(year, month, day, time, livehouseId, livehouseType)) {
                System.err.println("[ERROR] doPost: Missing parameters.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }
            
            int applicationId = Integer.parseInt(applicationIdParam);

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
                if (isNullOrEmpty(userId, applicationIdParam)) {
                    System.err.println("[ERROR] doPost: Missing userId or applicationId.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDまたは申請IDが指定されていません。");
                    return;
                }

                boolean updateResult = applicationDAO.updateLivehouseApplication(
                    applicationId,
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
            // 予約完了メッセージ
            String reservationMessage = "予約が完了しました。";

            // 必要なデータをリクエストスコープに設定
            request.setAttribute("reservationMessage", reservationMessage);
            
            // 申請情報を取得
            Livehouse_application application = applicationDAO.getLivehouse_applicationById(applicationId);
            if (application == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された申請情報が見つかりません。");
                return;
            }

            // ライブハウス情報を取得
            Livehouse_information livehouse = applicationDAO.getLivehouseInformationById(application.getLivehouse_information_id());
            if (livehouse == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたライブハウス情報が見つかりません。");
                return;
            }

            // 固定メッセージとデータをリクエストスコープにセット
            request.setAttribute("reservationMessage", "予約が完了しました。");
            request.setAttribute("application", application);
            request.setAttribute("livehouse", livehouse);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な申請IDです。");
            // 予約完了ページにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-reservation-completed.jsp").forward(request, response);

        } catch (Exception e) {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GETリクエストの場合も同様の処理をする
        doPost(request, response);
    }
}
