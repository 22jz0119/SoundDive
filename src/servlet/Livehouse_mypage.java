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
    private Livehouse_informationDAO dao;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = DBManager.getInstance();
        dao = new Livehouse_informationDAO(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("doGet: userId = " + userId);

        if (userId == null) {
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        try {
            Livehouse_information livehouse = dao.getLivehouse_informationByUserId(userId);

            if (livehouse != null) {
                request.setAttribute("livehouse", livehouse);
            } else {
                request.setAttribute("errorMessage", "ライブハウス情報が見つかりません。");
            }
        } catch (Exception e) {
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
        System.out.println("doPost: userId = " + userId);

        if (userId == null) {
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        // **リクエストパラメータ取得**
        String livehouseName = request.getParameter("livehouseName");
        String ownerName = request.getParameter("ownerName");
        String liveTelNumber = request.getParameter("liveTelNumber");
        String livehouseExplanation = request.getParameter("livehouseExplanation");
        String livehouseDetailed = request.getParameter("livehouseDetailed");
        String equipmentInformation = request.getParameter("equipmentInformation");
        String livehouseAddress = request.getParameter("livehouseAddress");

        // **画像アップロード処理**
        Part imagePart = request.getPart("picture_image_naigaikan");
        String imagePath = null;

        if (imagePart != null && imagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + imagePart.getSubmittedFileName();
            String uploadDir = getServletContext().getRealPath("/uploads/");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            imagePath = "/uploads/" + fileName;

            try (InputStream inputStream = imagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(uploadDir + File.separator + fileName));
            } catch (IOException e) {
                request.setAttribute("errorMessage", "画像アップロード中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
                return;
            }
        }

        try {
            Livehouse_information existingLivehouse = dao.getLivehouse_informationByUserId(userId);

            if (existingLivehouse != null) {
                existingLivehouse.setLivehouse_name(livehouseName);
                existingLivehouse.setOwner_name(ownerName);
                existingLivehouse.setLive_tel_number(liveTelNumber);
                existingLivehouse.setLivehouse_explanation_information(livehouseExplanation);
                existingLivehouse.setLivehouse_detailed_information(livehouseDetailed);
                existingLivehouse.setEquipment_information(equipmentInformation);
                existingLivehouse.setLive_address(livehouseAddress);
                existingLivehouse.setUpdateDate(new Date());

                if (imagePath != null) {
                    existingLivehouse.setPicture_image_naigaikan(imagePath);
                }

                boolean isUpdated = dao.updateLivehouse_information(existingLivehouse);
                if (isUpdated) {
                    response.sendRedirect(request.getContextPath() + "/Livehouse_mypage");
                    return;
                } else {
                    request.setAttribute("errorMessage", "データの更新に失敗しました。");
                }
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "データ保存中にエラーが発生しました。");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
    }
}
