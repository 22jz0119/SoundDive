package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import dao.Member_tableDAO;
import model.Artist_group;
import model.Member;
import service.MemberService;

@WebServlet("/At_Mypage")
@MultipartConfig
public class At_Mypage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Artist_groupDAO artistGroupDAO;
    private Member_tableDAO memberTableDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        DBManager dbManager = DBManager.getInstance();
        artistGroupDAO = Artist_groupDAO.getInstance(dbManager);
        memberTableDAO = Member_tableDAO.getInstance(dbManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "ログインが必要です。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        try {
            Artist_group userGroup = artistGroupDAO.getGroupByUserId(userId);

            if (userGroup != null) {
                List<Member> members = memberTableDAO.getMembersByArtistGroupId(userGroup.getId());
                request.setAttribute("userGroup", userGroup);
                request.setAttribute("members", members);
            } else {
                request.setAttribute("errorMessage", "グループ情報が見つかりません。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データ取得中にエラーが発生しました。");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/artist/at_mypage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            request.setAttribute("errorMessage", "セッションが無効です。再ログインしてください。");
            request.getRequestDispatcher("/WEB-INF/jsp/top/top.jsp").forward(request, response);
            return;
        }

        // 削除対象メンバーの処理
        String[] deletedMemberIds = request.getParameterValues("deleted_member_ids[]");
        List<Integer> deletedMemberIdList = new ArrayList<>();
        if (deletedMemberIds != null) {
            for (String id : deletedMemberIds) {
                try {
                    deletedMemberIdList.add(Integer.parseInt(id));
                } catch (NumberFormatException e) {
                    System.err.println("[ERROR] 無効な削除ID: " + id);
                }
            }
            System.out.println("[DEBUG] 削除予定メンバーID: " + deletedMemberIdList);
        }

        // 新規メンバーの処理
        String[] memberNames = request.getParameterValues("member_name[]");
        String[] memberRoles = request.getParameterValues("member_role[]");

        List<Member> newMembers = new ArrayList<>();
        if (memberNames != null && memberRoles != null && memberNames.length == memberRoles.length) {
            for (int i = 0; i < memberNames.length; i++) {
                newMembers.add(new Member(0, 0, memberNames[i], memberRoles[i]));
            }
            System.out.println("[DEBUG] 追加予定メンバー: " + newMembers);
        }

        // バンド情報の更新
        String accountName = request.getParameter("account_name");
        String groupGenre = request.getParameter("group_genre");
        String bandYearsParam = request.getParameter("band_years");

        int bandYears = 0;
        if (bandYearsParam != null && !bandYearsParam.trim().isEmpty()) {
            try {
                bandYears = Integer.parseInt(bandYearsParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "バンド歴は数値で入力してください。");
                request.getRequestDispatcher("/WEB-INF/jsp/artist/at_mypage.jsp").forward(request, response);
                return;
            }
        }

        System.out.println("[DEBUG] 更新するバンド名: " + accountName);
        System.out.println("[DEBUG] 更新するジャンル: " + groupGenre);
        System.out.println("[DEBUG] 更新するバンド歴: " + bandYears);

        // 画像処理
        Part profileImagePart = request.getPart("picture_image_movie");
        String pictureImagePath = null;

        if (profileImagePart != null && profileImagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + profileImagePart.getSubmittedFileName();
            String uploadDir = getServletContext().getRealPath("/uploads/");
            File uploadDirFile = new File(uploadDir);

            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            pictureImagePath = "/uploads/" + fileName;

            try (InputStream inputStream = profileImagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(uploadDir + File.separator + fileName));
                System.out.println("[DEBUG] アップロードされた画像のパス: " + pictureImagePath);
            } catch (IOException e) {
                request.setAttribute("errorMessage", "画像アップロード中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/artist/at_mypage.jsp").forward(request, response);
                return;
            }
        }

        // データベースへの接続とトランザクション処理
        try (Connection conn = DBManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            Artist_group existingGroup = artistGroupDAO.getGroupByUserId(userId);

            if (existingGroup != null) {
                // === 既存グループの更新処理 ===
                existingGroup.setAccount_name(accountName);
                if (pictureImagePath != null) {
                    existingGroup.setPicture_image_movie(pictureImagePath);
                }
                existingGroup.setGroup_genre(groupGenre);
                existingGroup.setBand_years(bandYears);
                existingGroup.setUpdate_date(LocalDate.now());

                boolean isUpdated = artistGroupDAO.updateArtistGroupByUserId(userId, existingGroup);
                if (!isUpdated) {
                    conn.rollback();
                    System.err.println("[ERROR] バンド情報の更新に失敗しました。");
                    request.setAttribute("errorMessage", "プロフィール更新中にエラーが発生しました。");
                    return;
                }

                System.out.println("[DEBUG] バンド情報が正常に更新されました: " + existingGroup);

                // メンバー情報の更新
                MemberService memberService = new MemberService(Member_tableDAO.getInstance(DBManager.getInstance()));
                memberService.manageMembers(conn, existingGroup.getId(), deletedMemberIdList, newMembers);

                conn.commit();
                System.out.println("[DEBUG] メンバー情報が正常に更新されました。");
                request.setAttribute("successMessage", "プロフィールが正常に更新されました。");

            } else {
                // === 新規グループの作成処理 ===
                System.out.println("[DEBUG] 新規グループ作成処理を開始します (userId: " + userId + ")");

                Artist_group newGroup = new Artist_group();
                newGroup.setUser_id(userId);
                newGroup.setAccount_name(accountName);
                newGroup.setGroup_genre(groupGenre);
                newGroup.setBand_years(bandYears);
                newGroup.setCreate_date(LocalDate.now());
                newGroup.setUpdate_date(LocalDate.now());
                newGroup.setPicture_image_movie(pictureImagePath != null ? pictureImagePath : "");

                int newGroupId = artistGroupDAO.createAndReturnId(newGroup, newMembers);

                if (newGroupId > 0) {
                    conn.commit();
                    System.out.println("[DEBUG] 新規グループが作成されました (groupId: " + newGroupId + ")");
                    request.setAttribute("successMessage", "新規グループが作成されました。");
                } else {
                    conn.rollback();
                    System.err.println("[ERROR] 新規グループ作成に失敗しました。");
                    request.setAttribute("errorMessage", "新規グループ作成中にエラーが発生しました。");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "サーバーでエラーが発生しました。もう一度お試しください。");
        }

        // 更新後にマイページを再表示
        doGet(request, response);
    }

}
