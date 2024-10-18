package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Artist_group;

public class Artist_groupDAO {
    private DBManager dbManager = DBManager.getInstance();
    
    public boolean insertArtist_group_table(Artist_group artist_group_table) {
        String sql = "INSERT INTO artist_group_table(id, user_id, account_name, picture_image_movie, create_date, update_date, rating_star)  VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1,  artist_group_table.getId());
            pstmt.setInt(2,  artist_group_table.getUser_id());
            pstmt.setString(3, artist_group_table.getAccount_name());
            pstmt.setString(4,  artist_group_table.getPicture_image_movie());
            pstmt.setDate(5, new java.sql.Date(artist_group_table.getCreate_date().getTime())); // カンマ追加
            pstmt.setDate(6, new java.sql.Date(artist_group_table.getUpdate_date().getTime()));
            pstmt.setString(7,  artist_group_table.getRating_star());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 1行以上が挿入されたらtrueを返す
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 例外発生時はfalseを返す
    }
    public List<Artist_group> getAllArtist_groups() {
        List<Artist_group> Artist_groups = new ArrayList<>();
        String sql = "SELECT * FROM Artist_group_table";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
            	Artist_group artist_group_table = new Artist_group();
                artist_group.setId(rs.getInt("id"));
                artist_group.setUser_id(rs.getString("user_id"));
                artist_group.setAccount_name(rs.getString("account_name"));
                artist_group.setPicture_image_movie(rs.getLong("picture_image_movie"));
                artist_group.setCreate_date(rs.getDate("create_date"));
                artist_group.setUpdate_date(rs.getDate("update_date"));
                artist_group.setRating_star(rs.getString("rating_star"));
                
                
                

                Artist_groups.add(artist_group_table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Artist_groups;
    }
    
}
