package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Member;

public class Member_tableDAO {
    private DBManager dbManager;

    public Member_tableDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // メンバーを挿入するメソッド
    public boolean insertMember(Member member) {
        String sql = "INSERT INTO member_table (artist_group_id, member_name, member_position) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, member.getArtist_group_id());
            pstmt.setString(2, member.getMember_name());
            pstmt.setString(3, member.getMember_position()); // 修正済み

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Inserted Member: " + member.getMember_name() + ", Rows Affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // IDでメンバーを取得するメソッド
    public Member getMemberById(int id) {
        String sql = "SELECT artist_group_id, member_name, member_position FROM member_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int artistGroupId = rs.getInt("artist_group_id");
                String memberName = rs.getString("member_name");
                String memberPosition = rs.getString("member_position");

                Member member = new Member(id, artistGroupId, memberName, memberPosition);
                System.out.println("Retrieved Member: " + memberName + ", Artist Group ID: " + artistGroupId);
                return member;
            } else {
                System.out.println("No member found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 指定されたartist_group_idに関連するメンバーリストを取得
    public List<Member> getMembersByArtistGroupId(int artistGroupId) {
        String sql = "SELECT id, member_name, member_position FROM member_table WHERE artist_group_id = ?";
        List<Member> members = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistGroupId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String memberName = rs.getString("member_name");
                String memberPosition = rs.getString("member_position");

                Member member = new Member(id, artistGroupId, memberName, memberPosition);
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // 指定されたグループに関連するメンバーを取得するメソッド
    public List<Member> getMembersByGroupId(int groupId) {
        String sql = "SELECT id, artist_group_id, member_name, member_position FROM member_table WHERE artist_group_id = ?";
        List<Member> members = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, groupId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member(
                    rs.getInt("id"),
                    rs.getInt("artist_group_id"),
                    rs.getString("member_name"),
                    rs.getString("member_position") // 修正済み
                );
                members.add(member);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving members by group ID: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    // 指定されたIDのメンバーを削除
    public boolean deleteMemberById(int id) {
        String sql = "DELETE FROM member_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Deleted Member with ID: " + id + ", Rows Affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
