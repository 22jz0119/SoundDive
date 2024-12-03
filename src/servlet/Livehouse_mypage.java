package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager; // 必要に応じてDBManagerをインポート
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

/**
 * Servlet implementation class Livehouse_mypage
 */
@WebServlet("/Livehouse_mypage")
public class Livehouse_mypage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Livehouse_informationDAO dao; // DAOのインスタンスをクラスメンバとして保持

    @Override
    public void init() throws ServletException {
        // DBManagerのインスタンスを取得してDAOを初期化
        DBManager dbManager = DBManager.getInstance(); // シングルトン実装
        dao = new Livehouse_informationDAO(dbManager);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = 1; // セッションなどからユーザーIDを取得する（仮のIDを指定）

        // DAOでデータを取得
        Livehouse_information livehouse = dao.getLivehouse_informationById(userId);

        // 取得したデータをリクエストスコープにセット
        request.setAttribute("livehouse", livehouse);

        // JSPにフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
    }

    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 入力値の取得
        String livehouseName = request.getParameter("livehouseName");
        String ownerName = request.getParameter("ownerName");
        String liveTelNumber = request.getParameter("liveTelNumber");
        String livehouseExplanation = request.getParameter("livehouseExplanation");
        String livehouseDetailed = request.getParameter("livehouseDetailed");
        String equipmentInformation = request.getParameter("equipmentInformation");

        // モデルオブジェクトを作成
        Livehouse_information livehouse = new Livehouse_information(
            0, // IDは自動生成される場合は0またはnullを指定
            ownerName,
            equipmentInformation,
            livehouseExplanation,
            livehouseDetailed,
            livehouseName,
            "未入力", // 必要に応じてフォームから取得
            liveTelNumber,
            new Date(), // 現在時刻
            new Date()  // 現在時刻
        );

        // DAOで保存処理
        boolean isInserted = dao.insertLivehouse_information(livehouse);

        // 結果に応じた処理
        if (isInserted) {
            // 保存後のデータを取得して再表示
            Livehouse_information savedLivehouse = dao.getLivehouse_informationById(livehouse.getId());
            request.setAttribute("livehouse", savedLivehouse);

            // JSPにフォワード
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp");
            dispatcher.forward(request, response);
        } else {
            // エラーメッセージを設定して再表示
            request.setAttribute("errorMessage", "データの保存に失敗しました。");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp");
            dispatcher.forward(request, response);
        }
    }


}
