package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Livehouse_information;

public class Livehouse_informationDAO {
    private DBManager dbManager;

    public Livehouse_informationDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // 共通エラーハンドリング
    private void handleSQLException(SQLException e, String message) {
        System.err.println(message);
        System.err.println("SQLState: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        e.printStackTrace();
    }

    // ライブハウス情報を挿入する 昆
    public boolean insertLivehouse_information(Livehouse_information livehouse_information) {
        String sql = "INSERT INTO livehouse_information (owner_name, equipment_information, " +
                     "livehouse_explanation_information, livehouse_detailed_information, " +
                     "livehouse_name, live_address, live_tel_number, create_date, update_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, livehouse_information.getOwner_name());
            pstmt.setString(2, livehouse_information.getEquipment_information());
            pstmt.setString(3, livehouse_information.getLivehouse_explanation_information());
            pstmt.setString(4, livehouse_information.getLivehouse_detailed_information());
            pstmt.setString(5, livehouse_information.getLivehouse_name());
            pstmt.setString(6, livehouse_information.getLive_address());
            pstmt.setString(7, livehouse_information.getLive_tel_number());
            pstmt.setTimestamp(8, new java.sql.Timestamp(livehouse_information.getCreateDate().getTime()));
            pstmt.setTimestamp(9, new java.sql.Timestamp(livehouse_information.getUpdateDate().getTime()));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    livehouse_information.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error inserting Livehouse_information");
        }
        return false;
    }


    
    // IDでライブハウス情報を取得する
    public Livehouse_information getLivehouse_informationById(int id) {
        String sql = "SELECT * FROM livehouse_information WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLivehouse(rs);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error retrieving Livehouse_information with ID: " + id);
        }
        return null;
    }

    // ライブハウス情報をリストで取得する
    public List<Livehouse_information> getAllLivehouse_information() {
        List<Livehouse_information> list = new ArrayList<>();
        String sql = "SELECT * FROM livehouse_information";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToLivehouse(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error retrieving all Livehouse_information");
        }
        return list;
    }

    // データ削除
    public boolean deleteLivehouse_informationById(int id) {
        String sql = "DELETE FROM livehouse_information WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Error deleting Livehouse_information with ID: " + id);
        }
        return false;
    }

    // データ更新
    public boolean updateLivehouse_information(Livehouse_information livehouse_information) {
        String sql = "UPDATE livehouse_information SET owner_name = ?, equipment_information = ?, " +
                     "livehouse_explanation_information = ?, livehouse_detailed_information = ?, " +
                     "livehouse_name = ?, live_address = ?, live_tel_number = ?, update_date = ? " +
                     "WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

        	
            pstmt.setString(1, livehouse_information.getOwner_name());
            pstmt.setString(2, livehouse_information.getEquipment_information());
            pstmt.setString(3, livehouse_information.getLivehouse_explanation_information());
            pstmt.setString(4, livehouse_information.getLivehouse_detailed_information());
            pstmt.setString(5, livehouse_information.getLivehouse_name());
            pstmt.setString(6, livehouse_information.getLive_address());
            pstmt.setString(7, livehouse_information.getLive_tel_number());
            pstmt.setTimestamp(8, new java.sql.Timestamp(livehouse_information.getUpdateDate().getTime()));
            pstmt.setInt(9, livehouse_information.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException(e, "Error updating Livehouse_information with ID: " + livehouse_information.getId());
        }
        return false;
    }

    // ユーティリティ: ResultSetをLivehouse_informationオブジェクトにマッピング
    private Livehouse_information mapResultSetToLivehouse(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String owner_name = rs.getString("owner_name");
        String equipment_information = rs.getString("equipment_information");
        String livehouse_explanation_information = rs.getString("livehouse_explanation_information");
        String livehouse_detailed_information = rs.getString("livehouse_detailed_information");
        String livehouse_name = rs.getString("livehouse_name");
        String live_address = rs.getString("live_address");
        String live_tel_number = rs.getString("live_tel_number");
        Date createDate = rs.getTimestamp("create_date");
        Date updateDate = rs.getTimestamp("update_date");

        return new Livehouse_information(id, owner_name, equipment_information,
                livehouse_explanation_information, livehouse_detailed_information, livehouse_name,
                live_address, live_tel_number, createDate, updateDate);
    }
}
