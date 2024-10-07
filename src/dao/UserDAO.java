package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserDAO {
    private DBManager dbManager = DBManager.getInstance();

    // ユーザーをDBに追加するメソッド
    public boolean insertUser(User user) {
        String sql = "INSERT INTO user_table (id, us_name, us_password, us_tel_number, us_address, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setLong(4, user.getTelNumber());
            pstmt.setString(5, user.getAddress());
            pstmt.setDate(6, new java.sql.Date(user.getCreateDate().getTime()));
            pstmt.setDate(7, new java.sql.Date(user.getUpdateDate().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 1行以上が挿入されたらtrueを返す
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ユーザー一覧を取得するメソッド
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user_table";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("us_name"));
                user.setPassword(rs.getString("us_password"));
                user.setTelNumber(rs.getLong("us_tel_number"));
                user.setAddress(rs.getString("us_address"));
                user.setCreateDate(rs.getDate("create_date"));
                user.setUpdateDate(rs.getDate("update_date"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
