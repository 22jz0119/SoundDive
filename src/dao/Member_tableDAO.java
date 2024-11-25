package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Member;

public class Member_tableDAO {
    private final DBManager dbManager;

    public Member_tableDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // メンバーを一括挿入するメソッド（単体挿入にも対応）
    public boolean insertMembers(int artistGroupId, List<Member> members) {
        String sql = "INSERT INTO member_table (artist_group_id, member_name, member_position) VALUES (?, ?, ?)";
        System.out.println("[insertMembers] Start inserting members for ArtistGroupID: " + artistGroupId);
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (Member member : members) {
                System.out.println("[insertMembers] Adding to batch: MemberName: " + member.getMember_name() + ", MemberPosition: " + member.getMember_position());
                pstmt.setInt(1, artistGroupId);
                pstmt.setString(2, member.getMember_name());
                pstmt.setString(3, member.getMember_position());
                pstmt.addBatch();
            }
            int[] rowsAffected = pstmt.executeBatch();
            System.out.println("[insertMembers] Inserted " + rowsAffected.length + " members.");

            // 取得したIDを反映
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                int index = 0;
                while (generatedKeys.next() && index < members.size()) {
                    int generatedId = generatedKeys.getInt(1);
                    members.get(index).setId(generatedId);
                    System.out.println("[insertMembers] Generated ID: " + generatedId + " for Member: " + members.get(index).getMember_name());
                    index++;
                }
            }

            return rowsAffected.length == members.size();
        } catch (SQLException e) {
            System.err.println("[insertMembers] Error while inserting members: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // メンバーを更新するメソッド
    public boolean updateMember(Member member) {
        String sql = "UPDATE member_table SET member_name = ?, member_position = ? WHERE id = ?";
        System.out.println("[updateMember] Updating member: ID: " + member.getId() + ", Name: " + member.getMember_name());
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getMember_name());
            pstmt.setString(2, member.getMember_position());
            pstmt.setInt(3, member.getId());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[updateMember] Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[updateMember] Error while updating member: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 指定されたIDのメンバーを削除
    public boolean deleteMemberById(int id) {
        String sql = "DELETE FROM member_table WHERE id = ?";
        System.out.println("[deleteMemberById] Deleting member with ID: " + id);
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[deleteMemberById] Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[deleteMemberById] Error while deleting member: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 指定されたグループIDに関連するメンバーリストを取得
    public List<Member> getMembersByArtistGroupId(int artistGroupId) {
        String sql = "SELECT id, member_name, member_position, artist_group_id FROM member_table WHERE artist_group_id = ?";
        System.out.println("[getMembersByArtistGroupId] Retrieving members for ArtistGroupID: " + artistGroupId);
        List<Member> members = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, artistGroupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Member member = rs2model(rs); // rs2model で正しいマッピングが可能に
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            System.err.println("[getMembersByArtistGroupId] Error while retrieving members: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }

    public void deleteMembersNotInNewList(int artistGroupId, List<Member> newMembers, List<Member> existingMembers) {
        System.out.println("[deleteMembersNotInNewList] Start deleting members not in the new list for ArtistGroupID: " + artistGroupId);

        // 新しいリストに含まれるメンバーのIDを収集
        List<Integer> newMemberIds = newMembers.stream()
                                               .map(Member::getId)
                                               .filter(id -> id > 0) // IDが0以外
                                               .toList();
        System.out.println("[deleteMembersNotInNewList] New Member IDs: " + newMemberIds);

        // 既存メンバーの中で新しいリストに含まれていないものを削除
        for (Member existingMember : existingMembers) {
            System.out.println("[deleteMembersNotInNewList] Checking existing member ID: " + existingMember.getId());
            if (!newMemberIds.contains(existingMember.getId())) {
                System.out.println("[deleteMembersNotInNewList] Deleting member with ID: " + existingMember.getId());
                deleteMemberById(existingMember.getId());
            } else {
                System.out.println("[deleteMembersNotInNewList] Keeping member with ID: " + existingMember.getId());
            }
        }
    }




    // 新しいリストのメンバーを更新
    public void updateExistingMembers(List<Member> newMembers, List<Member> existingMembers) {
        System.out.println("[updateExistingMembers] Start updating existing members.");
        for (Member newMember : newMembers) {
            for (Member existingMember : existingMembers) {
                if (newMember.getId() > 0 && newMember.getId() == existingMember.getId()) {
                    System.out.println("[updateExistingMembers] Updating member: ID: " + newMember.getId());
                    updateMember(newMember);
                }
            }
        }
    }

    // 新しいリストで ID が 0 のメンバーを挿入
    public void insertNewMembers(int artistGroupId, List<Member> newMembers) {
        System.out.println("[insertNewMembers] Start inserting new members for ArtistGroupID: " + artistGroupId);
        List<Member> membersToInsert = newMembers.stream()
                                                 .filter(member -> member.getId() == 0) // IDが0の新規メンバー
                                                 .toList();
        for (Member member : membersToInsert) {
            System.out.println("[insertNewMembers] Inserting new member: Name: " + member.getMember_name() + ", Position: " + member.getMember_position());
        }
        if (!membersToInsert.isEmpty()) {
            insertMembers(artistGroupId, membersToInsert);
        }
    }

    // ResultSetからMemberオブジェクトを作成するメソッド
    private Member rs2model(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String memberName = rs.getString("member_name");
        String memberPosition = rs.getString("member_position");
        int artistGroupId = rs.getInt("artist_group_id");
        System.out.println("[rs2model] Mapping ResultSet to Member object: ID: " + id + ", Name: " + memberName + ", Position: " + memberPosition);
        return new Member(id, artistGroupId, memberName, memberPosition);
    }
}
