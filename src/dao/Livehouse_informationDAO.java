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
    public boolean insertLivehouse_information(Livehouse_information livehouse) {
        String sql = "INSERT INTO livehouse_information (owner_name, equipment_information, " +
                     "livehouse_explanation_information, livehouse_detailed_information, " +
                     "livehouse_name, live_tel_number, create_date, update_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, livehouse.getOwner_name());
            pstmt.setString(2, livehouse.getEquipment_information());
            pstmt.setString(3, livehouse.getLivehouse_explanation_information());
            pstmt.setString(4, livehouse.getLivehouse_detailed_information());
            pstmt.setString(5, livehouse.getLivehouse_name());
            pstmt.setString(6, livehouse.getLive_tel_number());
            pstmt.setTimestamp(7, new java.sql.Timestamp(livehouse.getCreateDate().getTime()));
            pstmt.setTimestamp(8, new java.sql.Timestamp(livehouse.getUpdateDate().getTime()));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        livehouse.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error: " + e.getMessage());
        }
        return false;
    }





    // IDでライブハウス情報を取得するメソッド
    public Livehouse_information getLivehouse_informationById(int id) {
        String sql = "SELECT * FROM livehouse_information WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String owner_name = rs.getString("owner_name");  // 修正されたフィールド名
                String equipment_information = rs.getString("equipment_information");
                String livehouse_explanation_information = rs.getString("livehouse_explanation_information");
                String livehouse_detailed_information = rs.getString("livehouse_detailed_information");
                String livehouse_name = rs.getString("livehouse_name");
                String live_address = rs.getString("live_address");
                String live_tel_number = rs.getString("live_tel_number");
                Date createDate = rs.getTimestamp("create_date");  // DATETIMEを適切に処理
                Date updateDate = rs.getTimestamp("update_date");  // DATETIMEを適切に処理

                return new Livehouse_information(id, owner_name, equipment_information,
                        livehouse_explanation_information, livehouse_detailed_information, livehouse_name,
                        live_address, live_tel_number, createDate, updateDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

    // ライブハウス情報を表示するメソッド
    public void printLivehouse_information(Livehouse_information livehouse_information) {
        if (livehouse_information != null) {
            System.out.println("オーナー名 :" + livehouse_information.getOwner_name());  // 修正されたフィールド名
            System.out.println("機材情報 :" + livehouse_information.getEquipment_information());
            System.out.println("ライブハウス説明情報 :" + livehouse_information.getLivehouse_explanation_information());
            System.out.println("ライブハウス詳細情報 :" + livehouse_information.getLivehouse_detailed_information());
            System.out.println("ライブハウス名 : " + livehouse_information.getLivehouse_name());
            System.out.println("住所 :" + livehouse_information.getLive_address());
            System.out.println("電話番号 :" + livehouse_information.getLive_tel_number());
            System.out.println("作成日時 :" + livehouse_information.getCreateDate());
            System.out.println("更新日時 :" + livehouse_information.getUpdateDate());

        } else {
            System.out.println("ユーザーが見つかりませんでした。");
        }
    }

    // すべてのライブハウス情報を取得するメソッド
    public List<Livehouse_information> get() {
        List<Livehouse_information> list = new ArrayList<>();
        try (Connection conn = dbManager.getConnection()) {
            String sql = "SELECT * FROM livehouse_information";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String owner_name = rs.getString("owner_name");  // 修正されたフィールド名
                String equipment_information = rs.getString("equipment_information");
                String livehouse_explanation_information = rs.getString("livehouse_explanation_information");
                String livehouse_detailed_information = rs.getString("livehouse_detailed_information");
                String livehouse_name = rs.getString("livehouse_name");
                String live_address = rs.getString("live_address");
                String live_tel_number = rs.getString("live_tel_number");
                Date createDate = rs.getTimestamp("create_date");  // DATETIMEを適切に処理
                Date updateDate = rs.getTimestamp("update_date");  // DATETIMEを適切に処理

                Livehouse_information livehouse = new Livehouse_information(id, owner_name, equipment_information,
                        livehouse_explanation_information, livehouse_detailed_information, livehouse_name,
                        live_address, live_tel_number, createDate, updateDate);
                list.add(livehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    
    public List<Livehouse_information> searchLivehouses(String searchQuery) {
        List<Livehouse_information> livehouses = new ArrayList<>();
        String sql = "SELECT * FROM livehouse_information WHERE livehouse_name LIKE ? COLLATE utf8mb4_general_ci";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 部分一致検索のために検索パターンを設定
            String searchPattern = "%" + searchQuery + "%";
            pstmt.setString(1, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
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

                Livehouse_information livehouse = new Livehouse_information(id, owner_name, equipment_information,
                        livehouse_explanation_information, livehouse_detailed_information, livehouse_name,
                        live_address, live_tel_number, createDate, updateDate);
                livehouses.add(livehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livehouses;
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

