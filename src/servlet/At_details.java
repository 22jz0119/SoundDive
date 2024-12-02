package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.Livehouse_informationDAO;
import model.Livehouse_application;
import model.Livehouse_information;

@WebServlet("/At_details")
public class At_details extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // リクエストID取得
        String idParam = request.getParameter("id");
        System.err.println("Received ID: " + idParam);  // 標準エラー出力にログを表示

        if (idParam == null) {
            System.err.println("ID parameter is missing.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required.");
            return;
        }

        int livehouseId;
        try {
            livehouseId = Integer.parseInt(idParam);  // IDを整数型に変換
            System.err.println("Parsed livehouseId: " + livehouseId);  // 標準エラー出力にログを表示
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + idParam);  // エラーログ
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
            return;
        }

        // DAO呼び出し
        Livehouse_informationDAO livehouseDao = new Livehouse_informationDAO(DBManager.getInstance());
        Livehouse_applicationDAO applicationDao = new Livehouse_applicationDAO(DBManager.getInstance());

        // Livehouse_information を取得
        Livehouse_information livehouse = livehouseDao.getLivehouse_informationById(livehouseId);

        if (livehouse == null) {
            System.err.println("Livehouse not found for ID: " + livehouseId);  // 標準エラー出力にログを表示
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Livehouse not found.");
            return;
        }

        // 現在の年と月を取得
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        int year = (yearParam != null) ? Integer.parseInt(yearParam) : java.time.LocalDate.now().getYear();
        int month = (monthParam != null) ? Integer.parseInt(monthParam) : java.time.LocalDate.now().getMonthValue();

        System.err.println("Using year: " + year + ", month: " + month);  // 標準エラー出力にログを表示

        // 日ごとの予約状態を取得
        Map<Integer, Boolean> reservationStatus = applicationDao.getDailyReservationStatus(livehouse, year, month);

        // 申請詳細情報を取得（追加部分）
        String applicationIdParam = request.getParameter("applicationId");
        if (applicationIdParam != null) {
            int applicationId = Integer.parseInt(applicationIdParam);
            List<Livehouse_application> application = applicationDao.getLivehouse_applicationsByLivehouseId(applicationId);

            if (application == null) {
                System.err.println("Livehouse application not found for ID: " + applicationId);  // 標準エラー出力にログを表示
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Livehouse application not found.");
                return;
            }

            // 申請詳細情報をJSPに渡す
            request.setAttribute("application", application);
        }

        // 他のリクエストパラメータの取得 (例: キーワード検索など)
        String searchQuery = request.getParameter("searchQuery");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            // 検索クエリに基づく処理を追加することができます
            System.err.println("Search query: " + searchQuery);  // 標準エラー出力にログを表示
        }

        // 取得したライブハウス情報と予約状態をJSPに渡す
        request.setAttribute("livehouse", livehouse);
        request.setAttribute("reservationStatus", reservationStatus);
        request.setAttribute("year", year);
        request.setAttribute("month", month);

        // JSPへフォワード
        System.err.println("Forwarding to at_details.jsp for livehouseId: " + livehouseId);  // 標準エラー出力にログを表示
        request.getRequestDispatcher("/WEB-INF/jsp/artist/at_details.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // フォームから検索キーワードを取得
        String searchQuery = request.getParameter("searchQuery");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            System.err.println("Search query received: " + searchQuery);  // 標準エラー出力にログを表示
            // 検索処理を実装
        }
    }
}
