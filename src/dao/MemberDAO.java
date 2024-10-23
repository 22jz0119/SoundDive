package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Member;

public class MemberDAO {
    private DBManager dbManager;

    public MemberDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean insertMember(Member member) {
        String sql = "INSERT INTO user_table (id, artist_group_id, member_name, member_position) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

        	pstmt.setInt(1, member.getId());  // getId() が Integer であればこのままで良い
            pstmt.setInt(2, member.getArtist_group_id());
            pstmt.setString(3, member.getMember_name());
            pstmt.setString(4, member.getMember_potision());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Inserted Member: " + member.getMember_name() + ", Rows Affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Member getMemberById(int id) {
        String sql = "SELECT artist_group_id, member_name, member_position FROM member_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // id を int 型で設定
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
}
