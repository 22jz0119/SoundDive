package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.Artist_group;

@WebServlet("/Application_approval")
public class Application_approval extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DBManagerインスタンスを取得してArtist_groupDAOを作成
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO artistGroupDAO = new Livehouse_applicationDAO(dbManager);

        // サンプルとして、IDが1のアーティストグループ情報を取得
        int artistGroupId = 1;  // 取得したいArtist_groupのIDを指定
        Artist_group artistGroup = artistGroupDAO.getArtist_groupById(artistGroupId);

        // アーティストグループ情報が見つかればaccount_nameをJSPに渡し、見つからなければエラーメッセージを渡す
        if (artistGroup != null) {
            String accountName = artistGroup.getAccount_name(); // account_name属性を取得
            request.setAttribute("accountName", accountName); // JSPに渡す
        } else {
            request.setAttribute("error", "アーティストグループ情報が見つかりません");
        }

        // JSPページにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/application_approval.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
