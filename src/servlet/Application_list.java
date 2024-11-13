package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Artist_groupDAO;
import dao.DBManager;
import model.Artist_group;

@WebServlet("/Application_list")
public class Application_list extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 // DB接続マネージャーのインスタンス化
    	DBManager dbManager = DBManager.getInstance();
        Artist_groupDAO artistGroupDAO = new Artist_groupDAO(dbManager);

        // アーティストグループの一覧を取得する
        List<Artist_group> artistList = artistGroupDAO.getAllArtistGroups(); // getAllArtistGroups()メソッドは後述

        // アーティスト情報をリクエストスコープにセット
        request.setAttribute("artistList", artistList);

        // JSPにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/application_list.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
