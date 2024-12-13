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
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.DBManager;
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
        super.init();
        DBManager dbManager = DBManager.getInstance();
        dao = new Livehouse_informationDAO(dbManager); // DBManagerをDAOに渡す
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        try {
            // すでに登録されているライブハウス情報を取得
            Livehouse_information livehouse = dao.getLivehouse_informationById(userId);

            if (livehouse != null) {
                // 登録されている場合、マイページに表示
                request.setAttribute("livehouse", livehouse);
             // Livehouse_information が正しく取得されているか確認
                System.out.println("Livehouse: " + livehouse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データ取得中にエラーが発生しました。");
        }

        // マイページのJSPにフォワード
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

        // セッションからuserIdを取得
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        // 入力値の取得
        String livehouseName = request.getParameter("livehouseName");
        String ownerName = request.getParameter("ownerName");
        String liveTelNumber = request.getParameter("liveTelNumber");
        String livehouseExplanation = request.getParameter("livehouseExplanation");
        String livehouseDetailed = request.getParameter("livehouseDetailed");
        String equipmentInformation = request.getParameter("equipmentInformation");

        // 画像アップロード処理
        Part naikanImage = request.getPart("naikanImage");
        Part gaikanImage = request.getPart("gaikanImage");
        Part profileImagePart = request.getPart("picture_image_naigaikan");

        String naikanImagePath = null;
        String gaikanImagePath = null;
        String pictureImagePath = null;

        try {
            // アップロードディレクトリの取得
            String uploadDir = getServletContext().getRealPath("/uploads/");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // 画像の保存
            naikanImagePath = saveImage(naikanImage, uploadDir, "内観");
            gaikanImagePath = saveImage(gaikanImage, uploadDir, "外観");
            pictureImagePath = saveImage(profileImagePart, uploadDir, "プロフィール");

            if (naikanImagePath == null || gaikanImagePath == null || pictureImagePath == null) {
                request.setAttribute("errorMessage", "すべての画像をアップロードしてください。");
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "画像アップロード中にエラーが発生しました。");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
            return;
        }

        
        // 入力値のバリデーション
        if (livehouseName == null || livehouseName.isEmpty() ||
            ownerName == null || ownerName.isEmpty() ||
            liveTelNumber == null || liveTelNumber.isEmpty() ||
            livehouseExplanation == null || livehouseExplanation.isEmpty() ||
            livehouseDetailed == null || livehouseDetailed.isEmpty() ||
            equipmentInformation == null || equipmentInformation.isEmpty()) {
            request.setAttribute("errorMessage", "すべての必須項目を記入してください。");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
            return;
        }

        // モデルオブジェクトを作成 (user_idを追加)
        Livehouse_information livehouse = new Livehouse_information(
            0,  // id は0で、新規作成
            ownerName,
            equipmentInformation,
            livehouseExplanation,
            livehouseDetailed,
            livehouseName,
            "未入力",
            liveTelNumber,
            pictureImagePath,
            new Date(),
            new Date(),
            userId  // ここでuser_idを設定
        );

        // DAOで保存処理
        boolean isInserted = dao.insertLivehouseInformation(livehouse, userId);

        // 結果に応じた処理
        if (isInserted) {
            // 保存された内容を再取得
            Livehouse_information savedLivehouse = dao.getLivehouse_informationById(livehouse.getId());

            // 保存したデータをリクエストに設定
            request.setAttribute("livehouse", savedLivehouse);
            request.setAttribute("successMessage", "データが正常に保存されました。");
        } else {
            request.setAttribute("errorMessage", "データの保存に失敗しました。");
        }

        // homeにフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_home.jsp").forward(request, response);
    }

    private String saveImage(Part imagePart, String uploadDir, String imageType) throws IOException {
        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + imagePart.getSubmittedFileName().replaceAll("[^a-zA-Z0-9._-]", "_");
            File uploadDirFile = new File(uploadDir);

            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String imagePath = uploadDir + File.separator + fileName;

            try (InputStream inputStream = imagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(imagePath));
                return "/uploads/" + fileName;
            }
        }
        return null;
    }
}