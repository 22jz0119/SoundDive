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
    private Livehouse_informationDAO livehouseInformationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // DBManagerのシングルトンインスタンスを取得してDAOを初期化
        DBManager dbManager = DBManager.getInstance();
        livehouseInformationDAO = new Livehouse_informationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // DAOからすべてのライブハウス情報を取得
            List<Livehouse_information> livehouseList = livehouseInformationDAO.get();
            
            if (livehouseList == null || livehouseList.isEmpty()) {
                System.out.println("ライブハウス情報が見つかりませんでした。");
            }
            
            String ownerName = request.getParameter("owner_name");
            String equipmentInformation = request.getParameter("equipment_information");
            String livehouseExplanationInformation = request.getParameter("livehouse_explanation_information");
            String livehouseDetailedInformation = request.getParameter("livehouse_detailed_information");
            String livehouseName = request.getParameter("livehouse_name");
            String liveAddress = request.getParameter("live_address");
            String liveTelNumber = request.getParameter("live_tel_number");
            String createDate = request.getParameter("create_date"); // 日付フォーマットが適切であることを確認
            String updateDate = request.getParameter("update_date"); // 同上

            // 取得したデータをリクエストスコープに保存
            request.setAttribute("livehouseList", livehouseList);

            // JSPにフォワード
            request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // エラー発生時、エラーメッセージを返す
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ライブハウス情報の取得中にエラーが発生しました。");
        }
        

      
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
    	  request.setCharacterEncoding("UTF-8");

    	    // フォームから検索キーワードを取得
    	    String searchQuery = request.getParameter("q");

    	    // 検索結果をDAOから取得
    	    List<Livehouse_information> livehouseList = livehouseInformationDAO.searchLivehouses(searchQuery);

    	    // リクエストスコープに検索結果をセット
    	    request.setAttribute("livehouseList", livehouseList);

    	    // 検索結果を表示するJSPへフォワード
    	    request.getRequestDispatcher("/WEB-INF/jsp/artist/at-livehouse-search.jsp").forward(request, response);
    }
}
