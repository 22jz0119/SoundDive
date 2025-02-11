package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO; // 追加
import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;

@WebServlet("/Application_list")
public class Application_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);
        Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);

        // パラメータ取得
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");

        // パラメータを整数に変換
        int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : -1;
        int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : -1;
        int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;

        System.out.println("[DEBUG] Received parameters - year: " + year + ", month: " + month + ", day: " + day);

        List<LivehouseApplicationWithGroup> soloApplications = new ArrayList<>();
        List<LivehouseApplicationWithGroup> cogigApplications = new ArrayList<>();
        Map<Integer, String> pictureImageMap = new HashMap<>(); // groupIdごとの画像マッピング

        if (year != -1 && month != -1 && day != -1) {
            soloApplications = livehouseApplicationDAO.getReservationsWithTrueFalseZero(year, month, day);
            cogigApplications = livehouseApplicationDAO.getReservationsByCogigOrSolo(year, month, day);

            for (LivehouseApplicationWithGroup app : soloApplications) {
                int groupId = app.getGroupId();
                if (!pictureImageMap.containsKey(groupId)) {
                    String pictureImageMovie = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                    if (pictureImageMovie == null || pictureImageMovie.isEmpty()) {
                        pictureImageMovie = "/uploads/default_image.png"; // デフォルト画像を設定
                    }
                    pictureImageMap.put(groupId, pictureImageMovie);
                }
            }

            // cogig_or_solo = 2 のデータを取得
            System.out.println("[DEBUG] Fetching reservations with cogig_or_solo = 2");
            cogigApplications = livehouseApplicationDAO.getReservationsByCogigOrSolo(year, month, day);
            System.out.println("[DEBUG] cogigApplications size: " + cogigApplications.size());

            for (LivehouseApplicationWithGroup app : cogigApplications) {
                int groupId = app.getGroupId();
                if (!pictureImageMap.containsKey(groupId)) {
                    String pictureImageMovie = artistGroupDAO.getPictureImageMovieByArtistGroupId(groupId);
                    if (pictureImageMovie == null || pictureImageMovie.isEmpty()) {
                        pictureImageMovie = "/uploads/default_image.png"; // デフォルト画像を設定
                    }
                    pictureImageMap.put(groupId, pictureImageMovie);
                }
            }

            // データを JSP に渡す
            request.setAttribute("soloApplications", soloApplications);
            request.setAttribute("cogigApplications", cogigApplications);
            request.setAttribute("pictureImageMap", pictureImageMap); // JSP に画像マップを渡す
        } else {
            // パラメータエラー時の処理
            if (year == -1) System.out.println("[DEBUG] Missing or invalid parameter: year");
            if (month == -1) System.out.println("[DEBUG] Missing or invalid parameter: month");
            if (day == -1) System.out.println("[DEBUG] Missing or invalid parameter: day");

            System.out.println("[DEBUG] Invalid or missing date parameters");
            request.setAttribute("errorMessage", "年、月、日を正しく入力してください。");
        }

        // JSP へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
