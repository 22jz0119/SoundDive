package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Member;

public class Member_tableDAO {
    private static Member_tableDAO instance; // シングルトンインスタンス
    private final DBManager dbManager;

    // プライベートコンストラクタで外部からのインスタンス化を防止
    private Member_tableDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // シングルトンインスタンスを取得するメソッド
    public static synchronized Member_tableDAO getInstance(DBManager dbManager) {
        if (instance == null) {
            instance = new Member_tableDAO(dbManager);
        }
        return instance;
    }

    // メンバーを一括挿入するメソッド
    public boolean insertMembers(int artistGroupId, List<Member> members) {
        String sql = "INSERT INTO member_table (artist_group_id, member_name, member_position) VALUES (?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (Member member : members) {
                pstmt.setInt(1, artistGroupId);
                pstmt.setString(2, member.getMember_name());
                pstmt.setString(3, member.getMember_position());
                pstmt.addBatch();
            }
            int[] rowsAffected = pstmt.executeBatch();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                int index = 0;
                while (generatedKeys.next() && index < members.size()) {
                    int generatedId = generatedKeys.getInt(1);
                    members.get(index).setId(generatedId);
                    index++;
                }
            }

            return rowsAffected.length == members.size();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // メンバーを更新するメソッド
    public boolean updateMember(Member member) {
        String sql = "UPDATE member_table SET member_name = ?, member_position = ? WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getMember_name());
            pstmt.setString(2, member.getMember_position());
            pstmt.setInt(3, member.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

 // メンバーを削除するメソッド
    public boolean deleteMemberById(int id) {
        String sql = "DELETE FROM member_table WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            // 追跡ログ
            System.out.println("[deleteMemberById] Attempting to delete member with ID: " + id);
            System.out.println("[deleteMemberById] Rows affected: " + rowsAffected);
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            // エラー追跡ログ
        	
            System.err.println("[deleteMemberById] Error occurred while trying to delete member with ID: " + id);
            e.printStackTrace();
            return false;
            
        }
        
    }
    


    // グループIDに関連するメンバーを取得するメソッド
    public List<Member> getMembersByArtistGroupId(int artistGroupId) {
        String sql = "SELECT id, member_name, member_position, artist_group_id FROM member_table WHERE artist_group_id = ?";
        List<Member> members = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, artistGroupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(rs2model(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // 既存メンバーを更新するメソッド
    public void updateExistingMembers(List<Member> newMembers, List<Member> existingMembers) {
        for (Member newMember : newMembers) {
            for (Member existingMember : existingMembers) {
                if (newMember.getId() > 0 && newMember.getId() == existingMember.getId()) {
                    if (!newMember.getMember_name().equals(existingMember.getMember_name()) || 
                        !newMember.getMember_position().equals(existingMember.getMember_position())) {
                        updateMember(newMember);
                    }
                }
            }
        }
    }

    // ResultSetからMemberオブジェクトを作成するヘルパーメソッド
    private Member rs2model(ResultSet rs) throws SQLException {
        return new Member(
            rs.getInt("id"),
            rs.getInt("artist_group_id"),
            rs.getString("member_name"),
            rs.getString("member_position")
        );
    }
}
