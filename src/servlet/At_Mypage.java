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

        request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
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
                    System.out.println("[Error] 無効な削除ID: " + id);
                }
            }
        }

        // 新規メンバーの処理
        String[] memberNames = request.getParameterValues("member_name[]");
        String[] memberRoles = request.getParameterValues("member_role[]");

        List<Member> newMembers = new ArrayList<>();
        if (memberNames != null && memberRoles != null && memberNames.length == memberRoles.length) {
            for (int i = 0; i < memberNames.length; i++) {
                newMembers.add(new Member(0, 0, memberNames[i], memberRoles[i]));
            }
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
                request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
                return;
            }
        }

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
            } catch (IOException e) {
                request.setAttribute("errorMessage", "画像アップロード中にエラーが発生しました。");
                request.getRequestDispatcher("/WEB-INF/jsp/at_mypage.jsp").forward(request, response);
                return;
            }
        }

        try (Connection conn = DBManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            Artist_group existingGroup = artistGroupDAO.getGroupByUserId(userId);

            if (existingGroup != null) {
                existingGroup.setAccount_name(accountName);
                existingGroup.setPicture_image_movie(pictureImagePath);
                existingGroup.setGroup_genre(groupGenre);
                existingGroup.setBand_years(bandYears);
                existingGroup.setUpdate_date(LocalDate.now());

                boolean isUpdated = artistGroupDAO.updateArtistGroupByUserId(userId, existingGroup);

                if (isUpdated) {
                    // 既存メンバーを取得
                    List<Member> existingMembers = memberTableDAO.getMembersByArtistGroupId(existingGroup.getId());

                    // **1. 削除処理**
                    for (int memberId : deletedMemberIdList) {
                        memberTableDAO.deleteMemberById(memberId);
                    }

                    // 削除済みメンバーを除外したリストを作成
                    List<Member> filteredNewMembers = newMembers.stream()
                            .filter(newMember -> existingMembers.stream()
                                    .noneMatch(existingMember -> deletedMemberIdList.contains(existingMember.getId())))
                            .toList();

                    // **2. 更新処理**
                    memberTableDAO.updateExistingMembers(filteredNewMembers, existingMembers);

                    // **3. 挿入処理**
                    List<Member> membersToInsert = filteredNewMembers.stream()
                            .filter(member -> member.getId() == 0) // 新規挿入するメンバーのみ
                            .toList();

                    if (!membersToInsert.isEmpty()) {
                        memberTableDAO.insertMembers(existingGroup.getId(), membersToInsert);
                    }

                    conn.commit();
                    request.setAttribute("successMessage", "プロフィールが正常に更新されました。");
                } else {
                    conn.rollback();
                    request.setAttribute("errorMessage", "プロフィール更新中にエラーが発生しました。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "サーバーでエラーが発生しました。もう一度お試しください。");
        }

        doGet(request, response);
    }
}
