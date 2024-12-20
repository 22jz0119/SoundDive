package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_applicationDAO;
import model.LivehouseApplicationWithGroup;

@WebServlet("/Application_approval")
public class Application_approval extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBManager dbManager = DBManager.getInstance();
        Livehouse_applicationDAO livehouseApplicationDAO = new Livehouse_applicationDAO(dbManager);

        String applicationIdParam = request.getParameter("id");

        if (applicationIdParam != null) {
            try {
                int applicationId = Integer.parseInt(applicationIdParam);
                LivehouseApplicationWithGroup applicationDetails = livehouseApplicationDAO.getApplicationDetailsById(applicationId);

             // デバッグ用: メソッドが呼ばれているか確認
             System.out.println("applicationDetails: " + applicationDetails); // 取得されたデータを表示


                // デバッグ用: applicationDetailsがnullでないことを確認
                if (applicationDetails != null) {
                    System.out.println("applicationDetails: " + applicationDetails);
                    request.setAttribute("application", applicationDetails); // JSPに渡す
                } else {
                    // データが見つからなかった場合
                    request.setAttribute("error", "指定された申請データが見つかりません");
                }
            } catch (NumberFormatException e) {
                // 無効なID形式の場合
                request.setAttribute("error", "無効な申請ID形式です");
                e.printStackTrace(); // エラースタックトレースの表示
            } catch (Exception e) {
                // その他の例外をキャッチ
                request.setAttribute("error", "エラーが発生しました");
                e.printStackTrace();
            }
        } else {
            // IDが指定されていない場合
            request.setAttribute("error", "申請IDが指定されていません");
        }
        

        // JSPにフォワード
        request.getRequestDispatcher("WEB-INF/jsp/livehouse/application_approval.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
