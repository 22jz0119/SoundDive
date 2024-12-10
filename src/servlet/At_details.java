package servlet;

import java.io.IOException;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.Livehouse_informationDAO;
import model.Livehouse_application;
import model.Livehouse_information;

@WebServlet("/At_details")
public class At_details extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // リクエストからライブハウスIDを取得
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません。");
                return;
            }

            int livehouseId;
            try {
                livehouseId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスID形式です。");
                return;
            }

            // DAOの初期化
            DBManager dbManager = DBManager.getInstance();
            Livehouse_informationDAO livehouseInfoDao = new Livehouse_informationDAO(dbManager);
            Livehouse_applicationDAO livehouseAppDao = new Livehouse_applicationDAO(dbManager);

            // ライブハウス情報を取得
            Livehouse_information livehouseInfo = livehouseInfoDao.getLivehouse_informationById(livehouseId);
            if (livehouseInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたライブハウスが見つかりません。");
                return;
            }

            // 年と月のパラメータを取得
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");

            int year = (yearParam != null && !yearParam.isEmpty())
                    ? Integer.parseInt(yearParam)
                    : java.time.LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty())
                    ? Integer.parseInt(monthParam)
                    : java.time.LocalDate.now().getMonthValue();

            // 指定された年と月の日数を計算
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

            // 日ごとの予約状況を取得
            List<Livehouse_application> applications = livehouseAppDao.getLivehouse_applicationsByLivehouseId(livehouseId);
            Map<Integer, String> reservationStatus = new HashMap<>();

            // 日付の初期化
            for (int i = 1; i <= daysInMonth; i++) {
                reservationStatus.put(i, "〇"); // 初期値は空き（〇）
            }

            // アプリケーションデータを反映
            for (Livehouse_application application : applications) {
                if (application.getDate_time() != null &&
                        application.getDate_time().getMonthValue() == month &&
                        application.getDate_time().getYear() == year) {
                    int day = application.getDate_time().getDayOfMonth();
                    reservationStatus.put(day, application.isTrue_False() ? "×" : "〇");
                }
            }

            // デバッグ用ログを出力
            System.out.println("[DEBUG] Reservation Status Map: ");
            reservationStatus.forEach((day, status) -> 
                System.out.println("Day " + day + ": " + status)
            );

            // リクエストスコープにデータを保存
            request.setAttribute("livehouse", livehouseInfo);
            request.setAttribute("reservationStatus", reservationStatus);
            request.setAttribute("daysInMonth", daysInMonth);
            request.setAttribute("year", year);
            request.setAttribute("month", month);

            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at_details.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "エラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 必要なら POST リクエスト用の処理を追加
    }
}
