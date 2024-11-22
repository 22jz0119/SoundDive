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
        String getMaxSql = "SELECT MAX(artist_group_id) FROM member_table WHERE artist_group_id = ?";
        String insertSql = "INSERT INTO member_table (artist_group_id, member_name, member_position) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnection()) {
            // 同じグループ内での最大順序番号を取得
            int nextArtistGroupId = 1;
            try (PreparedStatement getMaxStmt = conn.prepareStatement(getMaxSql)) {
                getMaxStmt.setInt(1, member.getArtist_group_id()); // グループのID
                ResultSet rs = getMaxStmt.executeQuery();
                if (rs.next()) {
                    nextArtistGroupId = rs.getInt(1) + 1; // 次の順序番号
                }
            }

            // 新しいメンバーを挿入
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, nextArtistGroupId); // 次の artist_group_id
                insertStmt.setString(2, member.getMember_name());
                insertStmt.setString(3, member.getMember_position());

                int rowsAffected = insertStmt.executeUpdate();
                System.out.println("Inserted Member: " + member.getMember_name() + ", Rows Affected: " + rowsAffected);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // 指定されたグループIDに関連するメンバーリストを取得
    public List<Member> getMembersByArtistGroupId(int artistGroupId) {
        String sql = "SELECT id, member_name, member_position FROM member_table WHERE artist_group_id = ?";
        List<Member> members = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistGroupId);
            var rs = pstmt.executeQuery();

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

    // IDでメンバーを取得するメソッド
    public Member getMemberById(int id) {
        String sql = "SELECT artist_group_id, member_name, member_position FROM member_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                int artistGroupId = rs.getInt("artist_group_id");
                String memberName = rs.getString("member_name");
                String memberPosition = rs.getString("member_position");

                return new Member(id, artistGroupId, memberName, memberPosition);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
