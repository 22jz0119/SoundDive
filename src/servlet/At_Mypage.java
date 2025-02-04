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

        // 削除対象のメンバー
        String[] deletedMemberIds = request.getParameterValues("deleted_member_ids[]");
        List<Integer> deletedMemberIdList = new ArrayList<>();
        if (deletedMemberIds != null) {
            for (String id : deletedMemberIds) {
                try {
                    deletedMemberIdList.add(Integer.parseInt(id));
                } catch (NumberFormatException e) {
                    // 無効な ID はスキップ
                }
            }
        }

        // メンバー情報取得
        String[] memberIds = request.getParameterValues("member_id[]");
        String[] memberNames = request.getParameterValues("member_name[]");
        String[] memberRoles = request.getParameterValues("member_role[]");

        // NULLを回避するための処理
        int memberCount = (memberNames != null) ? memberNames.length : 0;
        if (memberRoles != null && memberRoles.length != memberCount) {
            memberCount = 0; // 一致しない場合、処理をスキップ
        }

        List<Member> newMembers = new ArrayList<>();
        for (int i = 0; i < memberCount; i++) {
            int memberId = 0;
            if (memberIds != null && i < memberIds.length) {
                try {
                    memberId = Integer.parseInt(memberIds[i]); // IDが数値ならパース
                } catch (NumberFormatException e) {
                    // 無効な ID は 0 (新規) として扱う
                }
            }

            newMembers.add(new Member(memberId, 0, memberNames[i], memberRoles[i]));
        }

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
                request.getRequestDispatcher("/WEB-INF/jsp/artist/at_mypage.jsp").forward(request, response);
                return;
            }
        }

        try (Connection conn = DBManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            Artist_group existingGroup = artistGroupDAO.getGroupByUserId(userId);

            if (existingGroup != null) {
                existingGroup.setAccount_name(accountName);
                if (pictureImagePath != null) {
                    existingGroup.setPicture_image_movie(pictureImagePath);
                }
                existingGroup.setGroup_genre(groupGenre);
                existingGroup.setBand_years(bandYears);
                existingGroup.setUpdate_date(LocalDate.now());
                artistGroupDAO.updateArtistGroupByUserId(userId, existingGroup);

                // **既存メンバーの ID を取得**
                List<Member> existingMembers = memberTableDAO.getMembersByArtistGroupId(existingGroup.getId());
                List<Integer> existingMemberIdList = new ArrayList<>();
                for (Member member : existingMembers) {
                    existingMemberIdList.add(member.getId());
                }

                // **メンバー管理**
                MemberService memberService = new MemberService(Member_tableDAO.getInstance(DBManager.getInstance()));
                memberService.manageMembers(conn, existingGroup.getId(), deletedMemberIdList, newMembers, existingMemberIdList);

                conn.commit();
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "サーバーでエラーが発生しました。もう一度お試しください。");
        }
        doGet(request, response);
    }
}
