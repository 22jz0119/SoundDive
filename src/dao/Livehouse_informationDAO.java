package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

    // ライブハウス情報を挿入する
    public boolean insertLivehouse_information(Livehouse_information livehouse_information) {
        String sql = "INSERT INTO livehouse_information (id, owner_name, equipment_information, " +
                     "livehouse_explanation_information, livehouse_detailed_information, " +
                     "livehouse_name, live_address, live_tel_number, create_date, update_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouse_information.getId());
            pstmt.setString(2, livehouse_information.getOwner_name());
            pstmt.setString(3, livehouse_information.getEquipment_information());
            pstmt.setString(4, livehouse_information.getLivehouse_explanation_information());
            pstmt.setString(5, livehouse_information.getLivehouse_detailed_information());
            pstmt.setString(6, livehouse_information.getLivehouse_name());
            pstmt.setString(7, livehouse_information.getLive_address());
            pstmt.setString(8, livehouse_information.getLive_tel_number());
            pstmt.setTimestamp(9, new java.sql.Timestamp(livehouse_information.getCreateDate().getTime()));
            pstmt.setTimestamp(10, new java.sql.Timestamp(livehouse_information.getUpdateDate().getTime()));

            return pstmt.executeUpdate() > 0;
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
    
    public int createApplication(int userId, int livehouseInformationId, LocalDate datetime, boolean trueFalse, LocalDate startTime, LocalDate finishTime) {
        String sql = "INSERT INTO livehouse_application_table (user_id, livehouse_information_id, date_time, true_false, start_time, finish_time, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, livehouseInformationId);

            // date_time の null チェック
            if (datetime != null) {
                pstmt.setDate(3, java.sql.Date.valueOf(datetime));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }

            pstmt.setBoolean(4, trueFalse);

            // start_time の null チェック
            if (startTime != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(startTime));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }

            // finish_time の null チェック
            if (finishTime != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(finishTime));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }

            int rowsAffected = pstmt.executeUpdate();

            // インサートが成功した場合、生成されたIDを取得
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // 生成されたIDを返す
                    } else {
                        throw new SQLException("Creating application failed, no ID obtained.");
                    }
                }
            } else {
                throw new SQLException("Insert failed, no rows affected.");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to create livehouse application.");
            e.printStackTrace();
            return -1; // エラー時に-1を返す
        }
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
