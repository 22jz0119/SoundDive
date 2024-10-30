package servlet.livehouse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Live_artistDAO;
import dao.Livehouse_applicationDAO;
import model.Live_artist;
import model.Livehouse_application;

@WebServlet("/Application_approval")
public class Application_approval extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private Livehouse_applicationDAO applicationDAO;
    private Live_artistDAO artistDAO;

    @Override
    public void init() {
        DBManager dbManager = DBManager.getInstance();
        applicationDAO = new Livehouse_applicationDAO(dbManager);
        artistDAO = new Live_artistDAO(dbManager);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationIdParam = request.getParameter("applicationId");

        // applicationIdがnullまたは空でないか確認
        if (applicationIdParam == null || applicationIdParam.isEmpty()) {
            response.getWriter().println("エラー: 予約IDが指定されていません。");
            return;
        }

        try {
            int applicationId = Integer.parseInt(applicationIdParam);

            // データベースから予約日時を取得
            Livehouse_application application = applicationDAO.getLivehouse_applicationById(applicationId);
            
            if (application != null) {
                Live_artist artist = artistDAO.getLive_artistById(application.getId());

                if (artist != null) {
                	request.setAttribute("reservationName", "田中 太郎");
                	request.setAttribute("reservationDateTime", "2024年10月10日 15時");
                	request.getRequestDispatcher("/WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
                	System.out.println("JSPへのフォワードを開始します");

                } else {
                    response.getWriter().println("エラー: アーティスト情報が見つかりませんでした。");
                }
            } else {
                response.getWriter().println("エラー: ライブハウス申請情報が見つかりませんでした。");
            }
        } catch (NumberFormatException e) {
            response.getWriter().println("エラー: 予約IDが無効です。");
        }
    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

