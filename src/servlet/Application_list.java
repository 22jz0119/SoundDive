package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

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

        // year, month, day パラメータを取得
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String dayParam = request.getParameter("day");
        String dataParam = request.getParameter("data"); // フロントエンドから送信されたデータ

        System.out.println("[DEBUG] Received parameters - year: " + yearParam + ", month: " + monthParam + ", day: " + dayParam);
        System.out.println("[DEBUG] Received data: " + dataParam);

        // パラメータが有効であれば、整数に変換して取得
        int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : -1;
        int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : -1;
        int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;

        // 初期化
        List<LivehouseApplicationWithGroup> applicationList = new ArrayList<>();

        if (year != -1 && month != -1 && day != -1 && dataParam != null && !dataParam.isEmpty()) {
            try {
                // フロントエンドから送信されたデータをパース (JSON配列)
                ObjectMapper mapper = new ObjectMapper();
                List<ReservationData> reservations = Arrays.asList(mapper.readValue(dataParam, ReservationData[].class));

                // 各予約データについて処理
                for (ReservationData reservation : reservations) {
                    int cogigOrSolo = reservation.getCogig_or_solo();
                    System.out.println("[DEBUG] Processing reservation with cogig_or_solo: " + cogigOrSolo);

                    // cogig_or_solo の値に応じたデータ取得
                    if (cogigOrSolo == 1) {
                        applicationList.addAll(livehouseApplicationDAO.getReservationsWithTrueFalseZero(year, month, day));
                    } else if (cogigOrSolo == 2) {
                        applicationList.addAll(livehouseApplicationDAO.getReservationsByCogigOrSolo(year, month, day));
                    }
                }

                System.out.println("[DEBUG] Final applicationList size: " + applicationList.size());
                for (LivehouseApplicationWithGroup app : applicationList) {
                    System.out.println("[DEBUG] Application: " + app.getAccountName() + ", Genre: " + app.getGroupGenre());
                }

                // JSP に渡すデータを設定
                request.setAttribute("applicationList", applicationList);
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to parse reservation data: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("errorMessage", "予約データの処理中にエラーが発生しました。");
            }
        } else {
            System.out.println("[DEBUG] Invalid or missing parameters");
            request.setAttribute("errorMessage", "無効なパラメータが指定されています。");
        }

        // JSPページにフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    // ReservationDataクラス
    private static class ReservationData {
        private int cogig_or_solo;

        public int getCogig_or_solo() {
            return cogig_or_solo;
        }

        public void setCogig_or_solo(int cogig_or_solo) {
            this.cogig_or_solo = cogig_or_solo;
        }
    }
}

