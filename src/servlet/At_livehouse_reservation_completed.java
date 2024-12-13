package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/At_livehouse_reservation_completed")
public class At_livehouse_reservation_completed extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // リクエストパラメータからライブハウスIDを取得
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ライブハウスIDが指定されていません。");
                return;
            }

            int livehouseId = Integer.parseInt(idParam);
            DBManager dbManager = DBManager.getInstance();
            Livehouse_informationDAO livehouseInfoDao = new Livehouse_informationDAO(dbManager);

            // IDでライブハウス情報を取得
            Livehouse_information livehouseInfo = livehouseInfoDao.getLivehouse_informationById(livehouseId);
            if (livehouseInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定されたライブハウスが見つかりません。");
                return;
            }

            // JSPに渡す
            request.setAttribute("livehouse", livehouseInfo);
            request.getRequestDispatcher("/WEB-INF/jsp/at-livehouse-reservation-completed.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "エラーが発生しました。");
        }
    }
}
