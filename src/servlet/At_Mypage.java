package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.Artist_groupDAO;
import dao.DBManager;
import model.Artist_group;

@WebServlet("/At_Mypage")
@MultipartConfig
public class At_Mypage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top.jsp").forward(request, response);
            return;
        }

        // ユーザーに関連するグループ情報を取得
        Artist_groupDAO artistGroupDAO = new Artist_groupDAO(DBManager.getInstance());
        Artist_group userGroup = artistGroupDAO.getGroupByUserId(userId);

        // グループが存在する場合、リクエストスコープに情報をセット
        if (userGroup != null) {
            request.setAttribute("userGroup", userGroup);
        } else {
            request.setAttribute("errorMessage", "グループ情報が見つかりません。");
        }

        // JSPへ転送
        request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "セッションが無効です。再ログインしてください。");
            request.getRequestDispatcher("/WEB-INF/jsp/top.jsp").forward(request, response);
            return;
        }

        // リクエストパラメータの確認
        String accountName = request.getParameter("account_name");
        String groupGenre = request.getParameter("group_genre");
        String bandYearsParam = request.getParameter("band_years");

        // ログ出力
        System.out.println("Received account_name: " + accountName);
        System.out.println("Received group_genre: " + groupGenre);
        System.out.println("Received band_years: " + bandYearsParam);

        // バンド歴の処理
        int bandYears = 0;
        if (bandYearsParam != null && !bandYearsParam.trim().isEmpty()) {
            try {
                bandYears = Integer.parseInt(bandYearsParam);
                System.out.println("Parsed band_years: " + bandYears);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "バンド歴は数値で入力してください。");
                request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
                return;
            }
        }

        // 画像のアップロード処理
        Part profileImagePart = request.getPart("picture_image_movie");
        String pictureImagePath = null;

        if (profileImagePart != null && profileImagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + profileImagePart.getSubmittedFileName();
            
            // 保存先のディレクトリを確認
            String uploadDir = "/var/lib/tomcat/webapps/uploads"; // 明示的なパスに変更
            File uploadDirFile = new File(uploadDir);

            // ディレクトリが存在するか確認し、なければ作成
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                if (!created) {
                    request.setAttribute("errorMessage", "画像の保存ディレクトリを作成できませんでした。");
                    request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
                    return;
                }
            }

            pictureImagePath = "uploads/" + fileName;

            // ファイルの保存処理
            try (InputStream inputStream = profileImagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(uploadDir + java.io.File.separator + fileName));
                System.out.println("Image saved at: " + uploadDir + java.io.File.separator + fileName); // 画像の保存先をログ出力
            } catch (IOException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "画像の保存中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
                return;
            }
        }

        // アーティストグループの更新または新規作成
        Artist_groupDAO artistGroupDAO = new Artist_groupDAO(DBManager.getInstance());
        Artist_group existingGroup = artistGroupDAO.getGroupByUserId(userId);

        if (existingGroup != null) {
            existingGroup.setAccount_name(accountName);
            existingGroup.setPicture_image_movie(pictureImagePath);
            existingGroup.setGroup_genre(groupGenre);
            existingGroup.setBand_years(bandYears);
            existingGroup.setUpdate_date(LocalDate.now());

            boolean isUpdated = artistGroupDAO.updateGroup(existingGroup);
            if (isUpdated) {
                request.setAttribute("successMessage", "プロフィールが正常に更新されました。");
            } else {
                request.setAttribute("errorMessage", "プロフィール更新中にエラーが発生しました。");
            }
        } else {
            Artist_group newGroup = new Artist_group(
                0,
                userId,
                accountName,
                pictureImagePath,
                groupGenre,
                bandYears,
                LocalDate.now(),
                LocalDate.now(),
                "0.0"
            );
            int groupId = artistGroupDAO.createAndReturnId(newGroup);

            if (groupId > 0) {
                request.setAttribute("successMessage", "プロフィールが正常に保存されました。");
            } else {
                request.setAttribute("errorMessage", "プロフィール保存中にエラーが発生しました。");
            }
        }

        // ユーザーの最新のグループ情報を再度取得して表示
        Artist_group userGroup = artistGroupDAO.getGroupByUserId(userId);
        if (userGroup != null) {
            request.setAttribute("userGroup", userGroup);
        } else {
            request.setAttribute("errorMessage", "グループ情報が見つかりません。");
        }

        // JSPへ転送
        request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
    }
}
