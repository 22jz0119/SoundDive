package servlet;

import java.io.IOException;
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
        System.out.println("[DEBUG] Livehouse_mypage servlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        System.out.println("[DEBUG] GET request received. Session userId: " + userId);

        if (userId == null) {
            System.out.println("[ERROR] User is not logged in.");
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        try {
            Livehouse_information livehouse = dao.getLivehouse_informationByUserId(userId);

            if (livehouse != null) {
                System.out.println("[DEBUG] Retrieved Livehouse information: " + livehouse);
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

        try {
            Livehouse_information existingLivehouse = dao.getLivehouse_informationByUserId(userId);
            if (existingLivehouse != null) {
                System.out.println("[DEBUG] Existing Livehouse data before update:");
                System.out.println("  livehouse_name: " + existingLivehouse.getLivehouse_name());
                System.out.println("  owner_name: " + existingLivehouse.getOwner_name());
                System.out.println("  live_tel_number: " + existingLivehouse.getLive_tel_number());
                System.out.println("  livehouse_explanation_information: " + existingLivehouse.getLivehouse_explanation_information());
                System.out.println("  livehouse_detailed_information: " + existingLivehouse.getLivehouse_detailed_information());
                System.out.println("  equipment_information: " + existingLivehouse.getEquipment_information());

                existingLivehouse.setLivehouse_name(livehouseName);
                existingLivehouse.setOwner_name(ownerName);
                existingLivehouse.setLive_tel_number(liveTelNumber);
                existingLivehouse.setLivehouse_explanation_information(livehouseExplanation);
                existingLivehouse.setLivehouse_detailed_information(livehouseDetailed);
                existingLivehouse.setEquipment_information(equipmentInformation);
                existingLivehouse.setUpdateDate(new Date());
                

                System.out.println("[DEBUG] Updating Livehouse with new values:");
                System.out.println("  livehouse_name: " + existingLivehouse.getLivehouse_name());
                System.out.println("  owner_name: " + existingLivehouse.getOwner_name());
                System.out.println("  live_tel_number: " + existingLivehouse.getLive_tel_number());
                System.out.println("  livehouse_explanation_information: " + existingLivehouse.getLivehouse_explanation_information());
                System.out.println("  livehouse_detailed_information: " + existingLivehouse.getLivehouse_detailed_information());
                System.out.println("  equipment_information: " + existingLivehouse.getEquipment_information());

                boolean isUpdated = dao.updateLivehouse_information(existingLivehouse);
                if (isUpdated) {
                    System.out.println("[DEBUG] Livehouse information updated successfully.");
                    response.sendRedirect(request.getContextPath() + "/Livehouse_mypage");
                    return;
                } else {
                    System.err.println("[ERROR] Failed to update Livehouse information.");
                    request.setAttribute("errorMessage", "データの更新に失敗しました。");
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception occurred while updating Livehouse information:");
            e.printStackTrace();
            request.setAttribute("errorMessage", "データ保存中にエラーが発生しました。");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/livehouse/livehouse_mypage.jsp").forward(request, response);
    }

}
