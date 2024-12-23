package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Notice;

public class NoticeDAO {
    private static NoticeDAO instance;
    private DBManager dbManager;

    private NoticeDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public static synchronized NoticeDAO getInstance(DBManager dbManager) {
        if (instance == null) {
            instance = new NoticeDAO(dbManager);
        }
        return instance;
    }

    // 通知を挿入するメソッド
    public boolean addNotice(Notice notice) {
        String sql = "INSERT INTO notice_table (livehouse_application_id, create_date, update_date, message, is_approved) VALUES (?, NOW(), NOW(), ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, notice.getLivehouse_application_id());
            pstmt.setString(2, notice.getMessage());
            pstmt.setBoolean(3, notice.isApproved());

            // デバッグ用ログ
            System.out.println("[DEBUG] Executing SQL: " + sql);
            System.out.println("[DEBUG] Params: livehouse_application_id=" + notice.getLivehouse_application_id() +
                               ", message=" + notice.getMessage() +
                               ", is_approved=" + notice.isApproved());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[DEBUG] Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL Exception in addNotice");
            e.printStackTrace();
            return false;
        }
    }

    // 通知を更新するメソッド
    public boolean updateNoticeWithApplicationId(int noticeId, int livehouseApplicationId) {
        String sql = "UPDATE notice_table SET livehouse_application_id = ?, is_approved = true WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseApplicationId);
            pstmt.setInt(2, noticeId);

            // デバッグ用ログ
            System.out.println("[DEBUG] Executing SQL: " + sql);
            System.out.println("[DEBUG] Params: livehouse_application_id=" + livehouseApplicationId + ", noticeId=" + noticeId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("[DEBUG] Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update notice.");
            e.printStackTrace();
            return false;
        }
    }

    // 通知を取得するメソッド (IDベース)
    public Notice getNoticeById(int noticeId) {
        String sql = "SELECT * FROM notice_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, noticeId);

            // デバッグ用ログ
            System.out.println("[DEBUG] Executing SQL: " + sql);
            System.out.println("[DEBUG] Params: noticeId=" + noticeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Notice notice = new Notice(
                        rs.getInt("id"),
                        rs.getInt("livehouse_application_id"),
                        rs.getDate("create_date").toLocalDate(),
                        rs.getDate("update_date").toLocalDate(),
                        rs.getString("message"),
                        rs.getBoolean("is_approved")
                    );

                    System.out.println("[DEBUG] Notice found: " + notice);
                    return notice;
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch notice.");
            e.printStackTrace();
        }
        System.out.println("[DEBUG] No notice found with id=" + noticeId);
        return null;
    }
}
