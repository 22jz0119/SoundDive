package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Artist_group;

public class Artist_groupDAO {
    private DBManager dbManager;

    public Artist_groupDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    
    // Artist_groupをデータベースに挿入するメソッド
    public boolean create(Artist_group artistGroup, InputStream pictureImageStream, int imageSize) {
        String sql = "INSERT INTO artist_group (user_id, account_name, picture_image_movie, create_date, update_date, rating_star, group_genre, band_years, Janru) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistGroup.getUser_id());
            pstmt.setString(2, artistGroup.getAccount_name());
            pstmt.setBinaryStream(3, pictureImageStream, imageSize); // InputStreamで画像を挿入
            pstmt.setDate(4, Date.valueOf(artistGroup.getCreate_date()));
            pstmt.setDate(5, Date.valueOf(artistGroup.getUpdate_date()));
            pstmt.setString(6, artistGroup.getRating_star());
            pstmt.setString(7, artistGroup.getGroup_genre());
            pstmt.setString(8, artistGroup.getBand_years());
            pstmt.setString(9, artistGroup.getJanru());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Artist_groupを挿入するメソッド
    public boolean insertArtist_group(Artist_group artistGroup) {
        String sql = "INSERT INTO artist_group (id, user_id, account_name, picture_image_movie, group_genre, band_years, create_date, update_date, rating_star, Janru) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, artistGroup.getId());
            pstmt.setInt(2, artistGroup.getUser_id());
            pstmt.setString(3, artistGroup.getAccount_name());
            pstmt.setBytes(4, artistGroup.getPicture_image_movie()); // MEDIUMBLOB対応
            pstmt.setString(5, artistGroup.getGroup_genre());
            pstmt.setString(6, artistGroup.getBand_years());
            pstmt.setDate(7, Date.valueOf(artistGroup.getCreate_date()));
            pstmt.setDate(8, Date.valueOf(artistGroup.getUpdate_date()));
            pstmt.setString(9, artistGroup.getRating_star());
            pstmt.setString(10, artistGroup.getJanru()); // Janruフィールド追加

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

 // Artist_groupDAOクラス
    public byte[] getImageById(int id) {
        String sql = "SELECT picture_image_movie FROM artist_group WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("picture_image_movie"); // picture_image_movieをbyte[]で取得
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 画像が見つからない場合はnullを返す
    }

    
    // リスト表示
    public List<Artist_group> getAllArtistGroups() {
        String sql = "SELECT id, user_id, account_name, group_genre, band_years, Janru FROM artist_group";
        List<Artist_group> artistList = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String account_name = rs.getString("account_name");
                String group_genre = rs.getString("group_genre");
                String band_years = rs.getString("band_years");
                String janru = rs.getString("Janru"); // 新しいJanruフィールド

                // 必要なフィールドのみ設定
                Artist_group artist = new Artist_group(id, user_id, account_name, null, group_genre, band_years, null, null, null, janru);
                artistList.add(artist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistList;
    }

    // IDでArtist_groupを取得するメソッド
    public Artist_group getArtist_groupById(int id) {
        String sql = "SELECT * FROM artist_group WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs2model(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
 // ResultSetからArtist_groupオブジェクトを作成するメソッド
    private Artist_group rs2model(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int user_id = rs.getInt("user_id");
        String account_name = rs.getString("account_name");
        byte[] picture_image_movie = rs.getBytes("picture_image_movie"); // MEDIUMBLOB対応
        Date create_date = rs.getDate("create_date");
        Date update_date = rs.getDate("update_date");
        String rating_star = rs.getString("rating_star");
        String group_genre = rs.getString("group_genre");
        String band_years = rs.getString("band_years");
        String janru = rs.getString("Janru"); // 新しいJanruフィールド

        return new Artist_group(id, user_id, account_name, picture_image_movie, group_genre, band_years,
                create_date.toLocalDate(), update_date.toLocalDate(), rating_star, janru);
    }

}
