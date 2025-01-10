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
        String action = request.getParameter("action");
        
        if ("getReservationData".equals(action)) {
            // 予約データ取得処理
            handleReservationData(request, response);
            return;
        }
        

        // それ以外の場合は詳細画面の処理
        handleDetails(request, response);
    }


    /**
     * 予約データを取得する処理
     */
    private void handleReservationData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");
            String livehouseIdParam = request.getParameter("livehouseId");

            if (yearParam == null || monthParam == null || livehouseIdParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }

            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int livehouseId = Integer.parseInt(livehouseIdParam);

            // DAOを初期化して予約データを取得
            DBManager dbManager = DBManager.getInstance();
            Livehouse_applicationDAO livehouseAppDao = new Livehouse_applicationDAO(dbManager);
            Map<Integer, String> reservationStatus = livehouseAppDao.getReservationStatusByMonthAndLivehouseId(livehouseId, year, month);

            // JSON形式で返却
            Gson gson = new Gson();
            String json = gson.toJson(reservationStatus);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

            
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch reservation data: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while fetching reservation data.");
        }
    }

    /**
     * 詳細画面を処理する
     */
    private void handleDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String livehouseIdParam = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");
            String userIdParam = request.getParameter("userId");
            String applicationIdParam = request.getParameter("applicationId");

            if (livehouseIdParam == null || livehouseIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません。");
                return;
            }

            int livehouseId = Integer.parseInt(livehouseIdParam);

            // DAOの初期化
            DBManager dbManager = DBManager.getInstance();
            Livehouse_informationDAO livehouseInfoDao = new Livehouse_informationDAO(dbManager);
            Livehouse_applicationDAO livehouseAppDao = new Livehouse_applicationDAO(dbManager);

            // ライブハウス情報を取得
            Livehouse_information livehouseInfo = livehouseInfoDao.getLivehouse_informationById(livehouseId);
            if (livehouseInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたライブハウスが見つかりません。");
                return;
            }

            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");

            int year = (yearParam != null && !yearParam.isEmpty())
                    ? Integer.parseInt(yearParam)
                    : java.time.LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty())
                    ? Integer.parseInt(monthParam)
                    : java.time.LocalDate.now().getMonthValue();

            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            Map<Integer, String> reservationStatus = livehouseAppDao.getReservationStatusByMonthAndLivehouseId(livehouseId, year, month);

            Gson gson = new Gson();
            String reservationStatusJson = gson.toJson(reservationStatus);

            request.setAttribute("livehouse", livehouseInfo);
            request.setAttribute("reservationStatus", reservationStatusJson);
            request.setAttribute("daysInMonth", daysInMonth);
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);

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
