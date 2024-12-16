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

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.Livehouse_informationDAO;
import model.Artist_group;
import model.Livehouse_application;
import model.Livehouse_information;

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

            // 入力チェック
            if (userIdParam == null || userIdParam.isEmpty() || livehouseIdParam == null || livehouseIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが指定されていません。");
                return;
            }

            int userId = Integer.parseInt(userIdParam);
            int livehouseId = Integer.parseInt(livehouseIdParam);

            LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            LocalDateTime startTime = LocalDateTime.parse(date + "T" + time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // DBManager初期化
            DBManager dbManager = DBManager.getInstance();

            // Artist_group情報の取得
            Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);
            Artist_group artistGroup = artistGroupDAO.getGroupByUserId(userId);

            if (artistGroup == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたアーティストグループが見つかりません。");
                return;
            }

            // Livehouse_information情報の取得
            Livehouse_informationDAO livehouseDAO = new Livehouse_informationDAO(dbManager);
            Livehouse_information livehouseInfo = livehouseDAO.getLivehouse_informationById(livehouseId);

            if (livehouseInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたライブハウス情報が見つかりません。");
                return;
            }

            // モデルにデータを設定
            Livehouse_application application = new Livehouse_application(
                0, // idは自動採番される前提
                userId,
                livehouseId,
                date,
                false, // true_falseの初期値
                startTime.toLocalDate(), // LocalDateTime → LocalDate
                null, // 終了時間は未定の場合
                LocalDate.now(), // 作成日
                LocalDate.now(), // 更新日
                0, // cogig_or_solo
                0  // artist_group_id
            );

            // DAOを使用してデータベースに挿入
            Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);
            boolean result = applicationDAO.insertLivehouse_application(application);

            if (result) {
                // 確認画面へ値を渡す
                request.setAttribute("selectedYear", year);
                request.setAttribute("selectedMonth", month);
                request.setAttribute("selectedDay", day);
                request.setAttribute("artistGroup", artistGroup); // Artist_group情報を渡す
                request.setAttribute("livehouseInfo", livehouseInfo); // Livehouse_information情報を渡す
                request.setAttribute("application", application);

                // 予約完了ページへリダイレクト
                response.sendRedirect("At_livehouse_reservation_completed");
            } else {
                // エラーページを表示
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "データベースへの挿入に失敗しました。");
            }
        } catch (Exception e) {
            // エラー処理
            System.err.println("[ERROR] Error while processing booking confirmation");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーでエラーが発生しました: " + e.getMessage());
        }
    }
}
