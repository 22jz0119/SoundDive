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

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");
        String userId = request.getParameter("userId");
        String livehouseIdParam = request.getParameter("livehouseId"); // livehouseId を受け取る

        try {
            // パラメータのバリデーション - 年月日
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int day = Integer.parseInt(dayParam);

            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();

            if (day < 1 || day > daysInMonth) {
                System.err.println("[ERROR] Invalid date: " + year + "-" + month + "-" + day);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日付が不正です");
                return;
            }

            System.out.println("[DEBUG] Valid Date: " + year + "-" + month + "-" + day);

            // userIdのバリデーション
            if (userId == null || userId.trim().isEmpty()) {
                System.err.println("[ERROR] Invalid userId: " + userId);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDが指定されていません");
                return;
            }

            System.out.println("[DEBUG] Received userId: " + userId);

            // livehouseId のバリデーション
            int livehouseId = -1; // デフォルト値として無効な値を設定
            if (livehouseIdParam != null && !livehouseIdParam.trim().isEmpty()) {
                try {
                    livehouseId = Integer.parseInt(livehouseIdParam);
                    System.out.println("[DEBUG] Received livehouseId: " + livehouseId);
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] Invalid livehouseId: " + livehouseIdParam);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが不正です");
                    return;
                }
            } else {
                System.err.println("[ERROR] livehouseId is missing");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません");
                return;
            }

            // フォワードまたは他の処理に進む
            // 必要に応じて次の画面にリダイレクトまたはフォワード
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("day", day);
            request.setAttribute("userId", userId);
            request.setAttribute("livehouseId", livehouseId);  // livehouseId をリクエストに追加

            // 次のページにフォワード (例: reservationDetails.jsp)
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at_reservation.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("[ERROR] Invalid parameters: year=" + yearParam + ", month=" + monthParam + ", day=" + dayParam);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "パラメータが不正です");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 必要に応じてPOST処理を追加
    }
}
