package servlet;

import java.io.IOException;
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

        // true_false = 0 の申請リストを取得
        List<LivehouseApplicationWithGroup> applicationList = livehouseApplicationDAO.getReservationsWithTrueFalseZero();

        // リクエストスコープに設定
        request.setAttribute("applicationList", applicationList);

        // JSPページにフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
