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
        System.out.println("[insertMembers] Start - artistGroupId=" + artistGroupId + ", members=" + members);

        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // トランザクションの開始
            connection.setAutoCommit(false);

            for (Member member : members) {
                pstmt.setInt(1, artistGroupId);
                pstmt.setString(2, member.getMember_name());
                pstmt.setString(3, member.getMember_position());
                System.out.println("[SQL] Executing: " + sql + " with artistGroupId=" + artistGroupId +
                                   ", member_name=" + member.getMember_name() +
                                   ", member_position=" + member.getMember_position());
                pstmt.addBatch();
            }

            // バッチ処理実行
            int[] rowsAffected = pstmt.executeBatch();
            System.out.println("[Result] Rows affected (insertMembers): " + rowsAffected.length);

            // 自動生成キーの取得
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                int index = 0;
                while (generatedKeys.next() && index < members.size()) {
                    int generatedId = generatedKeys.getInt(1);
                    members.get(index).setId(generatedId);
                    System.out.println("[GeneratedKey] Member ID: " + generatedId);
                    index++;
                }
            }

            // コミット
            connection.commit();
            System.out.println("[DEBUG] トランザクションが正常にコミットされました。");

            return rowsAffected.length == members.size();

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert members due to SQLException.");
            e.printStackTrace();

            // エラー時にロールバック
            try (Connection connection = dbManager.getConnection()) {
                if (connection != null) {
                    connection.rollback();
                    System.err.println("[DEBUG] ロールバックが正常に実行されました。");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("[ERROR] ロールバック失敗");
                rollbackEx.printStackTrace();
            }
            return false;

        } finally {
            System.out.println("[insertMembers] End");
        }
    }
    // メンバーを更新するメソッド
    public boolean updateMember(Member member) {
        String sql = "UPDATE member_table SET member_name = ?, member_position = ? WHERE id = ?";
        System.out.println("[updateMember] Start - member=" + member);
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getMember_name());
            pstmt.setString(2, member.getMember_position());
            pstmt.setInt(3, member.getId());
            System.out.println("[SQL] Executing: " + sql + " with member_name=" + member.getMember_name() +
                               ", member_position=" + member.getMember_position() + ", id=" + member.getId());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[Result] Rows affected (updateMember): " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[Error] Failed to update member with ID: " + member.getId());
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("[updateMember] End");
        }
    }

    // メンバーを削除するメソッド
    public boolean deleteMemberById(int id) {
        String sql = "DELETE FROM member_table WHERE id = ?";
        System.out.println("[deleteMemberById] Start - id=" + id);
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            System.out.println("[SQL] Executing: " + sql + " with id=" + id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[Result] Rows affected (deleteMemberById): " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[Error] Failed to delete member with ID: " + id);
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("[deleteMemberById] End");
        }
    }

    // グループIDに関連するメンバーを取得するメソッド
    public List<Member> getMembersByArtistGroupId(int artistGroupId) {
        String sql = "SELECT id, member_name, member_position, artist_group_id FROM member_table WHERE artist_group_id = ?";
        System.out.println("[getMembersByArtistGroupId] Start - artistGroupId=" + artistGroupId);
        List<Member> members = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, artistGroupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Member member = rs2model(rs);
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            System.err.println("[Error] Failed to fetch members for groupId=" + artistGroupId);
            e.printStackTrace();
        } finally {
            System.out.println("[getMembersByArtistGroupId] End");
        }
        return members;
    }

    // 既存メンバーを更新するメソッド
    public void updateExistingMembers(List<Member> newMembers, List<Member> existingMembers) {
        System.out.println("[updateExistingMembers] Start - newMembers=" + newMembers + ", existingMembers=" + existingMembers);
        for (Member newMember : newMembers) {
            for (Member existingMember : existingMembers) {
                if (newMember.getId() > 0 && newMember.getId() == existingMember.getId()) {
                    if (!newMember.getMember_name().equals(existingMember.getMember_name()) || 
                        !newMember.getMember_position().equals(existingMember.getMember_position())) {
                        System.out.println("[updateExistingMembers] Updating member ID: " + newMember.getId() +
                                           " from name: " + existingMember.getMember_name() + " to: " + newMember.getMember_name() +
                                           ", position: " + existingMember.getMember_position() + " to: " + newMember.getMember_position());
                        updateMember(newMember);
                    } else {
                        System.out.println("[updateExistingMembers] No changes detected for member ID: " + newMember.getId());
                    }
                }
            }
        }
        System.out.println("[updateExistingMembers] End");
    }
 // メンバー数をカウントするメソッド
    public int countMembersByGroupId(int groupId) {
        String sql = "SELECT COUNT(*) AS member_count FROM member_table WHERE artist_group_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("member_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // エラー時や該当メンバーがいない場合は 0 を返す
    }

    // ResultSetからMemberオブジェクトを作成するヘルパーメソッド
    private Member rs2model(ResultSet rs) throws SQLException {
        Member member = new Member(
            rs.getInt("id"),
            rs.getInt("artist_group_id"),
            rs.getString("member_name"),
            rs.getString("member_position")
        );
        return member;
    }
}
