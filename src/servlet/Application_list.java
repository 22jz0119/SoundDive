package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        // パラメータが有効であれば、整数に変換して取得
        int year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : -1;
        int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : -1;
        int day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : -1;

        System.out.println("[DEBUG] Received parameters - year: " + year + ", month: " + month + ", day: " + day);

        // 初期化
        List<LivehouseApplicationWithGroup> applicationList1 = new ArrayList<>();
        List<LivehouseApplicationWithGroup> applicationList2 = new ArrayList<>();

        if (year != -1 && month != -1 && day != -1) {
            // cogig_or_solo パラメータを取得し、デフォルト値を設定
            String cogigOrSoloParam = request.getParameter("cogig_or_solo");
            System.out.println("[DEBUG] cogig_or_solo parameter received: " + cogigOrSoloParam);  // 追加したデバッグログ
            int cogigOrSolo = (cogigOrSoloParam == null || cogigOrSoloParam.isEmpty()) ? 1 : Integer.parseInt(cogigOrSoloParam);

            System.out.println("[DEBUG] Parsed cogig_or_solo: " + cogigOrSolo);

            // 1の処理（基本情報の取得）
            System.out.println("[DEBUG] Fetching reservations with cogig_or_solo = 1");
            applicationList1 = livehouseApplicationDAO.getReservationsWithTrueFalseZero(year, month, day);

            // 2の処理（追加でグループ情報を取得）
            if (cogigOrSolo == 2) {
                System.out.println("[DEBUG] Fetching reservations with cogig_or_solo = 2");
                applicationList2 = livehouseApplicationDAO.getReservationsByCogigOrSolo(year, month, day);
            }

            // 取得データをマージしてリクエストスコープに設定
            List<LivehouseApplicationWithGroup> mergedApplicationList = new ArrayList<>(applicationList1);
            mergedApplicationList.addAll(applicationList2);

            System.out.println("[DEBUG] Final mergedApplicationList size: " + mergedApplicationList.size());
            for (LivehouseApplicationWithGroup app : mergedApplicationList) {
                System.out.println("[DEBUG] Application: " + app.getAccountName() + ", Genre: " + app.getGroupGenre());
            }

            request.setAttribute("applicationList", mergedApplicationList);
            request.setAttribute("cogigOrSolo", cogigOrSolo);

        } else {
            System.out.println("[DEBUG] Invalid or missing date parameters");
            request.setAttribute("errorMessage", "無効な日付が指定されています。");
            request.setAttribute("cogigOrSolo", 1); // デフォルト値を設定
        }

        // JSPページにフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
