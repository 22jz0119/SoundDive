package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.User;

public class UserDAO {
    private DBManager dbManager;

    public UserDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO user_table (id, us_name, us_password, us_tel_number, us_address, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        // DBManagerのinstanceフィールドから直接Connectionを取得
        try (Connection conn = dbManager.instance;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId().intValue()); // Long型のIDをint型に変換
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setLong(4, user.getTelNumber());
            pstmt.setString(5, user.getAddress());
            pstmt.setDate(6, new Date(user.getCreateDate().getTime()));
            pstmt.setDate(7, new Date(user.getUpdateDate().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 1行以上が挿入されたらtrueを返す
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
