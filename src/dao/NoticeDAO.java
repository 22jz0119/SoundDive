package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Notice;

public class NoticeDAO {
    private static NoticeDAO instance;
    private DBManager dbManager;

    public NoticeDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public static synchronized NoticeDAO getInstance(DBManager dbManager) {
        if (instance == null) {
            instance = new NoticeDAO(dbManager);
        }
        return instance;
    }

    /**
     * 通知を挿入するメソッド
     */
    public void insertNotice(int livehouseApplicationId, int userId, String message) throws SQLException {
        String sql = "INSERT INTO notice_table (livehouse_application_id, create_date, message, is_read, user_id) " +
                     "VALUES (?, NOW(), ?, 0, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livehouseApplicationId);
            stmt.setString(2, message);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        }
    }
    
    /**
     * ユーザーIDに基づいて通知を取得
     * @param userId ユーザーID
     * @return 通知リスト
     * @throws SQLException データベースエラー
     */
    public List<Notice> getNotificationsByUserId(int userId) throws SQLException {
        String sql = "SELECT id, livehouse_application_id, create_date, update_date, message, is_read, user_id " +
                     "FROM notice_table WHERE user_id = ? ORDER BY create_date DESC";
        List<Notice> notifications = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Noticeオブジェクトを新しいコンストラクタで生成
                    Notice notification = new Notice(
                        rs.getInt("id"),
                        rs.getInt("livehouse_application_id"),
                        rs.getTimestamp("create_date") != null ? rs.getTimestamp("create_date").toLocalDateTime() : null,
                        rs.getTimestamp("update_date") != null ? rs.getTimestamp("update_date").toLocalDateTime() : null,
                        rs.getString("message"),
                        rs.getBoolean("is_read"),
                        rs.getInt("user_id")
                    );
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }

}
