package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DBManager;
import dao.Livehouse_informationDAO;
import model.Livehouse_information;

@WebServlet("/LivehouseInformation")
public class livehouse_information extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private Livehouse_informationDAO livehouseInformationDAO;

    public livehouse_information() {
        super();
        // DBManagerのインスタンスを作成し、DAOに渡す
        DBManager dbManager = new DBManager();
        livehouseInformationDAO = new Livehouse_informationDAO(dbManager);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // IDで検索するため、リクエストパラメータからIDを取得
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            int id = Integer.parseInt(idParam);

            // DAOを使用してIDでライブハウス情報を取得
            Livehouse_information livehouseInformation = livehouseInformationDAO.getLivehouse_informationById(id);

            // 結果をコンソールに表示（実際のアプリケーションではJSPやJSONでクライアントに返すなどする）
            livehouseInformationDAO.printLivehouse_informatinon(livehouseInformation);
        } else {
            response.getWriter().println("IDが指定されていません。");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // POSTリクエストからライブハウス情報を取得
        int id = Integer.parseInt(request.getParameter("id"));
        String oner_name = request.getParameter("oner_name");
        String equipment_information = request.getParameter("equipment_information");
        String livehouse_explanation_information = request.getParameter("livehouse_explanation_information");
        String livehouse_detailed_information = request.getParameter("livehouse_detailed_information");
        String livehouse_name = request.getParameter("livehouse_name");
        String live_address = request.getParameter("live_address");
        String live_tel_number = request.getParameter("live_tel_number");

        // 現在の日時を作成日と更新日として設定
        Date createDate = new Date();
        Date updateDate = new Date();

        // Livehouse_informationオブジェクトを作成
        Livehouse_information livehouseInformation = new Livehouse_information(id, oner_name, equipment_information, 
            livehouse_explanation_information, livehouse_detailed_information, livehouse_name, 
            live_address, live_tel_number, createDate, updateDate);

        // DAOを使用してデータベースに挿入
        boolean isInserted = livehouseInformationDAO.insertLivehouse_information(livehouseInformation);

        // 結果をコンソールに出力
        if (isInserted) {
            response.getWriter().println("ライブハウス情報が正常に挿入されました。");
        } else {
            response.getWriter().println("ライブハウス情報の挿入に失敗しました。");
        }
    }
}
