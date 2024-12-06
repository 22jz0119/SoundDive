package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.DBManager; // 必要に応じてDBManagerをインポート
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

/**
 * Servlet implementation class Livehouse_mypage
 */
@WebServlet("/Livehouse_mypage")
@MultipartConfig
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
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");

    	
    	// 入力値の取得
        String livehouseName = request.getParameter("livehouseName");
        String ownerName = request.getParameter("ownerName");
        String liveTelNumber = request.getParameter("liveTelNumber");
        String livehouseExplanation = request.getParameter("livehouseExplanation");
        String livehouseDetailed = request.getParameter("livehouseDetailed");
        String equipmentInformation = request.getParameter("equipmentInformation");

        // 画像の取得
        Part naikanImage = request.getPart("naikanImage");
        Part gaikanImage = request.getPart("gaikanImage");
        
        // 画像処理
        Part profileImagePart = request.getPart("picture_image_movie");
        String pictureImagePath = null;

        if (profileImagePart != null && profileImagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + profileImagePart.getSubmittedFileName();
            String uploadDir = getServletContext().getRealPath("/uploads/");
            File uploadDirFile = new File(uploadDir);

            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            pictureImagePath = "/uploads/" + fileName;

            try (InputStream inputStream = profileImagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(uploadDir + File.separator + fileName));
            } catch (IOException e) {
                request.setAttribute("errorMessage", "画像アップロード中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
                return;
            }
        }

        // 入力値のバリデーション
        if (livehouseName == null || livehouseName.isEmpty() ||
            ownerName == null || ownerName.isEmpty() ||
            liveTelNumber == null || liveTelNumber.isEmpty() ||
            livehouseExplanation == null || livehouseExplanation.isEmpty() ||
            livehouseDetailed == null || livehouseDetailed.isEmpty() ||
            equipmentInformation == null || equipmentInformation.isEmpty() ||
            naikanImage == null || naikanImage.getSize() == 0 || // 内観画像のチェック
            gaikanImage == null || gaikanImage.getSize() == 0) { // 外観画像のチェック

            // エラーメッセージをリクエストスコープに設定
            request.setAttribute("errorMessage", "すべての必須項目と画像を記入してください。");
            
            // JSPにフォワード
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
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
        } else {
            // エラーメッセージを設定
            request.setAttribute("errorMessage", "データの保存に失敗しました。");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
        }
    }
}
