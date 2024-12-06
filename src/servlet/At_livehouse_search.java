package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/At-livehouse_search")
public class At_livehouse_search extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Livehouse_informationDAO livehouseInformationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = DBManager.getInstance();
        livehouseInformationDAO = new Livehouse_informationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
    	    List<Livehouse_information> livehouseList = livehouseInformationDAO.get();
    	    if (livehouseList == null || livehouseList.isEmpty()) {
    	        System.err.println("ライブハウス情報が見つかりませんでした。");
    	    }
    	    request.setAttribute("livehouseList", livehouseList);
    	    request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);
    	} catch (Exception e) {
    	    System.err.println("エラー: " + e.getMessage()); // エラーメッセージを出力
    	    e.printStackTrace(); // 詳細なスタックトレース
    	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス情報の取得中にエラー: " + e.getMessage());
    	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            String searchQuery = request.getParameter("q");
            System.err.println("検索クエリ: " + searchQuery);

            // 検索クエリを使用
            List<Livehouse_information> livehouseList = livehouseInformationDAO.searchLivehouses(searchQuery);

            request.setAttribute("livehouseList", livehouseList);
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("ライブハウス検索中にエラーが発生しました。");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス検索中にエラーが発生しました。");
        }
    }
}
