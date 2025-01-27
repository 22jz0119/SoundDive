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

        // セッションにuserIdがない場合の処理
        if (userId == null) {
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        try {
            // ユーザーに紐付くライブハウス情報を取得
            Livehouse_information livehouse = dao.getLivehouse_informationById(userId);

            if (livehouse != null) {
                // プロフィール画像、内観画像、外観画像として共通のフィールドを使用
                if (livehouse.getPicture_image_naigaikan() != null) {
                    System.out.println("画像パス: " + livehouse.getPicture_image_naigaikan());
                } else {
                    System.out.println("画像は設定されていません。");
                }

                // JSP に渡すためにリクエストスコープに設定
                request.setAttribute("livehouse", livehouse);
            } else {
                System.out.println("ライブハウス情報が見つかりません。");
                request.setAttribute("errorMessage", "ライブハウス情報が見つかりません。");
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
        System.out.println("[DEBUG] Logged-in userId: " + userId);

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
        
        System.out.println("[DEBUG] Input data:");
        System.out.println("livehouseName: " + request.getParameter("livehouseName"));
        System.out.println("ownerName: " + request.getParameter("ownerName"));
        System.out.println("liveTelNumber: " + request.getParameter("liveTelNumber"));
        System.out.println("livehouseExplanation: " + request.getParameter("livehouseExplanation"));
        System.out.println("livehouseDetailed: " + request.getParameter("livehouseDetailed"));
        System.out.println("equipmentInformation: " + request.getParameter("equipmentInformation"));

        // 画像アップロード処理 (共通フィールドを使用)
        Part imagePart = request.getPart("picture_image_naigaikan");
        String pictureImagePath = null;

        try {
            // アップロードディレクトリの取得
            String uploadDir = getServletContext().getRealPath("/uploads/");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // 画像の保存
            pictureImagePath = saveImage(imagePart, uploadDir, "共通画像");
            // ログ: 画像の保存場所を確認
            System.out.println("Image saved at: " + pictureImagePath);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "画像アップロード中にエラーが発生しました。");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
            return;
        }

        try {
            // 既存データの確認
            Livehouse_information existingLivehouse = dao.getLivehouse_informationById(userId);

            if (existingLivehouse != null) {
                // 既存データがある場合は更新
                existingLivehouse.setOwner_name(ownerName);
                existingLivehouse.setEquipment_information(equipmentInformation);
                existingLivehouse.setLivehouse_explanation_information(livehouseExplanation);
                existingLivehouse.setLivehouse_detailed_information(livehouseDetailed);
                existingLivehouse.setLivehouse_name(livehouseName);
                existingLivehouse.setLive_tel_number(liveTelNumber);
                existingLivehouse.setUpdateDate(new Date());
                System.out.println("[DEBUG] No existing data found for userId: " + userId);

                // 新しい画像がアップロードされた場合のみ更新
                if (pictureImagePath != null) {
                    existingLivehouse.setPicture_image_naigaikan(pictureImagePath);
                }

                // ログ: 更新対象のデータを確認
                System.out.println("Attempting to update Livehouse Information:");
                System.out.println(existingLivehouse);

                // データベースの更新処理
                boolean isUpdated = dao.updateLivehouse_information(existingLivehouse);
                
                // ログ: 更新結果を確認
                System.out.println("Update Status: " + isUpdated);
                System.out.println("Updated Data: " + existingLivehouse);

                if (isUpdated) {
                    // 更新後、画像パスをリクエストに設定してJSPに渡す
                    request.setAttribute("livehouse", existingLivehouse);
                    response.sendRedirect(request.getContextPath() + "/Livehouse_mypage");
                    return;
                } else {
                    request.setAttribute("errorMessage", "データの更新に失敗しました。");
                }
            } else {
                // データが存在しない場合は新規作成
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
                    userId
                );

                boolean isInserted = dao.insertLivehouseInformation(livehouse, userId);

                // ログ: 新規作成結果を確認
                System.out.println("Insert Status: " + isInserted);
                System.out.println("Inserted Data: " + livehouse);

                if (isInserted) {
                    // 新規作成後、画像パスをリクエストに設定してJSPに渡す
                    request.setAttribute("livehouse", livehouse);
                    response.sendRedirect(request.getContextPath() + "/Livehouse_mypage");
                    return;
                } else {
                    request.setAttribute("errorMessage", "データの保存に失敗しました。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データ保存中にエラーが発生しました。");
        }

        // エラー時にマイページを再表示
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
    }

    private String saveImage(Part imagePart, String uploadDir, String imageType) throws IOException {
        if (imagePart != null && imagePart.getSize() > 0) {
            // ファイル名を生成（タイムスタンプ + 画像タイプ + オリジナルファイル名）
            String fileName = System.currentTimeMillis() + "_" + imageType + "_" +
                              imagePart.getSubmittedFileName().replaceAll("[^a-zA-Z0-9._-]", "_");

            // 保存先ディレクトリの確認と作成
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // 保存パスを構築
            String imagePath = uploadDir + File.separator + fileName;

            // ファイルを保存
            try (InputStream inputStream = imagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(imagePath));
            }

            // HTTPでアクセス可能な相対パスを返す
            return "/uploads/" + fileName;
        }
        return null; // ファイルがアップロードされていない場合
    }

}
