package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Live_artist;

public class Live_artistDAO {
    private DBManager dbManager;

    public Live_artistDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // Live_artistを挿入するメソッド
    public boolean insertLive_artist(Live_artist liveArtist) {
        String sql = "INSERT INTO live_artist (id, user_id, livehouse_application_id, create_date, update_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, liveArtist.getId());
            pstmt.setInt(2, liveArtist.getUser_id());
            pstmt.setInt(3, liveArtist.getLivehouse_application_id());
            pstmt.setDate(4, Date.valueOf(liveArtist.getCreate_date()));
            pstmt.setDate(5, Date.valueOf(liveArtist.getUpdate_date()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // IDでLive_artistを取得するメソッド
    public Live_artist getLive_artistById(int id) {
        String sql = "SELECT * FROM live_artist WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                int livehouse_application_id = rs.getInt("livehouse_application_id");
                Date create_date = rs.getDate("create_date");
                Date update_date = rs.getDate("update_date");

                return new Live_artist(id, user_id, livehouse_application_id, create_date.toLocalDate(), update_date.toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Live_artistの情報を表示するメソッド
    public void printLiveArtist(Live_artist liveArtist) {
        if (liveArtist != null) {
            System.out.println("ID: " + liveArtist.getId());
            System.out.println("ユーザーID: " + liveArtist.getUser_id());
            System.out.println("ライブハウス申請ID: " + liveArtist.getLivehouse_application_id());
            System.out.println("作成日: " + liveArtist.getCreate_date());
            System.out.println("更新日: " + liveArtist.getUpdate_date());
        } else {
            System.out.println("該当するアーティスト情報が見つかりませんでした。");
        }
    }
}
