package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDAO {
    private DBManager dbManager;

    public UserDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO user_table (id, us_name, us_password, us_tel_number, us_address, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getId().intValue());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setLong(4, user.getTelNumber());
            pstmt.setString(5, user.getAddress());
            pstmt.setDate(6, new Date(user.getCreateDate().getTime()));
            pstmt.setDate(7, new Date(user.getUpdateDate().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserById(Long id) {
        String sql = "SELECT us_name, us_password, us_tel_number, us_address, create_date, update_date FROM user_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("us_name");
                String password = rs.getString("us_password");
                Long telNumber = rs.getLong("us_tel_number");
                String address = rs.getString("us_address");
                java.util.Date createDate = new java.util.Date(rs.getTimestamp("create_date").getTime());
                java.util.Date updateDate = new java.util.Date(rs.getTimestamp("update_date").getTime());

                return new User(id, name, password, telNumber, address, createDate, updateDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printUser(User user) {
        if (user != null) {
            System.out.println("ユーザーID: " + user.getId());
            System.out.println("ユーザー名: " + user.getName());
            System.out.println("パスワード: " + user.getPassword());
            System.out.println("電話番号: " + user.getTelNumber());
            System.out.println("住所: " + user.getAddress());
            System.out.println("作成日: " + user.getCreateDate());
            System.out.println("更新日: " + user.getUpdateDate());
        } else {
            System.out.println("ユーザーが見つかりませんでした。");
        }
    }
}

// UserServiceとメインメソッドを別ファイルに作成
