package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import model.User;

public class UserDAO {
    private DBManager dbManager;

    public UserDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // ユーザーを挿入するメソッド
    public boolean insertUser(User user) {
        String sql = "INSERT INTO user (id, user_name, user_password, user_tel_number, user_address, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getTelNumber());
            pstmt.setString(5, user.getAddress());
            pstmt.setTimestamp(6, user.getCreateDate());
            pstmt.setTimestamp(7, user.getUpdateDate());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // IDによってユーザーを取得するメソッド
    public User getUserById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs2model(rs); // rs2model メソッドで `User` オブジェクトを作成
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ログインIDとパスワードでユーザーを検索するメソッド
    public User findByLoginIdAndPassword(String loginId, String password) {
        String sql = "SELECT * FROM user WHERE user_login_id = ? AND user_password = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loginId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs2model(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ResultSet を User オブジェクトに変換するメソッド
    private User rs2model(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String loginId = rs.getString("user_login_id");
        String name = rs.getString("user_name");
        String password = rs.getString("user_password");
        String telNumber = rs.getString("user_tel_number");
        String address = rs.getString("user_address");
        Timestamp createDate = rs.getTimestamp("create_date");
        Timestamp updateDate = rs.getTimestamp("update_date");

        return new User(id, loginId, name, password, telNumber, address, createDate, updateDate);
    }
}
