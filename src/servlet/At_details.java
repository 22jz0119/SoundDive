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
        try {
            // リクエストからライブハウスIDを取得
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません。");
                return;
            }

            int livehouseId;
            try {
                livehouseId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なライブハウスID形式です。");
                return;
            }

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

            // 年と月のパラメータを取得
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");

            int year = (yearParam != null && !yearParam.isEmpty())
                    ? Integer.parseInt(yearParam)
                    : java.time.LocalDate.now().getYear();
            int month = (monthParam != null && !monthParam.isEmpty())
                    ? Integer.parseInt(monthParam)
                    : java.time.LocalDate.now().getMonthValue();

            // 日ごとの予約状況を取得
            Map<Integer, Boolean> reservationStatus = livehouseAppDao.getDailyReservationStatus(livehouseInfo, year, month);

            // 申請詳細情報を取得
            List<Livehouse_application> applications = livehouseAppDao.getLivehouse_applicationsByLivehouseId(livehouseId);

            // リクエストスコープにデータを保存
            request.setAttribute("livehouse", livehouseInfo);
            request.setAttribute("reservationStatus", reservationStatus);
            request.setAttribute("applications", applications);
            request.setAttribute("year", year);
            request.setAttribute("month", month);

            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at_details.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス情報の取得中にエラーが発生しました。");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 必要なら POST リクエスト用の処理を追加
    }
}
