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
    
    // ResultSetからArtist_groupオブジェクトを作成するメソッド
    private Artist_group rs2model(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int user_id = rs.getInt("user_id");
        String account_name = rs.getString("account_name");
        String picture_image_movie = rs.getString("picture_image_movie");
        Date create_date = rs.getDate("create_date");
        Date update_date = rs.getDate("update_date");
        String rating_star = rs.getString("rating_star");

        return new Artist_group(id, user_id, account_name, picture_image_movie, create_date.toLocalDate(), update_date.toLocalDate(), rating_star);
    }

    // Artist_groupをデータベースに挿入するメソッド
    public boolean create(Artist_group artistGroup) {
        String sql = "INSERT INTO artist_group (id, user_id, account_name, picture_image_movie, create_date, update_date, rating_star) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

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
            if (rowsAffected > 0) {
                System.out.println("アーティストグループが正常に作成されました。");
                return true;
            } else {
                System.out.println("アーティストグループの作成に失敗しました。");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Artist_groupを挿入するメソッド
    public boolean insertArtist_group(Artist_group artistGroup) {
        String sql = "INSERT INTO artist_group (id, user_id, account_name, picture_image_movie, create_date, update_date, rating_star) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                // rs2modelメソッドを使用してResultSetからArtist_groupを作成
                return rs2model(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;   
    }
}
