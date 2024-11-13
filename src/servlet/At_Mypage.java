package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;

/**
 * Servlet implementation class At_Mypage
 */
@WebServlet("/At_Mypage")
public class At_Mypage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Artist_groupDAO artistGroupDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // DBManagerのインスタンスをシングルトンパターンで取得
        DBManager dbManager = DBManager.getInstance();
        // Artist_groupDAOのインスタンスを作成
        artistGroupDAO = new Artist_groupDAO(dbManager);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ここで必要な処理を行う (例: artistGroupDAO を使ってデータベースからデータを取得する)
        
        // JSPにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/at_mypage.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
