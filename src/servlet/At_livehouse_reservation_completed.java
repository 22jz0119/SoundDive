package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/At_livehouse_reservation_completed")
public class At_livehouse_reservation_completed extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 予約完了メッセージ
            String reservationMessage = "予約が完了しました。";

            // 必要なデータをリクエストスコープに設定
            request.setAttribute("reservationMessage", reservationMessage);

            // 予約完了ページにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-reservation-completed.jsp").forward(request, response);

        } catch (Exception e) {
            // エラー処理
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーでエラーが発生しました: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GETリクエストの場合も同様の処理をする
        doPost(request, response);
    }
}
