package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Artist_groupDAO;
import dao.DBManager;
import model.Artist_group;

@WebServlet("/CheckMypageStatus")
public class CheckMypageStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        DBManager dbManager = DBManager.getInstance();
        Artist_groupDAO artistGroupDAO = Artist_groupDAO.getInstance(dbManager);
        
        boolean isComplete = false;

        if (userId != null) {
            Artist_group artistGroup = artistGroupDAO.getGroupByUserId(userId);
            if (artistGroup != null && artistGroup.getAccount_name() != null && !artistGroup.getAccount_name().isEmpty()) {
                isComplete = true;
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.print("{\"status\": \"" + (isComplete ? "ok" : "error") + "\"}");
        }
    }
}
