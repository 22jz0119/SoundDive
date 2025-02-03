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
        System.out.println("[DEBUG] Livehouse_mypage servlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        System.out.println("[DEBUG] GET request received. Session userId: " + userId);

        // セッションにuserIdがない場合の処理
        if (userId == null) {
            System.out.println("[ERROR] User is not logged in.");
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        try {
            // ユーザーに紐付くライブハウス情報を取得
            Livehouse_information livehouse = dao.getLivehouse_informationByUserId(userId);

            if (livehouse != null) {
                System.out.println("[DEBUG] Retrieved Livehouse information: " + livehouse);
                if (livehouse.getPicture_image_naigaikan() != null) {
                    System.out.println("[DEBUG] Image path: " + livehouse.getPicture_image_naigaikan());
                } else {
                    System.out.println("[DEBUG] No image set for livehouse.");
                }

                request.setAttribute("livehouse", livehouse);
            } else {
                System.out.println("[WARN] No Livehouse information found for userId: " + userId);
                request.setAttribute("errorMessage", "ライブハウス情報が見つかりません。");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception occurred while fetching Livehouse information:");
            e.printStackTrace();
            request.setAttribute("errorMessage", "データ取得中にエラーが発生しました。");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("[DEBUG] POST request received. Logged-in userId: " + userId);

        if (userId == null) {
            System.out.println("[ERROR] User is not logged in.");
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        String livehouseName = request.getParameter("livehouseName");
        String ownerName = request.getParameter("ownerName");
        String liveTelNumber = request.getParameter("liveTelNumber");
        String livehouseExplanation = request.getParameter("livehouseExplanation");
        String livehouseDetailed = request.getParameter("livehouseDetailed");
        String equipmentInformation = request.getParameter("equipmentInformation");

        System.out.println("[DEBUG] Input data:");
        System.out.println("  livehouseName: " + livehouseName);
        System.out.println("  ownerName: " + ownerName);
        System.out.println("  liveTelNumber: " + liveTelNumber);
        System.out.println("  livehouseExplanation: " + livehouseExplanation);
        System.out.println("  livehouseDetailed: " + livehouseDetailed);
        System.out.println("  equipmentInformation: " + equipmentInformation);

        Part imagePart = request.getPart("picture_image_naigaikan");
        String pictureImagePath = null;

        try {
            String uploadDir = getServletContext().getRealPath("/uploads/");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            pictureImagePath = saveImage(imagePart, uploadDir, "共通画像");
            System.out.println("[DEBUG] Image saved at: " + pictureImagePath);
        } catch (IOException e) {
            System.err.println("[ERROR] Exception occurred while uploading image:");
            e.printStackTrace();
            request.setAttribute("errorMessage", "画像アップロード中にエラーが発生しました。");
            request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
            return;
        }

        try {
            Livehouse_information existingLivehouse = dao.getLivehouse_informationById(userId);

            if (existingLivehouse != null) {
                existingLivehouse.setOwner_name(ownerName);
                existingLivehouse.setEquipment_information(equipmentInformation);
                existingLivehouse.setLivehouse_explanation_information(livehouseExplanation);
                existingLivehouse.setLivehouse_detailed_information(livehouseDetailed);
                existingLivehouse.setLivehouse_name(livehouseName);
                existingLivehouse.setLive_tel_number(liveTelNumber);
                existingLivehouse.setUpdateDate(new Date());

                if (pictureImagePath != null) {
                    existingLivehouse.setPicture_image_naigaikan(pictureImagePath);
                }

                System.out.println("[DEBUG] Updating existing Livehouse information: " + existingLivehouse);
                boolean isUpdated = dao.updateLivehouse_information(existingLivehouse);

                if (isUpdated) {
                    System.out.println("[DEBUG] Livehouse information updated successfully.");
                    response.sendRedirect(request.getContextPath() + "/Livehouse_mypage");
                    return;
                } else {
                    System.err.println("[ERROR] Failed to update Livehouse information.");
                    request.setAttribute("errorMessage", "データの更新に失敗しました。");
                }
            } else {
                Livehouse_information livehouse = new Livehouse_information(
                    0,
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

                System.out.println("[DEBUG] Creating new Livehouse information: " + livehouse);
                boolean isInserted = dao.insertLivehouseInformation(livehouse, userId);

                if (isInserted) {
                    System.out.println("[DEBUG] Livehouse information created successfully.");
                    response.sendRedirect(request.getContextPath() + "/Livehouse_mypage");
                    return;
                } else {
                    System.err.println("[ERROR] Failed to create Livehouse information.");
                    request.setAttribute("errorMessage", "データの保存に失敗しました。");
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception occurred while saving Livehouse information:");
            e.printStackTrace();
            request.setAttribute("errorMessage", "データ保存中にエラーが発生しました。");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
    }

    private String saveImage(Part imagePart, String uploadDir, String imageType) throws IOException {
        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + imageType + "_" +
                              imagePart.getSubmittedFileName().replaceAll("[^a-zA-Z0-9._-]", "_");

            String imagePath = uploadDir + File.separator + fileName;

            try (InputStream inputStream = imagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(imagePath));
            }

            return "/uploads/" + fileName;
        }
        return null;
    }
}
