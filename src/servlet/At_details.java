package servlet;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/At_details")
public class At_details extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[DEBUG] Query String: " + request.getQueryString());
        request.setCharacterEncoding("UTF-8");

        try {
            // リクエストから必要なパラメータを取得
            String livehouseIdParam = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");
            String userIdParam = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");

            // パラメータのデバッグログ
            System.out.println("[DEBUG] Received Parameters - livehouseId: " + livehouseIdParam +
                               ", livehouseType: " + livehouseType + 
                               ", userId: " + userIdParam + 
                               ", applicationId: " + applicationIdParam);

            if (livehouseIdParam == null || livehouseIdParam.isEmpty()) {
                System.err.println("[ERROR] livehouseId is missing.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません。");
                return;
            }

            // IDの変換
            int livehouseId;
            try {
                livehouseId = Integer.parseInt(livehouseIdParam);
                System.out.println("[DEBUG] Parsed livehouseId: " + livehouseId);
            } catch (NumberFormatException e) {
                System.err.println("[ERROR] Invalid livehouseId format: " + livehouseIdParam);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスID形式です。");
                return;
            }

            // DAOの初期化
            System.out.println("[DEBUG] Initializing DAOs");
            DBManager dbManager = DBManager.getInstance();
            Livehouse_informationDAO livehouseInfoDao = new Livehouse_informationDAO(dbManager);
            Livehouse_applicationDAO livehouseAppDao = new Livehouse_applicationDAO(dbManager);

            // ライブハウス情報を取得
            System.out.println("[DEBUG] Fetching livehouse information for ID: " + livehouseId);
            Livehouse_information livehouseInfo = livehouseInfoDao.getLivehouse_informationById(livehouseId);
            if (livehouseInfo == null) {
                System.err.println("[ERROR] Livehouse not found for ID: " + livehouseId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたライブハウスが見つかりません。");
                return;
            }
            System.out.println("[DEBUG] Retrieved Livehouse Information: " + livehouseInfo);

            // 年と月のパラメータを取得
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");

            int year = (yearParam != null && !yearParam.isEmpty())
                    ? Integer.parseInt(yearParam)
                    : java.time.LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty())
                    ? Integer.parseInt(monthParam)
                    : java.time.LocalDate.now().getMonthValue();
            System.out.println("[DEBUG] Year: " + year + ", Month: " + month);

            // 日数の計算
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            System.out.println("[DEBUG] Days in Month: " + daysInMonth);

            // 日ごとの予約状況を取得
            System.out.println("[DEBUG] Fetching reservation status for livehouseId: " + livehouseId);
            Map<Integer, String> reservationStatus = livehouseAppDao.getReservationStatusByMonthAndLivehouseId(livehouseId, year, month);
            System.out.println("[DEBUG] Generated Reservation Status Map: " + reservationStatus);

            // JSON形式に変換
            Gson gson = new Gson();
            String reservationStatusJson = gson.toJson(reservationStatus);
            System.out.println("[DEBUG] Reservation Status JSON: " + reservationStatusJson);

            // リクエストスコープに共通データをセット
            System.out.println("[DEBUG] Setting request attributes");
            request.setAttribute("livehouse", livehouseInfo);
            request.setAttribute("reservationStatus", reservationStatusJson);
            request.setAttribute("daysInMonth", daysInMonth);
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);

            // マルチライブの場合の追加データ
            if ("multi".equals(livehouseType)) {
                System.out.println("[DEBUG] Multi livehouse type detected");
                if (userIdParam == null || userIdParam.isEmpty() || applicationIdParam == null || applicationIdParam.isEmpty()) {
                    System.err.println("[ERROR] Missing userId or applicationId for multi livehouse.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ユーザーIDまたは申請IDが指定されていません。");
                    return;
                }

                int userId = Integer.parseInt(userIdParam);
                int applicationId = Integer.parseInt(applicationIdParam);
                request.setAttribute("userId", userId);
                request.setAttribute("applicationId", applicationId);

                System.out.println("[DEBUG] Multi Livehouse - userId: " + userId + ", applicationId: " + applicationId);
            }

            // JSPにフォワード
            System.out.println("[DEBUG] Forwarding to JSP: /WEB-INF/jsp/artist/at_details.jsp");
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at_details.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[ERROR] Exception occurred: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "エラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 必要なら POST リクエスト用の処理を追加
    }
}
