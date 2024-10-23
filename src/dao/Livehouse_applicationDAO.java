package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Livehouse_application;

public class Livehouse_applicationDAO {
    private DBManager dbManager;

    public Livehouse_applicationDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // Livehouse_applicationを挿入するメソッド
    public boolean insertLivehouse_application(Livehouse_application livehouse_application) {
        String sql = "INSERT INTO livehouse_application (id, livehouse_information_id, datetime, create_date, update_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouse_application.getId());
            pstmt.setInt(2, livehouse_application.getLivehouse_information_id());
            pstmt.setDate(3, Date.valueOf(livehouse_application.getDatetime()));
            pstmt.setDate(4, Date.valueOf(livehouse_application.getCreate_date()));
            pstmt.setDate(5, Date.valueOf(livehouse_application.getUpdate_date()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // IDでLivehouse_applicationを取得するメソッド
    public Livehouse_application getLivehouse_applicationById(int id) {
        String sql = "SELECT * FROM livehouse_application WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int livehouse_information_id = rs.getInt("livehouse_information_id");
                Date datetime = rs.getDate("datetime");
                Date create_date = rs.getDate("create_date");
                Date update_date = rs.getDate("update_date");

                return new Livehouse_application(
                    id, 
                    livehouse_information_id, 
                    datetime.toLocalDate(), 
                    create_date.toLocalDate(), 
                    update_date.toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Livehouse_applicationの情報を表示するメソッド
    public void printLivehouse_application(Livehouse_application livehouse_application) {
        if (livehouse_application != null) {
            System.out.println("ID: " + livehouse_application.getId());
            System.out.println("ライブハウス情報ID: " + livehouse_application.getLivehouse_information_id());
            System.out.println("日時: " + livehouse_application.getDatetime());
            System.out.println("作成日: " + livehouse_application.getCreate_date());
            System.out.println("更新日: " + livehouse_application.getUpdate_date());
        } else {
            System.out.println("該当するライブハウス申請情報が見つかりませんでした。");
        }
    }
}
