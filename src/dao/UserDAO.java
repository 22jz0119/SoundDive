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
            pstmt.setDate(6, new Date(user.getCreateDate().getTime()));
            pstmt.setDate(7, new Date(user.getUpdateDate().getTime()));

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
                String name = rs.getString("user_name");
                String password = rs.getString("user_password");
                String telNumber = rs.getString("user_tel_number");
                String address = rs.getString("user_address");
                Date createDate = rs.getDate("create_date");
                Date updateDate = rs.getDate("update_date");

                return new User(id, name, password, telNumber, address, createDate, updateDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ユーザー情報を表示するメソッド
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
