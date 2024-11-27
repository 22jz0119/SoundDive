package servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.DBManager;
import dao.Livehouse_applicationDAO;

/**
 * Servlet implementation class Livehouse_home
 */
@WebServlet("/Livehouse_home")
public class Livehouse_home extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Livehouse_applicationDAO dao;

    @Override
    public void init() throws ServletException {
        DBManager dbManager = DBManager.getInstance();
        dao = new Livehouse_applicationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        try {
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);

            // 月の範囲（1-12）をチェック
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            // 該当月の予約件数を取得
            Map<String, Integer> reservationCounts = dao.getReservationCountsForMonth(year, month);

            // JSON形式で結果を返す
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = new Gson().toJson(reservationCounts);
            response.getWriter().write(json);

        } catch (Exception e) {
            log("エラー: ", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "リクエスト処理中にエラーが発生しました");
        }
    }


}
