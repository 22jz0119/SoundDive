package servlet;

import java.io.IOException;
import java.time.YearMonth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/At_Reservation")
public class At_Reservation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // DAOの初期化
        DBManager dbManager = DBManager.getInstance();
        Livehouse_informationDAO livehouseDAO = new Livehouse_informationDAO(dbManager);
        Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);  // 追加
        Livehouse_applicationDAO applicationDAO = new Livehouse_applicationDAO(dbManager);

        try {
            // パラメータの取得
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");
            String dayParam = request.getParameter("day");
            String livehouseIdParam = request.getParameter("livehouseId");
            String livehouseType = request.getParameter("livehouse_type");
            String applicationIdParam = request.getParameter("applicationId");

            // 必須パラメータチェック
            if (yearParam == null || monthParam == null || dayParam == null || livehouseIdParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必要なパラメータが不足しています。");
                return;
            }

            // 型変換とエラーハンドリング
            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int day = Integer.parseInt(dayParam);
            int livehouseId = Integer.parseInt(livehouseIdParam);

            int applicationId = -1;  // 初期値
            if (applicationIdParam != null && !applicationIdParam.isEmpty()) {
                try {
                    applicationId = Integer.parseInt(applicationIdParam);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "申請IDの形式が正しくありません。");
                    return;
                }
            }

            // 日付のバリデーション
            YearMonth yearMonth = YearMonth.of(year, month);
            if (day < 1 || day > yearMonth.lengthOfMonth()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "日付が不正です。");
                return;
            }

            // ライブハウス情報の取得
            Livehouse_information livehouse = livehouseDAO.getLivehouse_informationById(livehouseId);
            if (livehouse == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "ライブハウス情報が見つかりませんでした。");
                return;
            }

            // 申請IDがあれば、artist_group_idを取得して画像情報を取得する
            if (applicationId != -1) {
                Integer artistGroupId = applicationDAO.getArtistGroupIdByApplicationId(applicationId);  // applicationIdからartist_group_idを取得
                if (artistGroupId != null) {
                    String pictureImageMovie = artistGroupDAO.getPictureImageMovieByArtistGroupId(artistGroupId);  // artist_group_idを使って画像を取得
                    if (pictureImageMovie != null) {
                        // 画像が見つかった場合、リクエストスコープに設定
                        request.setAttribute("pictureImageMovie", pictureImageMovie);
                    } else {
                        // 画像が見つからなかった場合
                        request.setAttribute("errorMessage", "アーティストグループの画像が見つかりませんでした。");
                    }
                } else {
                    // artist_group_idが見つからなかった場合
                    request.setAttribute("errorMessage", "アーティストグループが見つかりませんでした。");
                }
            }
            

            // マルチライブの場合の処理
            if ("multi".equalsIgnoreCase(livehouseType) && applicationId != -1) {
                // 申請IDからアーティスト名を取得
                String artistName = applicationDAO.getArtistNameByApplicationId(applicationId);
                if (artistName != null) {
                    request.setAttribute("artistName", artistName);
                } else {
                    request.setAttribute("errorMessage", "アーティスト情報が見つかりませんでした。");
                }
                request.setAttribute("applicationId", applicationId);
            }

            // リクエストスコープにデータを設定
            request.setAttribute("selectedYear", year);
            request.setAttribute("selectedMonth", month);
            request.setAttribute("selectedDay", day);
            request.setAttribute("livehouseId", livehouseId);
            request.setAttribute("livehouseType", livehouseType);
            request.setAttribute("livehouse", livehouse);

            // 詳細画面へフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at_reservation.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無効なパラメータ形式です。");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "サーバーエラーが発生しました。");
        }
    }
}
