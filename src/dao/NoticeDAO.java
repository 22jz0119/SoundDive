package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Notice;

public class NoticeDAO {
    private DBManager dbManager;

    public NoticeDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // Notice_tableを挿入するメソッド
    public boolean insertNotice(Notice notice) {
        String sql = "INSERT INTO notice_table (id, livehouse_application_id, create_date, update_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, notice.getId());
            pstmt.setInt(2, notice.getLivehouse_application_id());
            pstmt.setDate(3, Date.valueOf(notice.getCreate_date()));
            pstmt.setDate(4, Date.valueOf(notice.getUpdate_date()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // IDでNotice_tableを取得するメソッド
    public Notice getNoticeById(int id) {
        String sql = "SELECT * FROM notice_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int livehouse_application_id = rs.getInt("livehouse_application_id");
                Date create_date = rs.getDate("create_date");
                Date update_date = rs.getDate("update_date");

                return new Notice(id, livehouse_application_id, create_date.toLocalDate(), update_date.toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Notice_tableの情報を表示するメソッド
    public void printNotice(Notice notice) {
        if (notice != null) {
            System.out.println("ID: " + notice.getId());
            System.out.println("ライブハウス申請ID: " + notice.getLivehouse_application_id());
            System.out.println("作成日: " + notice.getCreate_date());
            System.out.println("更新日: " + notice.getUpdate_date());
        } else {
            System.out.println("該当する通知情報が見つかりませんでした。");
        }
    }
}
