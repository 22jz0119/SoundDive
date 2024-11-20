package servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            // パラメータが不足している場合、現在の年月を使用
            int year = (yearParam != null) ? Integer.parseInt(yearParam) : java.time.YearMonth.now().getYear();
            int month = (monthParam != null) ? Integer.parseInt(monthParam) : java.time.YearMonth.now().getMonthValue();

            // 予約データを取得
            Map<Integer, Integer> reservationCounts = dao.getReservationCountByMonth(year, month);

            // JSPに渡すデータを設定
            request.setAttribute("reservationCounts", reservationCounts);
            request.setAttribute("year", year);
            request.setAttribute("month", month);

            // JSPにフォワード
            request.getRequestDispatcher("WEB-INF/jsp/livehouse_home.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // 数値パラメータが無効な場合
            request.setAttribute("error", "不正な年または月のパラメータが渡されました。");
            request.getRequestDispatcher("WEB-INF/jsp/livehouse_home.jsp").forward(request, response);
        } catch (Exception e) {
            // その他のエラー
            request.setAttribute("error", "予期しないエラーが発生しました。");
            e.printStackTrace();
            request.getRequestDispatcher("WEB-INF/jsp/livehouse_home.jsp").forward(request, response);
        }
    }

}
