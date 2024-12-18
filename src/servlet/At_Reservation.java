package servlet;

import java.io.IOException;
import java.time.YearMonth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class At_Reservation
 */
@WebServlet("/At_Reservation")
public class At_Reservation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // リクエストパラメータの取得
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");
        String livehouseIdParam = request.getParameter("livehouseId");
        String livehouseType = request.getParameter("livehouse_type"); // livehouse_type を受け取る

        // パラメータログ
        System.out.println("[DEBUG] Received year: " + yearParam);
        System.out.println("[DEBUG] Received month: " + monthParam);
        System.out.println("[DEBUG] Received day: " + dayParam);
        System.out.println("[DEBUG] Received livehouseId: " + livehouseIdParam);
        System.out.println("[DEBUG] Received livehouseType: " + livehouseType);

        try {
            // 入力チェック - 年月日が存在しているか、空でないかをチェック
            if (yearParam == null || yearParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "年が指定されていません。");
                return;
            }
            if (monthParam == null || monthParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "月が指定されていません。");
                return;
            }
            if (dayParam == null || dayParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日が指定されていません。");
                return;
            }

            // livehouseId のバリデーション
            int livehouseId = -1;
            if (livehouseIdParam != null && !livehouseIdParam.trim().isEmpty()) {
                try {
                    livehouseId = Integer.parseInt(livehouseIdParam);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが不正です");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません");
                return;
            }

            // livehouseType による処理分岐
            if (livehouseType == null || livehouseType.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスタイプが指定されていません");
                return;
            }

            if ("solo".equalsIgnoreCase(livehouseType)) {
                System.out.println("[DEBUG] ソロライブモード");
                // ソロの場合の処理は特に追加データは不要
            } else if ("multi".equalsIgnoreCase(livehouseType)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "マルチライブのリクエストには追加情報が必要です");
                return;
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスタイプです");
                return;
            }

            // 年、月、日を整数に変換
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int day = Integer.parseInt(dayParam);

            // 日付のバリデーション
            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();

            if (day < 1 || day > daysInMonth) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日付が不正です");
                return;
            }

            // 正常な場合は次の画面にデータを渡す
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("day", day);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);

            // ログ確認
            System.out.println("[DEBUG] Set attributes: year=" + year + ", month=" + month + ", day=" + day +
                               ", livehouseId=" + livehouseId + ", livehouseType=" + livehouseType);

            // 次のページにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-booking-confirmation.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効な年月日が指定されています。");
        }
    }
}