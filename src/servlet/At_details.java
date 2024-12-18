package servlet;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/At_details")
public class At_details extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // リクエストからライブハウスIDとユーザーID、applicationIdを取得
            String livehouseIdParam = request.getParameter("livehouseId");
            String userIdParam = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");

            // パラメータがない場合のエラーハンドリング
            if (livehouseIdParam == null || livehouseIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません。");
                return;
            }
            if (userIdParam == null || userIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDが指定されていません。");
                return;
            }
            if (applicationIdParam == null || applicationIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "申請IDが指定されていません。");
                return;
            }

            // IDの変換
            int livehouseId;
            int userId;
            int applicationId;
            try {
                livehouseId = Integer.parseInt(livehouseIdParam);
                userId = Integer.parseInt(userIdParam);
                applicationId = Integer.parseInt(applicationIdParam);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なID形式です。");
                return;
            }

            // デバッグログ
            System.out.println("[DEBUG] livehouseId: " + livehouseId);
            System.out.println("[DEBUG] userId: " + userId);
            System.out.println("[DEBUG] applicationId: " + applicationId);

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

            // 日数の計算
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

            // 日ごとの予約状況を取得
            Map<Integer, String> reservationStatus = livehouseAppDao.getReservationStatusByMonthAndLivehouseId(livehouseId, year, month);

            // JSON形式に変換
            Gson gson = new Gson();
            String reservationStatusJson = gson.toJson(reservationStatus);

            // デバッグログ
            System.out.println("[DEBUG] Reservation Status JSON: " + reservationStatusJson);

            // JSPにデータを渡す
            request.setAttribute("livehouse", livehouseInfo);
            request.setAttribute("reservationStatus", reservationStatusJson);
            request.setAttribute("daysInMonth", daysInMonth);
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("userId", userId);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("applicationId", applicationId); // applicationIdをセット

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
