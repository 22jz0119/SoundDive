package servlet;

import java.io.IOException;
import java.time.YearMonth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/At_Reservation")
public class At_Reservation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // DAOの初期化
        Livehouse_informationDAO livehouseDAO = new Livehouse_informationDAO(DBManager.getInstance());

        try {
            // 必須パラメータの取得
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");
            String dayParam = request.getParameter("day");
            String livehouseIdParam = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");

            // 入力チェック
            if (yearParam == null || yearParam.trim().isEmpty() ||
                monthParam == null || monthParam.trim().isEmpty() ||
                dayParam == null || dayParam.trim().isEmpty() ||
                livehouseIdParam == null || livehouseIdParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが不足しています。");
                return;
            }

            // パラメータを整数に変換
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int day = Integer.parseInt(dayParam);
            int livehouseId = Integer.parseInt(livehouseIdParam);

            // 日付のバリデーション
            YearMonth yearMonth = YearMonth.of(year, month);
            if (day < 1 || day > yearMonth.lengthOfMonth()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日付が不正です");
                return;
            }

            // ライブハウス情報の取得
            Livehouse_information livehouse = livehouseDAO.getLivehouse_informationById(livehouseId);
            if (livehouse == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ライブハウス情報が見つかりませんでした。");
                return;
            }

            // 必須データをリクエストスコープに設定
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);
            request.setAttribute("livehouse", livehouse);

            // マルチライブ用の追加データチェック
            if ("multi".equalsIgnoreCase(livehouseType)) {
                String userIdParam = request.getParameter("userId");
                String applicationIdParam = request.getParameter("applicationId");
                if (userIdParam == null || userIdParam.trim().isEmpty() ||
                    applicationIdParam == null || applicationIdParam.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "マルチライブにはユーザーIDと申請IDが必要です");
                    return;
                }

                int userId = Integer.parseInt(userIdParam);
                int applicationId = Integer.parseInt(applicationIdParam);

                // マルチライブ用データをリクエストスコープに追加
                request.setAttribute("userId", userId);
                request.setAttribute("applicationId", applicationId);
            }

            // 次のページにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at_reservation.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なパラメータ形式です。");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
        }
    }
}
