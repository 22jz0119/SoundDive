package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Rating;

public class RatingDAO {
    private DBManager dbManager;

    public RatingDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // Ratingを挿入するメソッド
    public boolean insertRating(Rating rating) {
        String sql = "INSERT INTO rating (id, user_id, rating_star, review, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rating.getId());
            pstmt.setInt(2, rating.getUser_id());
            pstmt.setInt(3, rating.getRating_star());
            pstmt.setString(4, rating.getReview());
            pstmt.setDate(5, new Date(rating.getCreate_date().getTime()));
            pstmt.setDate(6, new Date(rating.getUpdate_date().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // IDでRatingを取得するメソッド
    public Rating getRatingById(int id) {
        String sql = "SELECT * FROM rating WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                int rating_star = rs.getInt("rating_star");
                String review = rs.getString("review");
                Date create_date = rs.getDate("create_date");
                Date update_date = rs.getDate("update_date");

                return new Rating(id, user_id, rating_star, review, create_date, update_date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ratingの情報を表示するメソッド
    public void printRating(Rating rating) {
        if (rating != null) {
            System.out.println("ID: " + rating.getId());
            System.out.println("ユーザーID: " + rating.getUser_id());
            System.out.println("評価: " + rating.getRating_star() + " スター");
            System.out.println("レビュー: " + rating.getReview());
            System.out.println("作成日: " + rating.getCreate_date());
            System.out.println("更新日: " + rating.getUpdate_date());
        } else {
            System.out.println("該当する評価情報が見つかりませんでした。");
        }
    }
}
