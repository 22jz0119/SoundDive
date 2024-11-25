package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

public class At_Details extends HttpServlet {

    private Livehouse_informationDAO livehouseInfoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // DBManagerからDAOを初期化
        livehouseInfoDAO = new Livehouse_informationDAO(DBManager.getInstance());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 全てのライブハウス情報を取得
        List<Livehouse_information> livehouseList = livehouseInfoDAO.get();
        
        // 取得した情報をリクエスト属性としてJSPに渡す
        request.setAttribute("livehouseList", livehouseList);
        
        // JSPページに転送
        RequestDispatcher dispatcher = request.getRequestDispatcher("/livehouseList.jsp");
        dispatcher.forward(request, response);
    }
}
