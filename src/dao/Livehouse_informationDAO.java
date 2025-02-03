package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    
    public Map<Integer, Livehouse_information> findLivehouseInformationByIds(List<Integer> livehouseIds) throws SQLException {
        if (livehouseIds == null || livehouseIds.isEmpty()) {
            return new HashMap<>();
        }

        String placeholders = livehouseIds.stream()
                                          .map(id -> "?")
                                          .collect(Collectors.joining(","));
        String sql = "SELECT * FROM livehouse_information WHERE id IN (" + placeholders + ")";

        Map<Integer, Livehouse_information> result = new HashMap<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < livehouseIds.size(); i++) {
                pstmt.setInt(i + 1, livehouseIds.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Livehouse_information info = new Livehouse_information(
                        rs.getInt("id"),
                        rs.getString("owner_name"),
                        rs.getString("equipment_information"),
                        rs.getString("livehouse_explanation_information"),
                        rs.getString("livehouse_detailed_information"),
                        rs.getString("livehouse_name"),
                        rs.getString("live_address"),
                        rs.getString("live_tel_number"),
                        rs.getString("picture_image_naigaikan"),
                        rs.getTimestamp("create_date"),
                        rs.getTimestamp("update_date"),
                        rs.getInt("user_id")
                    );
                    result.put(info.getId(), info);
                }
            }
        }
        return result;
    }


    // ライブハウス情報を挿入する 昆
    public boolean insertLivehouse_information(Livehouse_information livehouse) {
        String sql = "INSERT INTO livehouse_information (owner_name, equipment_information, " +
                     "livehouse_explanation_information, livehouse_detailed_information, " +
                     "livehouse_name, live_tel_number, picture_image_naigaikan, create_date, update_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, livehouse.getOwner_name());
            pstmt.setString(2, livehouse.getEquipment_information());
            pstmt.setString(3, livehouse.getLivehouse_explanation_information());
            pstmt.setString(4, livehouse.getLivehouse_detailed_information());
            pstmt.setString(5, livehouse.getLivehouse_name());
            pstmt.setString(6, livehouse.getLive_tel_number());
            pstmt.setString(7, livehouse.getPicture_image_naigaikan());
            pstmt.setTimestamp(8, new java.sql.Timestamp(livehouse.getCreateDate().getTime()));
            pstmt.setTimestamp(9, new java.sql.Timestamp(livehouse.getUpdateDate().getTime()));

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
            handleSQLException(e, "Failed to insert Livehouse information.");
        }
        return false;
    }
    //user テーブルの id と livehouse_information テーブルの user_id を紐づけて、livehouse_information のデータを新規作成
    public boolean insertLivehouseInformation(Livehouse_information livehouse, int userId) {
        String sql = "INSERT INTO livehouse_information (owner_name, equipment_information, livehouse_explanation_information, " +
                     "livehouse_detailed_information, livehouse_name, live_address, live_tel_number, create_date, update_date, picture_image_naigaikan, user_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, livehouse.getOwner_name());
            pstmt.setString(2, livehouse.getEquipment_information());
            pstmt.setString(3, livehouse.getLivehouse_explanation_information());
            pstmt.setString(4, livehouse.getLivehouse_detailed_information());
            pstmt.setString(5, livehouse.getLivehouse_name());
            pstmt.setString(6, livehouse.getLive_address());
            pstmt.setString(7, livehouse.getLive_tel_number());
            pstmt.setTimestamp(8, new java.sql.Timestamp(livehouse.getCreateDate().getTime()));
            pstmt.setTimestamp(9, new java.sql.Timestamp(livehouse.getUpdateDate().getTime()));
            pstmt.setString(10, livehouse.getPicture_image_naigaikan());
            pstmt.setInt(11, userId); // user_idを紐づける

            int rows = pstmt.executeUpdate();
            return rows > 0;  // 成功した場合はtrueを返す
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // 失敗した場合はfalseを返す
        }
    }
    
    public Livehouse_information getLivehouse_informationById(int userId) {
        String sql = "SELECT * FROM livehouse_information WHERE user_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            System.out.println("[DEBUG] Executing SQL: " + pstmt);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("[DEBUG] Retrieved Data: " + rs);
                
                return new Livehouse_information(
                    rs.getInt("id"),
                    rs.getString("owner_name"),
                    rs.getString("equipment_information"),
                    rs.getString("livehouse_explanation_information"),
                    rs.getString("livehouse_detailed_information"),
                    rs.getString("livehouse_name"),
                    rs.getString("live_address"),
                    rs.getString("live_tel_number"),
                    rs.getString("picture_image_naigaikan"),
                    rs.getTimestamp("create_date"), // DATETIME 形式
                    rs.getTimestamp("update_date"),
                    rs.getInt("user_id")
                );
            } else {
                System.out.println("[DEBUG] No record found for user_id: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL Exception while retrieving Livehouse information for user_id: " + userId);
            e.printStackTrace();
        }
        return null;
    }


 // userIdでライブハウス情報を取得するメソッド
    public Livehouse_information getLivehouse_informationByUserId(int Id) {
        String sql = "SELECT * FROM livehouse_information WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Id);

            // デバッグログ: 実行するクエリとパラメータを出力
            System.out.println("[DEBUG] Executing SQL: " + pstmt);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // ライブハウス情報を取得
                String owner_name = rs.getString("owner_name");
                String equipment_information = rs.getString("equipment_information");
                String livehouse_explanation_information = rs.getString("livehouse_explanation_information");
                String livehouse_detailed_information = rs.getString("livehouse_detailed_information");
                String livehouse_name = rs.getString("livehouse_name");
                String live_address = rs.getString("live_address");
                String live_tel_number = rs.getString("live_tel_number");
                String picture_image_naigaikan = rs.getString("picture_image_naigaikan");
                Date createDate = rs.getTimestamp("create_date"); // DATETIMEを適切に処理
                Date updateDate = rs.getTimestamp("update_date"); // DATETIMEを適切に処理

                // デバッグログ: 取得した値を出力
                System.out.println("[DEBUG] Retrieved Data:");
                System.out.println("  owner_name: " + owner_name);
                System.out.println("  equipment_information: " + equipment_information);
                System.out.println("  livehouse_explanation_information: " + livehouse_explanation_information);
                System.out.println("  livehouse_detailed_information: " + livehouse_detailed_information);
                System.out.println("  livehouse_name: " + livehouse_name);
                System.out.println("  live_address: " + live_address);
                System.out.println("  live_tel_number: " + live_tel_number);
                System.out.println("  picture_image_naigaikan: " + picture_image_naigaikan);
                System.out.println("  createDate: " + createDate);
                System.out.println("  updateDate: " + updateDate);
                System.out.println("  id: " + Id);

                // 取得したデータをLivehouse_informationオブジェクトにセットして返す
                return new Livehouse_information(rs.getInt("id"), owner_name, equipment_information,
                        livehouse_explanation_information, livehouse_detailed_information, livehouse_name,
                        live_address, live_tel_number, picture_image_naigaikan, createDate, updateDate, Id);
            } else {
                // デバッグログ: レコードが見つからない場合
                System.out.println("[DEBUG] No record found for id: " + Id);
            }
        } catch (SQLException e) {
            // デバッグログ: エラー発生時の情報を出力
            System.err.println("[ERROR] SQL Exception while retrieving Livehouse information for id: " + Id);
            e.printStackTrace();
        }
        return null; // レコードが見つからなかった場合やエラーが発生した場合
    }

    // ライブハウス情報を表示するメソッド
    public void printLivehouse_information(Livehouse_information livehouse_information) {
        if (livehouse_information != null) {
            System.out.println("オーナー名 :" + livehouse_information.getOwner_name());  // 修正されたフィールド名
            System.out.println("機材情報 :" + livehouse_information.getEquipment_information());
            System.out.println("ライブハウス説明情報 :" + livehouse_information.getLivehouse_explanation_information());
            System.out.println("ライブハウス詳細情報 :" + livehouse_information.getLivehouse_detailed_information());
            System.out.println("ライブハウス名 : " + livehouse_information.getLivehouse_name());
            System.out.println("ライブハウス内外観 : " + livehouse_information.getPicture_image_naigaikan());
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
                String owner_name = rs.getString("owner_name");
                String equipment_information = rs.getString("equipment_information");
                String livehouse_explanation_information = rs.getString("livehouse_explanation_information");
                String livehouse_detailed_information = rs.getString("livehouse_detailed_information");
                String livehouse_name = rs.getString("livehouse_name");
                String live_address = rs.getString("live_address");
                String live_tel_number = rs.getString("live_tel_number");
                String picture_image_naigaikan = rs.getString("picture_image_naigaikan"); // 修正済み
                Date createDate = rs.getTimestamp("create_date");
                Date updateDate = rs.getTimestamp("update_date");
                int user_id = rs.getInt("user_id"); // user_idを取得

                Livehouse_information livehouse = new Livehouse_information(
                    id, owner_name, equipment_information, livehouse_explanation_information,
                    livehouse_detailed_information, livehouse_name, live_address, live_tel_number,
                    picture_image_naigaikan, createDate, updateDate, user_id // user_idを渡す
                );
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
                String picture_image_naigaikan = rs.getString("picture_image_naigaikan"); // 修正済み
                Date createDate = rs.getTimestamp("create_date");
                Date updateDate = rs.getTimestamp("update_date");
                int user_id = rs.getInt("user_id"); // user_idを取得

                Livehouse_information livehouse = new Livehouse_information(
                    id, owner_name, equipment_information, livehouse_explanation_information,
                    livehouse_detailed_information, livehouse_name, live_address, live_tel_number,
                    picture_image_naigaikan, createDate, updateDate, user_id // user_idを渡す
                );
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
 // データ更新
    public boolean updateLivehouse_information(Livehouse_information livehouse_information) {
        String sql = "UPDATE livehouse_information SET owner_name = ?, equipment_information = ?, " +
                     "livehouse_explanation_information = ?, livehouse_detailed_information = ?, " +
                     "livehouse_name = ?, live_address = ?, live_tel_number = ?, update_date = ?, picture_image_naigaikan = ? " +
                     "WHERE user_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータの設定
            pstmt.setString(1, livehouse_information.getOwner_name());
            pstmt.setString(2, livehouse_information.getEquipment_information());
            pstmt.setString(3, livehouse_information.getLivehouse_explanation_information());
            pstmt.setString(4, livehouse_information.getLivehouse_detailed_information());
            pstmt.setString(5, livehouse_information.getLivehouse_name());
            pstmt.setString(6, livehouse_information.getLive_address());
            pstmt.setString(7, livehouse_information.getLive_tel_number());
            pstmt.setTimestamp(8, new java.sql.Timestamp(livehouse_information.getUpdateDate().getTime()));
            pstmt.setString(9, livehouse_information.getPicture_image_naigaikan());
            pstmt.setInt(10, livehouse_information.getUser_id()); // 修正

            // デバッグログ: SQLステートメントの確認
            System.out.println("[DEBUG] Executing SQL: " + pstmt);
            System.out.println("[DEBUG] Updating Livehouse for user_id: " + livehouse_information.getUser_id());

            // クエリを実行し、更新された行数を取得
            int rowsUpdated = pstmt.executeUpdate();

            // デバッグログ: 実行結果の確認
            System.out.println("[DEBUG] Rows Updated: " + rowsUpdated);

            // 更新成功なら true を返す
            return rowsUpdated > 0;

        } catch (SQLException e) {
            // デバッグログ: エラー詳細の出力
            System.err.println("[ERROR] SQL Error during update:");
            e.printStackTrace();

            // エラー処理
            handleSQLException(e, "Failed to update Livehouse information with user_id: " + livehouse_information.getUser_id());
        }

        // 更新失敗の場合は false を返す
        return false;
    }



    
    public int createApplication(int userId, int livehouseInformationId, LocalDate datetime, boolean trueFalse, LocalDate startTime, LocalDate finishTime) {
        String sql = "INSERT INTO livehouse_application_table (user_id, livehouse_information_id, date_time, true_false, start_time, finish_time, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, livehouseInformationId);

            // date_time の設定
            if (datetime != null) {
                pstmt.setDate(3, java.sql.Date.valueOf(datetime));
            } else {
                pstmt.setNull(3, java.sql.Types.NULL);
            }

            pstmt.setBoolean(4, trueFalse);

            // start_time の設定
            if (startTime != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(startTime));
            } else {
                pstmt.setNull(5, java.sql.Types.NULL);
            }

            // finish_time の設定
            if (finishTime != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(finishTime));
            } else {
                pstmt.setNull(6, java.sql.Types.NULL);
            }

            // デバッグ情報を表示
            System.out.println("[DEBUG] SQL Parameters:");
            System.out.println("  userId: " + userId);
            System.out.println("  livehouseInformationId: " + livehouseInformationId);
            System.out.println("  datetime: " + datetime);
            System.out.println("  trueFalse: " + trueFalse);
            System.out.println("  startTime: " + startTime);
            System.out.println("  finishTime: " + finishTime);

            int rowsAffected = pstmt.executeUpdate();

            // 生成されたキーを取得
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // 生成された ID を返す
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
            return -1; // エラー時に -1 を返す
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
        String picture_image_naigaikan = rs.getString("picture_image_naigaikan"); // 修正済み
        Date createDate = rs.getTimestamp("create_date");
        Date updateDate = rs.getTimestamp("update_date");
        int user_id = rs.getInt("user_id"); // user_idを取得

        return new Livehouse_information(id, owner_name, equipment_information,
                livehouse_explanation_information, livehouse_detailed_information, livehouse_name,
                live_address, live_tel_number, picture_image_naigaikan, createDate, updateDate, user_id); // user_idを渡す
    }
    
    
    public Livehouse_information findLivehouseInformationById(int livehouseInformationId) throws SQLException {
        Livehouse_information livehouseInfo = null;
        String sql = "SELECT * FROM livehouse_information WHERE id = ?"; // livehouse_informationテーブルからIDを基に情報を取得

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livehouseInformationId);  // パラメータにライブハウス情報IDを設定
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // データベースの情報を使用してLivehouse_informationオブジェクトを作成
                    int id = rs.getInt("id");
                    String owner_name = rs.getString("owner_name");
                    String equipment_information = rs.getString("equipment_information");
                    String livehouse_explanation_information = rs.getString("livehouse_explanation_information");
                    String livehouse_detailed_information = rs.getString("livehouse_detailed_information");
                    String livehouse_name = rs.getString("livehouse_name");
                    String live_address = rs.getString("live_address");
                    String live_tel_number = rs.getString("live_tel_number");
                    String picture_image_naigaikan = rs.getString("picture_image_naigaikan");
                    Date createDate = rs.getDate("create_date");  // create_dateカラムを取得
                    Date updateDate = rs.getDate("update_date");
                    int user_id = rs.getInt("user_id");

                    livehouseInfo = new Livehouse_information(id, owner_name, equipment_information,
                            livehouse_explanation_information, livehouse_detailed_information, livehouse_name,
                            live_address, live_tel_number, picture_image_naigaikan, createDate, updateDate, user_id);
                }
            }
        }
        return livehouseInfo;  // Livehouse_informationオブジェクトを返す
    }
	 // DateをLocalDateTimeに変換するヘルパーメソッド
	    private LocalDateTime convertDateToLocalDateTime(Date date) {
	        if (date != null) {
	            return date.toInstant()
	                       .atZone(ZoneId.systemDefault())
	                       .toLocalDateTime();
	        }
	        return null;  // Nullの場合はnullを返す
	    }


}