package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/Livehouse_mypage")
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class Livehouse_mypage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // データベース接続情報
    private String dbURL = "jdbc:mysql://localhost:8080/your_database";
    private String dbUser = "your_username";
    private String dbPass = "your_password";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // フォームから送信されたデータを取得
        String livehouseName = request.getParameter("livehouseName");
        String ownerName = request.getParameter("ownerName");
        String mainInfo = request.getParameter("mainInfo");
        String detailInfo = request.getParameter("detailInfo");

        // ファイルパラメータの取得
        Part iconImagePart = request.getPart("iconImage");
        Part naikanImagePart = request.getPart("naikanImage");
        Part gaikanImagePart = request.getPart("gaikanImage");

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            // SQL文を準備
            String sql = "INSERT INTO livehouse_profile (livehouse_name, owner_name, main_info, detail_info, icon_image, naikan_image, gaikan_image) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, livehouseName);
            statement.setString(2, ownerName);
            statement.setString(3, mainInfo);
            statement.setString(4, detailInfo);

            // 画像ファイルをバイトストリームとして設定
            if (iconImagePart != null) {
                InputStream iconImageStream = iconImagePart.getInputStream();
                statement.setBlob(5, iconImageStream);
            }
            if (naikanImagePart != null) {
                InputStream naikanImageStream = naikanImagePart.getInputStream();
                statement.setBlob(6, naikanImageStream);
            }
            if (gaikanImagePart != null) {
                InputStream gaikanImageStream = gaikanImagePart.getInputStream();
                statement.setBlob(7, gaikanImageStream);
            }

            // SQLを実行
            int row = statement.executeUpdate();
            if (row > 0) {
                response.getWriter().println("ライブハウス情報が保存されました！");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.getWriter().println("エラーが発生しました: " + ex.getMessage());
        }
    }
}
