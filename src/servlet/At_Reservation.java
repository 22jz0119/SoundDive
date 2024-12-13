package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import model.Artist_group;

/**
 * Servlet implementation class At_Reservation
 */
@WebServlet("/At_Reservation")
public class At_Reservation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // クエリパラメータから年、月、日を受け取る
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");
        String userIdParam = request.getParameter("userId");

        // userIdの確認
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDが指定されていません。");
            return;
        }
        int userId = Integer.parseInt(userIdParam);
        System.out.println("[DEBUG] userId: " + userId);

        // 年、月、日の確認
        if (yearParam == null || monthParam == null || dayParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "年、月、日が指定されていません。");
            return;
        }

        int year = Integer.parseInt(yearParam);
        int month = Integer.parseInt(monthParam);
        int day = Integer.parseInt(dayParam);
        System.out.println("[DEBUG] Selected Date: " + year + "-" + month + "-" + day);

        try {
            // DBManagerとDAOのインスタンスを初期化
            DBManager dbManager = DBManager.getInstance();
            Artist_groupDAO artistGroupDao = Artist_groupDAO.getInstance(dbManager);

            // userIdを使ってアーティストグループ情報を取得
            Artist_group artistGroup = artistGroupDao.getGroupByUserId(userId);

            if (artistGroup == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "アーティストグループが見つかりません。");
                return;
            }

            // デバッグログ: アーティストグループ情報
            System.out.println("[DEBUG] Retrieved Artist Group: " + artistGroup.getAccount_name());

            // JSPに渡すデータを設定
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("artistGroup", artistGroup);

            // 予約フォームや確認画面を表示するJSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at_reservation.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[ERROR] Error while processing reservation");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "エラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 必要に応じてPOST処理を追加
    }
}
