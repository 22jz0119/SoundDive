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

        List<LivehouseApplicationWithGroup> applicationList1 = new ArrayList<>();
        List<LivehouseApplicationWithGroup> applicationList2 = new ArrayList<>();

        if (year != -1 && month != -1 && day != -1) {
            // cogig_or_solo = 1 のデータを取得
            System.out.println("[DEBUG] Fetching reservations with cogig_or_solo = 1");
            applicationList1 = livehouseApplicationDAO.getReservationsWithTrueFalseZero(year, month, day);

            // cogig_or_solo = 2 のデータを取得
            System.out.println("[DEBUG] Fetching reservations with cogig_or_solo = 2");
            applicationList2 = livehouseApplicationDAO.getReservationsByCogigOrSolo(year, month, day);

            // データをマージ
            List<LivehouseApplicationWithGroup> mergedApplicationList = new ArrayList<>();
            System.out.println("[DEBUG] Adding cogig_or_solo = 1 data, size: " + applicationList1.size());
            mergedApplicationList.addAll(applicationList1);

            System.out.println("[DEBUG] Adding cogig_or_solo = 2 data, size: " + applicationList2.size());
            mergedApplicationList.addAll(applicationList2);

            // リクエストスコープに設定
            request.setAttribute("applicationList", mergedApplicationList);
        } else {
            // パラメータが無効または不足している場合、エラーメッセージを設定
            if (year == -1) System.out.println("[DEBUG] Missing or invalid parameter: year");
            if (month == -1) System.out.println("[DEBUG] Missing or invalid parameter: month");
            if (day == -1) System.out.println("[DEBUG] Missing or invalid parameter: day");

            System.out.println("[DEBUG] Invalid or missing date parameters");
            request.setAttribute("errorMessage", "年、月、日を正しく入力してください。");
        }

        // JSPページにフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}