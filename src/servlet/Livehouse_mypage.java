package servlet;

import java.io.IOException;
import java.util.Date;

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
    	request.setCharacterEncoding("UTF-8"); // リクエストの文字エンコーディングを設定
        response.setContentType("text/html; charset=UTF-8"); // レスポンスの文字セットを設定
        response.setCharacterEncoding("UTF-8");
    	
    	// 入力値の取得
        String livehouseName = request.getParameter("livehouseName");
        String ownerName = request.getParameter("ownerName");
        String liveTelNumber = request.getParameter("liveTelNumber");
        String livehouseExplanation = request.getParameter("livehouseExplanation");
        String livehouseDetailed = request.getParameter("livehouseDetailed");
        String equipmentInformation = request.getParameter("equipmentInformation");

        // 入力値のバリデーション
        if (livehouseName == null || livehouseName.isEmpty() || ownerName == null || ownerName.isEmpty()) {
            request.setAttribute("errorMessage", "ライブハウス名またはオーナー名を入力してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
            return;
        }

        // モデルオブジェクトを作成
        Livehouse_information livehouse = new Livehouse_information(
            0, // 自動生成される場合
            ownerName,
            equipmentInformation,
            livehouseExplanation,
            livehouseDetailed,
            livehouseName,
            "未入力",
            liveTelNumber,
            new Date(), // 現在時刻
            new Date()
        );

        // DAOで保存処理
        boolean isInserted = dao.insertLivehouse_information(livehouse);

        // 結果に応じた処理
        if (isInserted) {
            // 成功メッセージを設定
            request.setAttribute("successMessage", "データが正常に保存されました。");
            request.setAttribute("livehouse", livehouse);

        } else {
            // エラーメッセージを設定
            request.setAttribute("errorMessage", "データの保存に失敗しました。");
        }

        // JSPにフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
    }
}
