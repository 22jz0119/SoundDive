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
    
    // DAOのインスタンスをクラスレベルで保持
    private Livehouse_applicationDAO dao;

    @Override
    public void init() throws ServletException {
        // サーブレットの初期化時にDBManagerとDAOを初期化
        DBManager dbManager = DBManager.getInstance();
        dao = new Livehouse_applicationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // パラメータの取得
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        // パラメータのnullチェックとエラーハンドリング
        if (yearParam == null || monthParam == null) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing 'year' or 'month' parameter.\"}");
            return;
        }

        try {
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam); // 1月が1であると仮定

            // 予約データを取得
            Map<Integer, Integer> reservationCounts = dao.getReservationCountByMonth(year, month);

            // JSON形式に変換してレスポンスとして返す
            String json = new Gson().toJson(reservationCounts);
            response.setContentType("application/json");
            response.getWriter().write(json);

            // JSPにフォワード
            request.getRequestDispatcher("WEB-INF/jsp/livehouse_home.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // 数値に変換できない場合のエラーハンドリング
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid 'year' or 'month' parameter. Must be a number.\"}");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // POSTリクエストをGETリクエストとして処理
        doGet(request, response);
    }
}
