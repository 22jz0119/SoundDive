package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.mindrot.jbcrypt.BCrypt;

import model.User;

public class UserDAO {
    private DBManager dbManager;

    public UserDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // ユーザーを挿入するメソッド
    public boolean insertUser(User user) {
        String sql = "INSERT INTO user (name, password, tel_number, address, create_date, update_date, user_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Inserting user with name: " + user.getName());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            System.out.println("Hashed password: " + hashedPassword);

            pstmt.setString(1, user.getName());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getTel_number());
            pstmt.setString(4, user.getAddress());
            pstmt.setTimestamp(5, user.getCreateDate());
            pstmt.setTimestamp(6, user.getUpdateDate());
            pstmt.setString(7, user.getUser_type()); // user_typeを追加

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ユーザーをIDで検索するメソッド
    public User getUserById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Searching for user with ID: " + id);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("User found with ID: " + id);
                return rs2model(rs);
            } else {
                System.out.println("User not found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ユーザーを電話番号で検索するメソッド
    public User findByTelNumber(String tel_number) {
        User user = null;
        String sql = "SELECT * FROM user WHERE tel_number = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Searching for user with tel_number: " + tel_number);
            pstmt.setString(1, tel_number);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("User found with tel_number: " + tel_number);
                user = rs2model(rs);
            } else {
                System.out.println("User not found with tel_number: " + tel_number);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by tel_number: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    // 新しいユーザーを作成するメソッド
    public User create(String tel_number, String name, String password, String address, String user_type) {
        if (findByTelNumber(tel_number) != null) {
            System.out.println("User already exists with tel_number: " + tel_number);
            return null; // 既にユーザーが存在する場合
        }

        String sql = "INSERT INTO user (tel_number, name, password, address, create_date, update_date, user_type) VALUES (?, ?, ?, ?, NOW(), NOW(), ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Creating new user with tel_number: " + tel_number);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println("Hashed password for new user: " + hashedPassword);

            pstmt.setString(1, tel_number);
            pstmt.setString(2, name);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, address);
            pstmt.setString(5, user_type); // user_typeを追加

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected for new user creation: " + rowsAffected);

            if (rowsAffected > 0) {
                return findByTelNumber(tel_number); // 作成したユーザーを返す
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // 登録失敗
    }

    // 電話番号とパスワードでユーザーを検索するメソッド
    public User findByTelNumberAndPassword(String tel_number, String password) {
        System.out.println("Authenticating user with tel_number: " + tel_number);
        User user = findByTelNumber(tel_number);

        if (user != null) {
            boolean passwordMatch = BCrypt.checkpw(password, user.getPassword());
            System.out.println("Password match: " + passwordMatch);
            if (passwordMatch) {
                System.out.println("Authentication successful for tel_number: " + tel_number);
                return user;
            } else {
                System.out.println("Authentication failed: incorrect password for tel_number: " + tel_number);
            }
        } else {
            System.out.println("User not found with tel_number: " + tel_number);
        }
        return null;
    }

    // ResultSet から User モデルを作成するメソッド
    private User rs2model(ResultSet rs) throws SQLException {
        System.out.println("Mapping ResultSet to User model");
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        String telNumber = rs.getString("tel_number");
        String address = rs.getString("address");
        Timestamp createDate = rs.getTimestamp("create_date");
        Timestamp updateDate = rs.getTimestamp("update_date");
        String userType = rs.getString("user_type"); // user_typeを追加

        System.out.println("Mapped User: ID=" + id + ", name=" + name + ", userType=" + userType);
        return new User(id, name, password, telNumber, address, createDate, updateDate, userType);
    }
}
