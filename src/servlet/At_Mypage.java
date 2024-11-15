package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/At_Mypage")
@MultipartConfig
public class At_Mypage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // JSPへ転送
        request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // フォームのデータを取得
        String accountName = request.getParameter("account_name");
        String member1Name = request.getParameter("member1_name");
        String member1Role = request.getParameter("member1_role");
        String bandYears = request.getParameter("band_years");

        // ファイルのアップロード処理 (アイコンとサンプル音源)
        Part profileImage = request.getPart("picture_image_movie");
        Part sampleMusic = request.getPart("sample_music");

        // バリデーションや保存処理を行い、結果をメッセージとしてセット
        String successMessage = "情報が正常に保存されました。";
        request.setAttribute("successMessage", successMessage);

        // エラーがあればerrorMessageを設定する例
        // String errorMessage = "エラーが発生しました。";
        // request.setAttribute("errorMessage", errorMessage);

        // JSPへ転送
        request.getRequestDispatcher("/at_mypage.jsp").forward(request, response);
    }
}
