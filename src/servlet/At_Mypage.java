package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

        // アーティストグループ情報を取得
        Artist_groupDAO artistGroupDAO = new Artist_groupDAO(DBManager.getInstance());
        Artist_group userGroup = artistGroupDAO.getGroupByUserId(userId);

        // メンバー情報を取得
        if (userGroup != null) {
            Member_tableDAO memberTableDAO = new Member_tableDAO(DBManager.getInstance());
            List<Member> members = memberTableDAO.getMembersByArtistGroupId(userGroup.getId());
            request.setAttribute("userGroup", userGroup);
            request.setAttribute("members", members);
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

        String accountName = request.getParameter("account_name");
        String groupGenre = request.getParameter("group_genre");
        String bandYearsParam = request.getParameter("band_years");
        String[] memberNames = request.getParameterValues("member_name[]");
        String[] memberRoles = request.getParameterValues("member_role[]");

        int bandYears = 0;
        if (bandYearsParam != null && !bandYearsParam.trim().isEmpty()) {
            try {
                bandYears = Integer.parseInt(bandYearsParam);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "バンド歴は数値で入力してください。");
                request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
                return;
            }
        }

        List<Member> members = new ArrayList<>();
        if (memberNames != null && memberRoles != null && memberNames.length == memberRoles.length) {
            for (int i = 0; i < memberNames.length; i++) {
                members.add(new Member(0, 0, memberNames[i], memberRoles[i]));
            }
        }

        Part profileImagePart = request.getPart("picture_image_movie");
        String pictureImagePath = null;

        if (profileImagePart != null && profileImagePart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + profileImagePart.getSubmittedFileName();
            String uploadDir = "/var/lib/tomcat/webapps/SoundDive/uploads";
            File uploadDirFile = new File(uploadDir);

            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            pictureImagePath = "uploads/" + fileName;

            try (InputStream inputStream = profileImagePart.getInputStream()) {
                java.nio.file.Files.copy(inputStream, java.nio.file.Paths.get(uploadDir + File.separator + fileName));
            }
        }

        Artist_groupDAO artistGroupDAO = new Artist_groupDAO(DBManager.getInstance());
        Member_tableDAO memberTableDAO = new Member_tableDAO(DBManager.getInstance());
        Artist_group existingGroup = artistGroupDAO.getGroupByUserId(userId);

        if (existingGroup != null) {
            existingGroup.setAccount_name(accountName);
            existingGroup.setPicture_image_movie(pictureImagePath);
            existingGroup.setGroup_genre(groupGenre);
            existingGroup.setBand_years(bandYears);
            existingGroup.setUpdate_date(LocalDate.now());

            boolean isUpdated = artistGroupDAO.updateGroup(existingGroup);

            if (isUpdated) {
                memberTableDAO.deleteMemberById(existingGroup.getId()); // 既存メンバーを削除
                for (Member member : members) {
                    member.setArtist_group_id(existingGroup.getId());
                    memberTableDAO.insertMember(member);
                }
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
            int groupId = artistGroupDAO.createAndReturnId(newGroup, members);

            if (groupId > 0) {
                request.setAttribute("successMessage", "プロフィールが正常に保存されました。");
            } else {
                request.setAttribute("errorMessage", "プロフィール保存中にエラーが発生しました。");
            }
        }

        Artist_group userGroup = artistGroupDAO.getGroupByUserId(userId);
        if (userGroup != null) {
            List<Member> updatedMembers = memberTableDAO.getMembersByArtistGroupId(userGroup.getId());
            request.setAttribute("userGroup", userGroup);
            request.setAttribute("members", updatedMembers);
        } else {
            request.setAttribute("errorMessage", "グループ情報が見つかりません。");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
    }
}
