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
        String sql = "INSERT INTO user (id, login_id, name, password, tel_number, address, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Inserting user with ID: " + user.getId());
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getLogin_id());
            pstmt.setString(3, user.getName());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            System.out.println("Hashed password: " + hashedPassword);
            pstmt.setString(4, hashedPassword);
            pstmt.setString(5, user.getTelNumber());
            pstmt.setString(6, user.getAddress());
            pstmt.setTimestamp(7, user.getCreateDate());
            pstmt.setTimestamp(8, user.getUpdateDate());

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

    // ユーザーをログインIDで検索するメソッド
    public User findByLoginId(String loginId) {
        User user = null;
        String sql = "SELECT * FROM user WHERE login_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Searching for user with login ID: " + loginId);
            pstmt.setString(1, loginId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("User found with login ID: " + loginId);
                user = rs2model(rs);
            } else {
                System.out.println("User not found with login ID: " + loginId);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by login ID: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

 // 新しいユーザーを作成するメソッド
    public User create(String loginId, String name, String password, String telNumber, String address) {
        if (findByLoginId(loginId) != null) {
            System.out.println("User already exists with login ID: " + loginId);
            return null; // 既にユーザーが存在する場合
        }

        String sql = "INSERT INTO user (login_id, name, password, tel_number, address, create_date, update_date) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("Creating new user with login ID: " + loginId);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println("Hashed password for new user: " + hashedPassword);

            pstmt.setString(1, loginId);
            pstmt.setString(2, name);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, telNumber);
            pstmt.setString(5, address);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected for new user creation: " + rowsAffected);

            if (rowsAffected > 0) {
                return findByLoginId(loginId); // 作成したユーザーを返す
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // 登録失敗
    }

    // ログインIDとパスワードでユーザーを検索するメソッド
    public User findByLoginIdAndPassword(String loginId, String password) {
        System.out.println("Authenticating user with login ID: " + loginId);
        User user = findByLoginId(loginId);

        if (user != null) {
            boolean passwordMatch = BCrypt.checkpw(password, user.getPassword());
            System.out.println("Password match: " + passwordMatch);
            if (passwordMatch) {
                System.out.println("Authentication successful for login ID: " + loginId);
                return user;
            } else {
                System.out.println("Authentication failed: incorrect password for login ID: " + loginId);
            }
        } else {
            System.out.println("User not found with login ID: " + loginId);
        }
        return null;
    }


    // ResultSet から User モデルを作成するメソッド
    private User rs2model(ResultSet rs) throws SQLException {
        System.out.println("Mapping ResultSet to User model");
        int id = rs.getInt("id");
        String loginId = rs.getString("login_id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        String telNumber = rs.getString("tel_number");
        String address = rs.getString("address");
        Timestamp createDate = rs.getTimestamp("create_date");
        Timestamp updateDate = rs.getTimestamp("update_date");

        System.out.println("Mapped User: ID=" + id + ", loginId=" + loginId + ", name=" + name);
        return new User(id, loginId, name, password, telNumber, address, createDate, updateDate);
    }
}
