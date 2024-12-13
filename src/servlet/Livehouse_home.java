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
            // パラメータの取得とデフォルト値
            int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : java.time.LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : java.time.LocalDate.now().getMonthValue();

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月の値が不正です: " + month);
            }

            // 予約件数を取得
            Map<String, Integer> reservationCounts = dao.getReservationCountsForMonth(year, month);

            // JSPにデータを渡す
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("reservationCounts", reservationCounts);

        } catch (NumberFormatException e) {
            log("パラメータ形式エラー: yearまたはmonthの値が不正です", e);
            request.setAttribute("errorMessage", "日付パラメータが不正です。正しい値を入力してください。");
        } catch (IllegalArgumentException e) {
            log("パラメータエラー: " + e.getMessage(), e);
            request.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            log("エラー発生: リクエスト処理中に問題が発生しました", e);
            request.setAttribute("errorMessage", "システムエラーが発生しました。時間をおいて再度お試しください。");
        }

        // JSPにフォワード (エラー時も同じJSPで処理)
        request.getRequestDispatcher("WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
    }
}