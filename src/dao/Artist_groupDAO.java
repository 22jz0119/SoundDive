package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Artist_group;

public class Artist_groupDAO {
    private DBManager dbManager;

    public Artist_groupDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // Artist_groupを挿入するメソッド
    public boolean insertArtist_group(Artist_group artistGroup) {
        String sql = "INSERT INTO artist_group (id, user_id, account_name, picture_image_movie, create_date, update_date, rating_star) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistGroup.getId());
            pstmt.setInt(2, artistGroup.getUser_id());
            pstmt.setString(3, artistGroup.getAccount_name());
            pstmt.setString(4, artistGroup.getPicture_image_movie());
            pstmt.setDate(5, Date.valueOf(artistGroup.getCreate_date()));
            pstmt.setDate(6, Date.valueOf(artistGroup.getUpdate_date()));
            pstmt.setString(7, artistGroup.getRating_star());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // IDでArtist_groupを取得するメソッド
    public Artist_group getArtist_groupById(int id) {
        String sql = "SELECT * FROM artist_group WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                String account_name = rs.getString("account_name");
                String picture_image_movie = rs.getString("picture_image_movie");
                Date create_date = rs.getDate("create_date");
                Date update_date = rs.getDate("update_date");
                String rating_star = rs.getString("rating_star");

                return new Artist_group(id, user_id, account_name, picture_image_movie, create_date.toLocalDate(), update_date.toLocalDate(), rating_star);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Artist_groupの情報を表示するメソッド
    public void printArtistGroup(Artist_group artistGroup) {
        if (artistGroup != null) {
            System.out.println("ID: " + artistGroup.getId());
            System.out.println("ユーザーID: " + artistGroup.getUser_id());
            System.out.println("アカウント名: " + artistGroup.getAccount_name());
            System.out.println("画像/動画: " + artistGroup.getPicture_image_movie());
            System.out.println("作成日: " + artistGroup.getCreate_date());
            System.out.println("更新日: " + artistGroup.getUpdate_date());
            System.out.println("評価星: " + artistGroup.getRating_star());
        } else {
            System.out.println("該当するアーティストグループ情報が見つかりませんでした。");
        }
    }
}
