package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Artist_group;
import model.LivehouseApplicationWithGroup;
import model.Livehouse_application;
import model.Livehouse_information;
import model.Member;


public class Livehouse_applicationDAO {
    private DBManager dbManager;

    public Livehouse_applicationDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public Integer getArtistGroupIdByApplicationId(int applicationId) {
        String sql = "SELECT artist_group_id FROM livehouse_application_table WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("[DEBUG] Executing SQL: " + sql + " with applicationId = " + applicationId);  // SQL実行前にログ出力
            pstmt.setInt(1, applicationId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int artistGroupId = rs.getInt("artist_group_id");
                System.out.println("[DEBUG] Found artist_group_id: " + artistGroupId);  // 結果が見つかった場合にログ出力
                return artistGroupId;
            } else {
                System.out.println("[DEBUG] No artist_group_id found for applicationId: " + applicationId);  // 結果がない場合のログ出力
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] SQLException occurred while getting artist_group_id for applicationId: " + applicationId);
            e.printStackTrace();
        }
        
        return null; // 見つからない場合は null を返す
    }
    
    public String getArtistNameByApplicationId(int applicationId) {
        String sql = "SELECT ag.account_name FROM livehouse_application_table la " +
                     "JOIN artist_group ag ON la.artist_group_id = ag.id " +
                     "WHERE la.id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);  // 申請IDをセット

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("account_name");  // アーティスト名を取得
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // 見つからない場合はnull
    }
    
    //soloの場合の申請 梅島
    public boolean saveSoloReservation(int livehouseId, int userId, LocalDateTime dateTime, LocalDateTime startTime, LocalDateTime finishTime) {
        String sql = "INSERT INTO livehouse_application_table (livehouse_information_id, user_id, date_time, start_time, finish_time, cogig_or_solo, true_false, create_date, update_date) " +
                     "VALUES (?, ?, ?, ?, ?, 1, false, NOW(), NOW())";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // パラメータを設定
            stmt.setInt(1, livehouseId); // livehouse_information_id
            stmt.setInt(2, userId); // user_id
            stmt.setTimestamp(3, Timestamp.valueOf(dateTime)); // date_time
            stmt.setTimestamp(4, Timestamp.valueOf(startTime)); // start_time
            stmt.setTimestamp(5, Timestamp.valueOf(finishTime)); // finish_time (追加)

            // SQL 実行
            int rowsAffected = stmt.executeUpdate();
            System.out.println("[DEBUG] Rows affected by saveSoloReservation: " + rowsAffected);

            // 挿入されたIDを取得
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int applicationId = generatedKeys.getInt(1);

                        // 通知を送信
                        sendNotification(applicationId, userId, livehouseId, dateTime);
                    }
                }
            }

            // 成功した場合 true を返す
            return rowsAffected > 0;
        } catch (SQLException e) {
            // エラーハンドリング
            System.err.println("[ERROR] Failed to save solo reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 対バン通知を送信
     *
     * @param applicationId 挿入されたライブハウス申請ID
     * @param userId ユーザーID
     * @param livehouseId ライブハウスID
     * @param dateTime 予約日時
     */
    private void sendNotificationToArtistGroup(int applicationId) {
        NoticeDAO noticeDAO = NoticeDAO.getInstance(dbManager);

        String sql = "SELECT sender_ag.account_name AS applicant_name, sender_ag.user_id AS applicant_user_id, " +
                     "receiver_ag.user_id AS recipient_user_id, la.livehouse_information_id, la.date_time, " +
                     "li.livehouse_name " +
                     "FROM livehouse_application_table la " +
                     "JOIN artist_group sender_ag ON la.user_id = sender_ag.user_id " +  // 申請者のアーティスト情報を取得
                     "JOIN artist_group receiver_ag ON la.artist_group_id = receiver_ag.id " + // 申請を受け取る側のアーティスト情報
                     "JOIN livehouse_information li ON la.livehouse_information_id = li.id " + // ライブハウス情報を取得
                     "WHERE la.id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, applicationId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 正しい申請者と受取人の情報を取得
                    int applicantUserId = rs.getInt("applicant_user_id");  // 申請者のユーザーID
                    int recipientUserId = rs.getInt("recipient_user_id");  // 申請を受け取るユーザーID
                    int livehouseId = rs.getInt("livehouse_information_id");  // ライブハウスID
                    LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();  // 予約日時
                    String applicantName = rs.getString("applicant_name");  // 申請者のアーティストグループ名
                    String livehouse_name = rs.getString("livehouse_name"); // ライブハウス名
                    
                    System.out.println("[DEBUG] Retrieved livehouse name: " + livehouse_name);

                    // 通知メッセージを作成
                    String message = "新しい対バン申請が届きました: 申請者 " + applicantName + 
                                     ", 予約日時: " + dateTime + ", 予約ライブハウス: " + livehouse_name;

                    // 通知を受け取る **recipientUserId** に送信
                    noticeDAO.insertNotice(applicationId, recipientUserId, message);

                    // デバッグログ
                    System.out.println("[DEBUG] Notification sent to user ID: " + recipientUserId + 
                                       " (Applicant: " + applicantName + ", Livehouse: " + livehouse_name + 
                                       ", Applicant User ID: " + applicantUserId + ")");
                } else {
                    // レコードが見つからない場合
                    System.out.println("[DEBUG] No artist group found for application ID: " + applicationId);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to send notification to artist group: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * ソロ通知を送信
     *
     * @param applicationId 挿入されたライブハウス申請ID
     * @param userId ユーザーID
     * @param livehouseId ライブハウスID
     * @param dateTime 予約日時
     */
    private void sendNotification(int applicationId, int userId, int livehouseId, LocalDateTime dateTime) {
        NoticeDAO noticeDAO = NoticeDAO.getInstance(dbManager);

        // `livehouse_information_table` から `livehouse_name` を取得するSQL
        String sql = "SELECT livehouse_name FROM livehouse_information WHERE id = ?";

        String livehouseName = "不明なライブハウス"; // 初期値

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livehouseId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    livehouseName = rs.getString("livehouse_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to retrieve livehouse name: " + e.getMessage());
            e.printStackTrace();
        }

        // 通知メッセージを作成
        String message = "新しいSOLO予約が作成されました: " +
                         "予約日時: " + dateTime +
                         ", 予約ライブハウス: " + livehouseName;

        try {
            // 通知を挿入
            noticeDAO.insertNotice(applicationId, userId, message);
            System.out.println("[DEBUG] Notification sent successfully. " +
                               "Livehouse Name: " + livehouseName);
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to send notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Livehouse_applicationを挿入するメソッド
    public boolean updateLivehouseApplication(int applicationId, int livehouseInformationId, LocalDateTime startTime, LocalDateTime finishTime) {
        String sql = "UPDATE livehouse_application_table " +
                     "SET livehouse_information_id = ?, date_time = ?, start_time = ?, finish_time = ? " +
                     "WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // **🚀 `startTime` を `date_time` にセット**
            pstmt.setInt(1, livehouseInformationId);
            pstmt.setTimestamp(2, Timestamp.valueOf(startTime)); // `date_time`
            pstmt.setTimestamp(3, Timestamp.valueOf(startTime)); // `start_time`
            pstmt.setTimestamp(4, Timestamp.valueOf(finishTime)); // `finish_time`
            pstmt.setInt(5, applicationId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sendNotificationToArtistGroup(applicationId);
                return true;
            } else {
                System.out.println("[DEBUG] No rows updated for application ID: " + applicationId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to update livehouse application: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // IDでLivehouse_applicationを取得するメソッド
	    public Livehouse_application getLivehouse_applicationById(int id) throws SQLException {
	        String sql = "SELECT * FROM livehouse_application_table WHERE id = ?";
	        try (Connection conn = dbManager.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setInt(1, id);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                int livehouse_information_id = rs.getInt("livehouse_information_id");
	                int user_id = rs.getInt("user_id");
	                Date datetime = rs.getDate("date_time");
	                boolean true_false = rs.getBoolean("true_false");
	                Date start_time = rs.getDate("start_time");
	                Date finish_time = rs.getDate("finish_time");
	                Date create_date = rs.getDate("create_date");
	                Date update_date = rs.getDate("update_date");
	                int cogig_or_solo = rs.getInt("cogig_or_solo");
	                int artist_group_id = rs.getInt("artist_group_id");

	                LocalDate dateTime = (datetime != null) ? datetime.toLocalDate() : null;
	                LocalDate startDate = (start_time != null) ? start_time.toLocalDate() : null;
	                LocalDate finishDate = (finish_time != null) ? finish_time.toLocalDate() : null;
	                LocalDate createDate = (create_date != null) ? create_date.toLocalDate() : null;
	                LocalDate updateDate = (update_date != null) ? update_date.toLocalDate() : null;

	                String usName = getUserNameByUserId(user_id);

	                return new Livehouse_application(
	                    id, 
	                    user_id, 
	                    livehouse_information_id, 
	                    dateTime, 
	                    true_false,
	                    startDate, 
	                    finishDate, 
	                    createDate, 
	                    updateDate, 
	                    cogig_or_solo,
	                    artist_group_id
	                );
	            }
	        } catch (SQLException e) {
	            // SQLExceptionが発生した場合は再スローする
	            throw new SQLException("Error occurred while fetching Livehouse application with ID: " + id, e);
	        }

	        return null;  // データが見つからなかった場合はnullを返す
	    }

 // livehouse_information_idでLivehouse_applicationを取得するメソッド
	    public List<Livehouse_application> getLivehouse_applicationsByLivehouseId(int livehouseInformationId) {
	        String sql = "SELECT * FROM livehouse_application_table WHERE livehouse_information_id = ?";
	        List<Livehouse_application> applications = new ArrayList<>();
	        
	        try (Connection conn = dbManager.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            System.err.println("Executing query to fetch Livehouse_application with livehouse_information_id: " + livehouseInformationId);

	            pstmt.setInt(1, livehouseInformationId);
	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                System.out.println("applications size: " + applications.size());

	                System.err.println("Data found for livehouse_information_id: " + livehouseInformationId);
	                
	                int id = rs.getInt("id");
	                int user_id = rs.getInt("user_id");
	                int livehouse_information_id = rs.getInt("livehouse_information_id");
	                Date date_time = rs.getDate("date_time");
	                boolean true_false = rs.getBoolean("true_false");
	                Date start_time = rs.getDate("start_time");
	                Date finish_time = rs.getDate("finish_time");
	                Date create_date = rs.getDate("create_date");
	                Date update_date = rs.getDate("update_date");
	                int cogig_or_solo = rs.getInt("cogig_or_solo");
	                int artist_group_id = rs.getInt("artist_group_id");

	                // Nullチェックとコンバート
	                LocalDate dateTimeLocal = (date_time != null) ? date_time.toLocalDate() : null;
	                LocalDate startTimeLocal = (start_time != null) ? start_time.toLocalDate() : null;
	                LocalDate finishTimeLocal = (finish_time != null) ? finish_time.toLocalDate() : null;
	                LocalDate createDateLocal = (create_date != null) ? create_date.toLocalDate() : null;
	                LocalDate updateDateLocal = (update_date != null) ? update_date.toLocalDate() : null;

	                // us_name をユーザーIDを使って取得
	                String us_name = getUserNameByUserId(user_id);

	                // Livehouse_application オブジェクトを作成
	                Livehouse_application application = new Livehouse_application(
	                    id,
	                    user_id,
	                    livehouse_information_id,
	                    dateTimeLocal,
	                    true_false,
	                    startTimeLocal,
	                    finishTimeLocal,
	                    createDateLocal,
	                    updateDateLocal,
	                    cogig_or_solo,
	                    artist_group_id
	                );

	                applications.add(application);
	            }
	        } catch (SQLException e) {
	            System.err.println("SQL Exception occurred while fetching Livehouse_application with livehouse_information_id: " + livehouseInformationId);
	            e.printStackTrace();
	        }

	        if (applications.isEmpty()) {
	            System.err.println("No data found for livehouse_information_id: " + livehouseInformationId);
	        }

	        return applications;
	    }

    // 申請したグループ情報を結合
    public List<LivehouseApplicationWithGroup> getApplicationsWithGroups() {
        String sql = "SELECT " +
                     "la.id AS application_id, " +
                     "la.date_time, " +
                     "la.true_false, " +
                     "la.start_time, " +
                     "la.finish_time, " +
                     "ag.id AS group_id, " +
                     "ag.account_name, " +
                     "ag.group_genre, " +
                     "ag.band_years, " +
                     "la.user_id, " +
                     "u.us_name " +
                     "FROM livehouse_application_table la " +
                     "JOIN user u ON la.user_id = u.id " +
                     "JOIN artist_group ag ON u.id = ag.user_id";

        List<LivehouseApplicationWithGroup> applicationList = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int groupId = rs.getInt("group_id");

                // メンバー情報を取得
                List<Member> members = getMembersByGroupId(groupId);

                LivehouseApplicationWithGroup application = new LivehouseApplicationWithGroup(
                	    rs.getInt("application_id"),  // applicationId
                	    rs.getInt("application_id"),  // id (同じカラムを代入)
                	    rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null, // dateTime (null安全)
                	    rs.getBoolean("true_false"),  // trueFalse
                	    rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null, // startTime (null安全)
                	    rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null, // finishTime (null安全)
                	    groupId,                      // groupId
                	    rs.getString("account_name") != null ? rs.getString("account_name") : "", // accountName (null安全)
                	    rs.getString("group_genre") != null ? rs.getString("group_genre") : "",  // groupGenre (null安全)
                	    rs.getString("band_years") != null ? rs.getString("band_years") : "",   // bandYears (null安全)
                	    rs.getInt("user_id"),         // userId
                	    rs.getString("us_name") != null ? rs.getString("us_name") : "",         // usName (null安全)	
                	    members                       // メンバーリストを追加
                	);
                applicationList.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applicationList;

    }
    //ライブハウス詳細ページ対バンkon
    public int getCogigOrSoloByApplicationId(int applicationId) {
        String sql = "SELECT cogig_or_solo FROM livehouse_application_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cogig_or_solo"); // NULL の場合、0 を返す
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // データがない場合
    }
    //ライブハウス詳細ページ kon
    public LivehouseApplicationWithGroup getGroupDetailsById(int groupId) {
        String sql = "SELECT id, account_name, group_genre, band_years FROM artist_group WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, groupId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new LivehouseApplicationWithGroup(
                    rs.getInt("id"),
                    rs.getInt("id"),
                    null, // date_time
                    false, // true_false
                    null, // start_time
                    null, // finish_time
                    rs.getInt("id"),
                    rs.getString("account_name"),
                    rs.getString("group_genre"),
                    rs.getString("band_years"),
                    0, // user_id
                    "", // user_name
                    new ArrayList<>()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

 // グループIDに関連するメンバーリストを取得 kon
    public List<Member> getMembersByGroupId(int groupId) {
        String sql = "SELECT id, artist_group_id, member_name, member_position " +
                     "FROM member_table WHERE artist_group_id = ?";
        List<Member> members = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, groupId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member(
                    rs.getInt("id"),
                    rs.getInt("artist_group_id"),
                    rs.getString("member_name"),
                    rs.getString("member_position")
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
    
    public LivehouseApplicationWithGroup getApplicationById(int applicationId) {
        LivehouseApplicationWithGroup application = null;

        String sql = """
            SELECT 
                la.application_id, 
                la.id, 
                la.datetime, 
                la.true_false, 
                la.start_time, 
                la.finish_time, 
                la.group_id, 
                g.account_name, 
                g.group_genre, 
                g.band_years, 
                u.user_id, 
                u.us_name
            FROM livehouse_applications la
            JOIN groups g ON la.group_id = g.group_id
            JOIN users u ON g.user_id = u.user_id
            WHERE la.application_id = ?
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // メンバーリストを取得
                    List<Member> members = getMembersByGroupId(rs.getInt("group_id"));

                    // LivehouseApplicationWithGroup のオブジェクトを作成
                    application = new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getInt("id"),
                        rs.getTimestamp("datetime").toLocalDateTime(),
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("finish_time").toLocalDateTime(),
                        rs.getInt("group_id"),
                        rs.getString("account_name"),
                        rs.getString("group_genre"),
                        rs.getString("band_years"),
                        rs.getInt("user_id"),
                        rs.getString("us_name"),
                        members
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return application;
    }

 // 指定されたIDで申請の詳細を取得するメソッド
    public LivehouseApplicationWithGroup getApplicationDetailsById(int applicationId) {
        String sql = "SELECT " +
                     "la.id AS application_id, " +
                     "la.date_time, " +
                     "la.true_false, " +
                     "la.start_time, " +
                     "la.finish_time, " +
                     "ag.id AS group_id, " +
                     "ag.account_name, " +
                     "ag.group_genre, " +
                     "ag.band_years, " +
                     "la.user_id, " +
                     "u.us_name " +
                     "FROM livehouse_application_table la " +
                     "LEFT JOIN user u ON la.user_id = u.id " +             // LEFT JOINに変更
                     "LEFT JOIN artist_group ag ON u.id = ag.user_id " +    // LEFT JOINに変更
                     "WHERE la.id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            System.out.println("[DEBUG] Executing query: " + sql + " with applicationId=" + applicationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("[DEBUG] Row data retrieved:");
                    System.out.println("application_id: " + rs.getInt("application_id"));
                    System.out.println("date_time: " + rs.getTimestamp("date_time"));
                    System.out.println("true_false: " + rs.getBoolean("true_false"));
                    System.out.println("start_time: " + rs.getTimestamp("start_time"));
                    System.out.println("finish_time: " + rs.getTimestamp("finish_time"));
                    System.out.println("group_id: " + rs.getInt("group_id"));
                    System.out.println("account_name: " + rs.getString("account_name"));
                    System.out.println("group_genre: " + rs.getString("group_genre"));
                    System.out.println("band_years: " + rs.getString("band_years"));
                    System.out.println("user_id: " + rs.getInt("user_id"));
                    System.out.println("us_name: " + rs.getString("us_name"));

                    // group_id が NULL の場合は -1 をセット
                    int groupId = rs.getObject("group_id") != null ? rs.getInt("group_id") : -1;

                    // groupIdがNULL(-1)なら、空のリストを返す
                    List<Member> members = groupId != -1 ? getMembersByGroupId(groupId) : List.of();
                    System.out.println("[DEBUG] Fetched members: " + members.size() + " members found.");

                    return new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getInt("application_id"),
                        rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null,
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null,
                        rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null,
                        groupId,
                        rs.getString("account_name") != null ? rs.getString("account_name") : "",
                        rs.getString("group_genre") != null ? rs.getString("group_genre") : "",
                        rs.getString("band_years") != null ? rs.getString("band_years") : "",
                        rs.getInt("user_id"),
                        rs.getString("us_name") != null ? rs.getString("us_name") : "",
                        members
                    );
                } else {
                    System.out.println("[DEBUG] No data found for applicationId: " + applicationId);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] SQLException occurred while fetching data for applicationId: " + applicationId);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected exception occurred.");
            e.printStackTrace();
        }

        return null;
    }


    // 指定された年と月のライブハウス予約件数を取得するメソッド
    public Map<Integer, Integer> getReservationCountByMonth(int year, int month) {
        Map<Integer, Integer> reservationCounts = new HashMap<>();
        
        // SQLクエリ: 日ごとの予約件数を取得
        String sql = "SELECT DAY(date_time) AS day, COUNT(*) AS count " +
                     "FROM livehouse_application_table " +
                     "WHERE YEAR(date_time) = ? AND MONTH(date_time) = ? " +
                     "GROUP BY DAY(date_time)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータを設定
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);

            // クエリの実行
            try (ResultSet rs = pstmt.executeQuery()) {
                // 結果をマッピング
                while (rs.next()) {
                    reservationCounts.put(rs.getInt("day"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            // エラーメッセージをログに出力
            System.err.println("Error while fetching reservation counts for year: " + year + ", month: " + month);
            e.printStackTrace();
        }
        
        return reservationCounts;
    }


    
    //承認前ソロ
    public List<LivehouseApplicationWithGroup> getReservationsWithTrueFalseZero(Integer livehouseInformationId) {
        // SQLクエリの修正：true_falseを0に変更
        String sql = "SELECT DISTINCT la.id AS application_id, la.date_time, la.true_false, la.start_time, la.finish_time, " +
                     "la.livehouse_information_id, la.user_id, ag.id AS artist_group_id, la.cogig_or_solo, " + 
                     "ag.account_name, ag.group_genre, ag.band_years, u.us_name " + 
                     "FROM livehouse_application_table la " +
                     "LEFT JOIN artist_group ag ON la.user_id = ag.user_id " +
                     "LEFT JOIN user u ON la.user_id = u.id " +
                     "WHERE la.true_false = 0 AND la.cogig_or_solo = 1"; // ← true_falseを0に変更

        // livehouseInformationId が指定されている場合のみ WHERE 句を追加
        if (livehouseInformationId != null) {
            sql += " AND la.livehouse_information_id = ?";
        }

        System.out.println("[DEBUG] SQL Query: " + sql);

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータを設定する
            if (livehouseInformationId != null) {
                pstmt.setInt(1, livehouseInformationId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int groupId = rs.getInt("artist_group_id");
                    List<Member> members = getMembersByGroupId(groupId);

                    reservations.add(new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getInt("application_id"),
                        rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null,
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null,
                        rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null,
                        groupId,
                        rs.getString("account_name") != null ? rs.getString("account_name") : "",
                        rs.getString("group_genre") != null ? rs.getString("group_genre") : "",
                        rs.getString("band_years") != null ? rs.getString("band_years") : "",
                        rs.getInt("user_id"),
                        rs.getString("us_name") != null ? rs.getString("us_name") : "",
                        members
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    //承認前対バン
    public List<LivehouseApplicationWithGroup> getReservationsByCogigOrSoloTrueFalseZero(Integer livehouseInformationId) {
        String baseSql = "SELECT la.id AS application_id, " +
                         "       la.id AS id, " +
                         "       la.date_time AS datetime, " +
                         "       la.true_false, " +
                         "       la.start_time, " +
                         "       la.finish_time, " +
                         "       la.artist_group_id AS group_id, " +
                         "       ag1.account_name AS group_account_name, " +
                         "       ag1.group_genre AS group_genre, " +
                         "       ag1.band_years AS band_years, " +
                         "       la.user_id, " +
                         "       u.us_name AS user_name, " +
                         "       ag2.id AS user_group_id, " +
                         "       ag2.account_name AS user_group_account_name, " +
                         "       ag2.group_genre AS user_group_genre, " +
                         "       ag2.band_years AS user_group_band_years " +
                         "FROM livehouse_application_table la " +
                         "LEFT JOIN user u ON la.user_id = u.id " +
                         "LEFT JOIN artist_group ag1 ON la.artist_group_id = ag1.id " +
                         "LEFT JOIN artist_group ag2 ON u.id = ag2.user_id " +
                         "WHERE la.true_false = 0 AND la.cogig_or_solo = 2"; // ← true_falseを0に変更

        // livehouseInformationId が null でない場合のみ追加
        if (livehouseInformationId != null && livehouseInformationId > 0) {
            baseSql += " AND la.livehouse_information_id = ?";
        }

        System.out.println("[DEBUG] SQL Query: " + baseSql);
        System.out.println("[DEBUG] Parameter - livehouseInformationId: " + (livehouseInformationId != null ? livehouseInformationId : "ALL"));

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(baseSql)) {

            // livehouseInformationId がある場合のみセット
            if (livehouseInformationId != null && livehouseInformationId > 0) {
                pstmt.setInt(1, livehouseInformationId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int groupId = rs.getInt("group_id");
                    int userGroupId = rs.getInt("user_group_id");

                    List<Member> groupMembers = groupId > 0 ? getMembersByGroupId(groupId) : new ArrayList<>();
                    List<Member> userGroupMembers = userGroupId > 0 ? getMembersByGroupId(userGroupId) : new ArrayList<>();

                    // artist_group_id に基づく情報をリストに追加
                    if (groupId > 0) {
                        reservations.add(new LivehouseApplicationWithGroup(
                            rs.getInt("application_id"),                      // application_id
                            rs.getInt("id"),                                  // id
                            rs.getTimestamp("datetime") != null ? rs.getTimestamp("datetime").toLocalDateTime() : null, // datetime
                            rs.getBoolean("true_false"),                      // trueFalse
                            rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null, // startTime
                            rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null, // finishTime
                            groupId,                                          // groupId
                            rs.getString("group_account_name"),               // accountName
                            rs.getString("group_genre"),                      // groupGenre
                            rs.getString("band_years"),                       // bandYears
                            rs.getInt("user_id"),                             // userId
                            rs.getString("user_name"),                        // us_name
                            groupMembers                                      // members
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }


    
    
    
    



    //承認済み１のデータ
    //リスト対バン　２の処理
    public List<LivehouseApplicationWithGroup> getReservationsByCogigOrSolo(int year, int month, int day) {
        String sql = "SELECT la.id AS application_id, " +
                     "       la.id AS id, " +
                     "       la.date_time AS datetime, " +
                     "       la.true_false, " +
                     "       la.start_time, " +
                     "       la.finish_time, " +
                     "       la.artist_group_id AS group_id, " +
                     "       ag1.account_name AS group_account_name, " +
                     "       ag1.group_genre AS group_genre, " +
                     "       ag1.band_years AS band_years, " +
                     "       la.user_id, " +
                     "       u.us_name AS user_name, " +
                     "       ag2.id AS user_group_id, " +
                     "       ag2.account_name AS user_group_account_name, " +
                     "       ag2.group_genre AS user_group_genre, " +
                     "       ag2.band_years AS user_group_band_years " +
                     "FROM livehouse_application_table la " +
                     "LEFT JOIN user u ON la.user_id = u.id " +
                     "LEFT JOIN artist_group ag1 ON la.artist_group_id = ag1.id " +
                     "LEFT JOIN artist_group ag2 ON u.id = ag2.user_id " +
                     "WHERE la.true_false = 0 " +
                     "  AND YEAR(la.date_time) = ? " +
                     "  AND MONTH(la.date_time) = ? " +
                     "  AND DAY(la.date_time) = ? " +
                     "  AND la.cogig_or_solo = 2";

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータを設定
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setInt(3, day);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // artist_group_id に基づく情報
                    int groupId = rs.getInt("group_id");
                    List<Member> groupMembers = groupId > 0 ? getMembersByGroupId(groupId) : new ArrayList<>();

                    // user_id に基づく artist_group の情報
                    int userGroupId = rs.getInt("user_group_id");
                    List<Member> userGroupMembers = userGroupId > 0 ? getMembersByGroupId(userGroupId) : new ArrayList<>();

                    // artist_group_id に基づく情報をリストに追加
                    if (groupId > 0) {
                        reservations.add(new LivehouseApplicationWithGroup(
                            rs.getInt("application_id"),                      // application_id
                            rs.getInt("id"),                                  // id
                            rs.getTimestamp("datetime").toLocalDateTime(),    // datetime
                            rs.getBoolean("true_false"),                      // trueFalse
                            rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null, // startTime
                            rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null, // finishTime
                            groupId,                                          // groupId
                            rs.getString("group_account_name"),               // accountName
                            rs.getString("group_genre"),                      // groupGenre
                            rs.getString("band_years"),                       // bandYears
                            rs.getInt("user_id"),                             // userId
                            rs.getString("user_name"),                        // us_name
                            groupMembers                                      // members
                        ));
                    }

                    // user_id に基づく artist_group の情報をリストに追加
                    if (userGroupId > 0) {
                        reservations.add(new LivehouseApplicationWithGroup(
                            rs.getInt("application_id"),                      // application_id
                            rs.getInt("id"),                                  // id
                            rs.getTimestamp("datetime").toLocalDateTime(),    // datetime
                            rs.getBoolean("true_false"),                      // trueFalse
                            rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null, // startTime
                            rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null, // finishTime
                            userGroupId,                                      // groupId
                            rs.getString("user_group_account_name"),          // accountName
                            rs.getString("user_group_genre"),                 // groupGenre
                            rs.getString("user_group_band_years"),            // bandYears
                            rs.getInt("user_id"),                             // userId
                            rs.getString("user_name"),                        // us_name
                            userGroupMembers                                  // members
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
    //承認済み２のデータ
    public List<LivehouseApplicationWithGroup> getReservationsByCogigOrSoloTrueFalseOne(Integer livehouseInformationId) {
        String baseSql = "SELECT la.id AS application_id, " +
                         "       la.id AS id, " +
                         "       la.date_time AS datetime, " +
                         "       la.true_false, " +
                         "       la.start_time, " +
                         "       la.finish_time, " +
                         "       la.artist_group_id AS group_id, " +
                         "       ag1.account_name AS group_account_name, " +
                         "       ag1.group_genre AS group_genre, " +
                         "       ag1.band_years AS band_years, " +
                         "       la.user_id, " +
                         "       u.us_name AS user_name, " +
                         "       ag2.id AS user_group_id, " +
                         "       ag2.account_name AS user_group_account_name, " +
                         "       ag2.group_genre AS user_group_genre, " +
                         "       ag2.band_years AS user_group_band_years " +
                         "FROM livehouse_application_table la " +
                         "LEFT JOIN user u ON la.user_id = u.id " +
                         "LEFT JOIN artist_group ag1 ON la.artist_group_id = ag1.id " +
                         "LEFT JOIN artist_group ag2 ON u.id = ag2.user_id " +
                         "WHERE la.true_false = 1 AND la.cogig_or_solo = 2"; // ← ここでクエリを閉じる

        // livehouseInformationId が null でない場合のみ追加
        if (livehouseInformationId != null && livehouseInformationId > 0) {
            baseSql += " AND la.livehouse_information_id = ?";
        }

        System.out.println("[DEBUG] SQL Query: " + baseSql);
        System.out.println("[DEBUG] Parameter - livehouseInformationId: " + (livehouseInformationId != null ? livehouseInformationId : "ALL"));

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(baseSql)) {

            // livehouseInformationId がある場合のみセット
            if (livehouseInformationId != null && livehouseInformationId > 0) {
                pstmt.setInt(1, livehouseInformationId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int groupId = rs.getInt("group_id");
                    int userGroupId = rs.getInt("user_group_id");

                    List<Member> groupMembers = groupId > 0 ? getMembersByGroupId(groupId) : new ArrayList<>();
                    List<Member> userGroupMembers = userGroupId > 0 ? getMembersByGroupId(userGroupId) : new ArrayList<>();

                    // artist_group_id に基づく情報をリストに追加
                    if (groupId > 0) {
                        reservations.add(new LivehouseApplicationWithGroup(
                            rs.getInt("application_id"),                      // application_id
                            rs.getInt("id"),                                  // id
                            rs.getTimestamp("datetime") != null ? rs.getTimestamp("datetime").toLocalDateTime() : null, // datetime
                            rs.getBoolean("true_false"),                      // trueFalse
                            rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null, // startTime
                            rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null, // finishTime
                            groupId,                                          // groupId
                            rs.getString("group_account_name"),               // accountName
                            rs.getString("group_genre"),                      // groupGenre
                            rs.getString("band_years"),                       // bandYears
                            rs.getInt("user_id"),                             // userId
                            rs.getString("user_name"),                        // us_name
                            groupMembers                                      // members
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    //履歴削除ボタン
    public boolean deleteReservationById(int applicationId) {
        String sql = "DELETE FROM livehouse_application_table WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
 // 承認済み（true_false = 1）のデータのみを取得するメソッド
    public List<LivehouseApplicationWithGroup> getApprovedReservations(int userId, int year, int month, int day) {
        // SQL: 承認済みのデータ（true_false = 1）のみを取得
        String sql = "SELECT DISTINCT la.id AS application_id, la.date_time, la.true_false, la.start_time, la.finish_time, " +
                     "la.livehouse_information_id, la.user_id, la.artist_group_id, la.cogig_or_solo, " +
                     "ag.account_name, ag.group_genre, ag.band_years, u.us_name " +
                     "FROM livehouse_application_table la " +
                     "LEFT JOIN artist_group ag ON la.artist_group_id = ag.id " +
                     "LEFT JOIN user u ON la.user_id = u.id " +
                     "WHERE YEAR(la.date_time) = ? " +
                     "AND MONTH(la.date_time) = ? " +
                     "AND DAY(la.date_time) = ? " +
                     "AND la.livehouse_information_id = (SELECT livehouse_information_id FROM livehouse_information WHERE user_id = ?) " +
                     "AND la.true_false = 1";  // true_false = 1 のデータのみ取得

        List<LivehouseApplicationWithGroup> approvedReservations = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータの設定
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setInt(3, day);
            pstmt.setInt(4, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // メンバー情報の取得
                    int groupId = rs.getInt("artist_group_id");
                    List<Member> members = getMembersByGroupId(groupId);

                    // 予約データをLivehouseApplicationWithGroupオブジェクトに格納
                    approvedReservations.add(new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getInt("application_id"),
                        rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null,
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null,
                        rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null,
                        groupId,
                        rs.getString("account_name") != null ? rs.getString("account_name") : "",
                        rs.getString("group_genre") != null ? rs.getString("group_genre") : "",
                        rs.getString("band_years") != null ? rs.getString("band_years") : "",
                        rs.getInt("user_id"),
                        rs.getString("us_name") != null ? rs.getString("us_name") : "",
                        members
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return approvedReservations;
    }

    
 // ユーザーIDから livehouse_information_id を取得するメソッド
    public Integer getLivehouseInformationIdForUser(int userId) {
    	// user_id に関連する livehouse_information_id を取得する
    	String sql = "SELECT li.id AS livehouse_information_id FROM livehouse_information li " +
    	             "WHERE li.user_id = ?";

    	// PreparedStatementを使ってパラメータを設定
    	try (Connection conn = dbManager.getConnection();
    	     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    	    pstmt.setInt(1, userId);  // ログインユーザーの user_id をセット
    	    try (ResultSet rs = pstmt.executeQuery()) {
    	        if (rs.next()) {
    	            return rs.getInt("livehouse_information_id");  // livehouse_information_id を返す
    	        }
    	    }
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
    	return -1; 
    }// もし取得できなかった場合は -1 を返す
    

 // 例: `livehouse_information_id` に基づいて予約データを取得するクエリ
    public List<LivehouseApplicationWithGroup> getApprovedReservationsForUser(int livehouseInformationId) {
        String sql = "SELECT DISTINCT "
                + "la.id AS application_id, la.true_false, la.start_time, la.finish_time, "
                + "la.livehouse_information_id, la.user_id, la.artist_group_id, la.cogig_or_solo, "
                + "ag.account_name, ag.group_genre, ag.band_years, u.us_name "
                + "FROM livehouse_application_table la "
                + "LEFT JOIN artist_group ag ON la.artist_group_id = ag.id "
                + "LEFT JOIN user u ON la.user_id = u.id "
                + "WHERE la.true_false = 1 AND la.livehouse_information_id = ?";

        List<LivehouseApplicationWithGroup> approvedReservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseInformationId); // ライブハウスIDを設定

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int groupId = rs.getInt("artist_group_id");

                    // グループIDに基づいてメンバーリストを取得
                    List<Member> members = getMembersByGroupId(groupId);

                    // 結果を LivehouseApplicationWithGroup オブジェクトに変換してリストに追加
                    approvedReservations.add(new LivehouseApplicationWithGroup(
                            rs.getInt("application_id"), // 申請ID
                            rs.getInt("application_id"), // 冗長な例（必要に応じて修正）
                            null, // date_time を削除したので null を設定
                            rs.getBoolean("true_false"), // 承認フラグ
                            rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null, // 開始時間
                            rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null, // 終了時間
                            groupId, // アーティストグループID
                            rs.getString("account_name") != null ? rs.getString("account_name") : "", // アーティスト名
                            rs.getString("group_genre") != null ? rs.getString("group_genre") : "", // ジャンル
                            rs.getString("band_years") != null ? rs.getString("band_years") : "", // 結成年数
                            rs.getInt("user_id"), // ユーザーID
                            rs.getString("us_name") != null ? rs.getString("us_name") : "", // ユーザー名
                            members // メンバーリスト
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return approvedReservations;
    }
    
    public List<LivehouseApplicationWithGroup> getApprovedReservationsByLivehouse(int livehouseInformationId) {
        // livehouse_information_id を使って絞り込み
        String sql = "SELECT * FROM livehouse_application_table " +
                     "WHERE livehouse_information_id = ? AND true_false = 1";

        List<LivehouseApplicationWithGroup> approvedReservations = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseInformationId); // livehouse_information_id をセット

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // データを LivehouseApplicationWithGroup オブジェクトに格納
                    approvedReservations.add(new LivehouseApplicationWithGroup(
                        rs.getInt("id"), // 'id'を使う（application_id の代わり）
                        rs.getInt("id"), // 同様に 'id' をセット
                        rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null,
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null,
                        rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null,
                        rs.getInt("artist_group_id"),
                        rs.getString("account_name") != null ? rs.getString("account_name") : "",
                        rs.getString("group_genre") != null ? rs.getString("group_genre") : "",
                        rs.getString("band_years") != null ? rs.getString("band_years") : "",
                        rs.getInt("user_id"),
                        rs.getString("us_name") != null ? rs.getString("us_name") : "",
                        null // members の取得を追加する場合は、ここに追加
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return approvedReservations;
    }

 // true_false = 1の予約データを取得するメソッド
    public List<LivehouseApplicationWithGroup> getReservationsWithTrueFalseOne(Integer livehouseInformationId) {
        String sql = "SELECT DISTINCT la.id AS application_id, la.date_time, la.true_false, la.start_time, la.finish_time, " +
                     "la.livehouse_information_id, la.user_id, ag.id AS artist_group_id, la.cogig_or_solo, " + 
                     "ag.account_name, ag.group_genre, ag.band_years, u.us_name " + 
                     "FROM livehouse_application_table la " +
                     "LEFT JOIN artist_group ag ON la.user_id = ag.user_id " +
                     "LEFT JOIN user u ON la.user_id = u.id " +
                     "WHERE la.true_false = 1 AND la.cogig_or_solo = 1"; // ← 修正：ここでクエリを閉じる

        // livehouseInformationId が指定されている場合のみ WHERE 句を追加
        if (livehouseInformationId != null) {
            sql += " AND la.livehouse_information_id = ?";
        }

        System.out.println("[DEBUG] SQL Query: " + sql);

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータを設定する
            if (livehouseInformationId != null) {
                pstmt.setInt(1, livehouseInformationId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int groupId = rs.getInt("artist_group_id");
                    List<Member> members = getMembersByGroupId(groupId);

                    reservations.add(new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getInt("application_id"),
                        rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null,
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null,
                        rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null,
                        groupId,
                        rs.getString("account_name") != null ? rs.getString("account_name") : "",
                        rs.getString("group_genre") != null ? rs.getString("group_genre") : "",
                        rs.getString("band_years") != null ? rs.getString("band_years") : "",
                        rs.getInt("user_id"),
                        rs.getString("us_name") != null ? rs.getString("us_name") : "",
                        members
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }


 // ユーザーIDからus_nameを取得するメソッド
    public String getUserNameByUserId(int userId) {
        String sql = "SELECT us_name FROM user WHERE id = ?";

        // デバッグ用: ユーザーIDと実行するSQLを表示
        System.out.println("[DEBUG] Executing SQL: " + sql + " with userId=" + userId);

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);  // ユーザーIDを設定
            ResultSet rs = pstmt.executeQuery();

            // デバッグ用: SQLクエリが正常に実行されたか確認
            System.out.println("[DEBUG] Query executed, checking result...");

            if (rs.next()) {
                // デバッグ用: 取得したデータを表示
                String usName = rs.getString("us_name");
                System.out.println("[DEBUG] Fetched us_name: " + usName);
                return usName;  // 取得したユーザー名を返す
            } else {
                // データがなかった場合のデバッグ
                System.out.println("[DEBUG] No user found with userId=" + userId);
            }
        } catch (SQLException e) {
            // エラーログを表示
            System.err.println("[ERROR] SQL Exception occurred while fetching user name with userId=" + userId);
            e.printStackTrace();
        }

        return null;  // 取得できなかった場合はnullを返す
    }

    public Map<Integer, String> getDailyReservationStatus(Livehouse_information livehouse, int year, int month) {
        int livehouseInformationId = livehouse.getId();  // Livehouse_informationからIDを取得

        String sql = "SELECT DAY(date_time) AS day, true_false " +
                     "FROM livehouse_application_table " +
                     "WHERE livehouse_information_id = ? " +
                     "AND YEAR(date_time) = ? " +
                     "AND MONTH(date_time) = ?";

        Map<Integer, String> reservationStatus = new HashMap<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseInformationId); // Livehouse_informationからIDを取得
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int day = rs.getInt("day"); // 日付
                    boolean status = rs.getInt("true_false") == 1; // true_falseが1なら予約済み、0なら空き
                    reservationStatus.put(day, status ? "×" : "〇"); // 文字列形式で保存
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationStatus;
    }
 // リスト表示 承認前　1の処理（ソロ予約の取得）
    public List<LivehouseApplicationWithGroup> getReservationsWithTrueFalseZero(int year, int month, int day) {
        // 修正したSQLクエリ：cogig_or_solo = 1 を明確に指定
        String sql = "SELECT DISTINCT la.id AS application_id, la.date_time, la.true_false, la.start_time, la.finish_time, " +
                     "la.livehouse_information_id, la.user_id, ag.id AS artist_group_id, la.cogig_or_solo, " + 
                     "ag.account_name, ag.group_genre, ag.band_years, u.us_name " + 
                     "FROM livehouse_application_table la " +
                     "LEFT JOIN artist_group ag ON la.user_id = ag.user_id " +  // user_id を使って artist_group を結合
                     "LEFT JOIN user u ON la.user_id = u.id " +
                     "WHERE la.true_false = 0 AND YEAR(la.date_time) = ? AND MONTH(la.date_time) = ? " +
                     "AND DAY(la.date_time) = ? AND la.cogig_or_solo = 1"; // cogig_or_solo = 1 を指定

        System.out.println("[DEBUG] SQL Query: " + sql);

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータを設定
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setInt(3, day);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // groupId を取得
                    int groupId = rs.getInt("artist_group_id");

                    // メンバー情報を取得
                    List<Member> members = getMembersByGroupId(groupId);

                    // データを LivehouseApplicationWithGroup に追加
                    reservations.add(new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getInt("application_id"),
                        rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null,
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time") != null ? rs.getTimestamp("start_time").toLocalDateTime() : null,
                        rs.getTimestamp("finish_time") != null ? rs.getTimestamp("finish_time").toLocalDateTime() : null,
                        groupId,
                        rs.getString("account_name") != null ? rs.getString("account_name") : "",
                        rs.getString("group_genre") != null ? rs.getString("group_genre") : "",
                        rs.getString("band_years") != null ? rs.getString("band_years") : "",
                        rs.getInt("user_id"),
                        rs.getString("us_name") != null ? rs.getString("us_name") : "",
                        members
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

 // カレンダー申請件数表示（ライブハウスごとに日別集計）
 // カレンダー申請件数表示（ログイン中のライブハウスIDに基づく件数取得）
    public Map<String, Integer> getReservationCountsByLivehouse(int year, int month, int userId) throws SQLException {
        String query = "SELECT la.livehouse_information_id, DAY(la.date_time) AS day, COUNT(*) AS count " +
                       "FROM livehouse_application_table la " +
                       "JOIN livehouse_information li ON la.livehouse_information_id = li.id " +
                       "WHERE YEAR(la.date_time) = ? AND MONTH(la.date_time) = ? AND li.user_id = ? " +
                       "AND la.true_false = 0 " + // ← 承認待ちの予約データのみ取得
                       "GROUP BY la.livehouse_information_id, day " +
                       "ORDER BY day";

        System.out.println("[DEBUG] Executing query: " + query);
        System.out.println("[DEBUG] Parameters - year: " + year + ", month: " + month + ", userId: " + userId);

        Map<String, Integer> result = new LinkedHashMap<>(); // 日別件数保持

        try (Connection conn = dbManager.getConnection()) {
            if (conn == null || conn.isClosed()) {
                System.err.println("[ERROR] Database connection could not be established.");
                throw new SQLException("Database connection failed.");
            }
            System.out.println("[DEBUG] Database connection established successfully.");

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                stmt.setInt(3, userId);
                System.out.println("[DEBUG] PreparedStatement parameters set successfully.");

                try (ResultSet rs = stmt.executeQuery()) {
                    System.out.println("[DEBUG] Query executed successfully. Processing ResultSet...");

                    boolean hasResults = false;
                    while (rs.next()) {
                        hasResults = true;
                        String day = Integer.toString(rs.getInt("day"));   // 日付 (1～31)
                        int count = rs.getInt("count"); // 予約件数

                        result.put(day, count);

                        System.out.println("[DEBUG] Retrieved - day: " + day + ", count: " + count);
                    }

                    if (!hasResults) {
                        System.out.println("[DEBUG] Query returned no results.");
                    }
                }

                System.out.println("[DEBUG] Total days with reservations: " + result.size());
                if (!result.isEmpty()) {
                    System.out.println("[DEBUG] First entry: " + result.entrySet().iterator().next());
                }

            } catch (SQLException e) {
                System.err.println("[ERROR] SQLException occurred while executing query: " + e.getMessage());
                System.err.println("[ERROR] SQL State: " + e.getSQLState());
                System.err.println("[ERROR] Error Code: " + e.getErrorCode());
                e.printStackTrace();
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Database operation failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Unexpected exception occurred while retrieving reservation counts for the logged-in livehouse.", e);
        }

        System.out.println("[DEBUG] Returning result: " + result);
        return result;
    }

    public Integer getSingleLivehouseInformationIdByUserId(int userId) {
        String sql = "SELECT id FROM livehouse_information WHERE user_id = ? LIMIT 1";
        Integer livehouseId = null;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    livehouseId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("[DEBUG] Retrieved livehouseInformationId for userId " + userId + ": " + livehouseId);
        return livehouseId;
    }



 // user_idからlivehouse_information_idを取得
 public int getLivehouseIdByUserId(int userId) {
     String sql = "SELECT id FROM livehouse_information WHERE user_id = ?";
     try (Connection conn = dbManager.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setInt(1, userId);
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
             return rs.getInt("id");
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return -1;  // 取得失敗時
 }

 /**
  * `true_false`を1に更新するメソッド
  */
 private void updateTrueFalse(int applicationId) {
     DBManager dbManager = DBManager.getInstance();
     String updateQuery = "UPDATE livehouse_application_table SET true_false = 1 WHERE id = ?";
     
     try (Connection connection = dbManager.getConnection();
          PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
         
         stmt.setInt(1, applicationId);
         int rowsUpdated = stmt.executeUpdate();

         if (rowsUpdated > 0) {
             System.out.println("[DEBUG] Application ID " + applicationId + " updated successfully.");
         } else {
             System.err.println("[ERROR] Application ID " + applicationId + " update failed.");
         }
     } catch (SQLException e) {
         e.printStackTrace();
         throw new RuntimeException("Failed to update true_false in the database", e);
     }
 }


    
    //梅島
    public int createApplication(int userId, Integer livehouseInformationId, LocalDateTime date_time, 
            boolean trueFalse, LocalDateTime startTime, LocalDateTime finishTime, 
            int cogigOrSolo, int artistGroupId) {
String sql = "INSERT INTO livehouse_application_table " +
"(user_id, livehouse_information_id, date_time, true_false, start_time, finish_time, " +
"cogig_or_solo, artist_group_id, create_date, update_date) " +
"VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

try (Connection conn = dbManager.getConnection();
PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

// パラメータをセット
pstmt.setInt(1, userId);

// livehouseInformationId の null チェック
if (livehouseInformationId != null) {
pstmt.setInt(2, livehouseInformationId);
} else {
pstmt.setNull(2, java.sql.Types.INTEGER); // livehouseInformationId が null の場合
}

// date_time の null チェック
if (date_time != null) {
pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(date_time)); // LocalDateTime -> java.sql.Timestamp
} else {
pstmt.setNull(3, java.sql.Types.TIMESTAMP); // date_time が null の場合
}

pstmt.setBoolean(4, trueFalse);

// startTime の null チェック
if (startTime != null) {
pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(startTime)); // LocalDateTime -> java.sql.Timestamp
} else {
pstmt.setNull(5, java.sql.Types.TIMESTAMP); // startTime が null の場合
}

// finishTime の null チェック
if (finishTime != null) {
pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(finishTime)); // LocalDateTime -> java.sql.Timestamp
} else {
pstmt.setNull(6, java.sql.Types.TIMESTAMP); // finishTime が null の場合
}

pstmt.setInt(7, cogigOrSolo); // cogigOrSolo の値をセット
pstmt.setInt(8, artistGroupId); // artistGroupId の値をセット

// SQL 実行
int rowsAffected = pstmt.executeUpdate();

// 成功時に生成されたIDを返す
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
e.printStackTrace();
return -1; // エラー時に -1 を返す
}
}
    //ライブハウス予約
    public Map<Integer, Integer> getApplicationCountsByDate(int livehouseId, int year, int month) throws SQLException {
        String sql = "SELECT DAY(date_time) AS day, COUNT(*) AS count " +
                     "FROM livehouse_application_table " +
                     "WHERE livehouse_information_id = ? " +
                     "AND YEAR(date_time) = ? " +
                     "AND MONTH(date_time) = ? " +
                     "GROUP BY DAY(date_time)";

        Map<Integer, Integer> applicationCounts = new HashMap<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            System.out.println("[DEBUG] Executing query: " + pstmt);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int day = rs.getInt("day");
                    int count = rs.getInt("count");
                    applicationCounts.put(day, count);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error fetching application counts by date");
            e.printStackTrace();
            throw e;
        }

        return applicationCounts;
    }
    
    public Map<Integer, String> getReservationStatusByMonthAndLivehouseId(int livehouseId, int year, int month) {
        String sql = "SELECT DAY(date_time) AS day, true_false " +
                     "FROM livehouse_application_table " +
                     "WHERE livehouse_information_id = ? " +
                     "AND date_time IS NOT NULL " +
                     "AND YEAR(date_time) = ? " +
                     "AND MONTH(date_time) = ?";

        Map<Integer, String> reservationStatus = new HashMap<>();

        System.out.println("[DEBUG] Executing SQL Query: " + sql);
        System.out.println("[DEBUG] Parameters - livehouseInformationId: " + livehouseId + ", Year: " + year + ", Month: " + month);

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseId);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    int day = rs.getInt("day");
                    boolean isReserved = rs.getInt("true_false") == 1; // 1が予約済みの場合
                    
                    reservationStatus.put(day, isReserved ? "×" : "〇");
                    rowCount++;
                }
                System.out.println("[DEBUG] Total Rows Fetched: " + rowCount);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL Exception occurred while fetching reservation status");
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch reservation status", e); // 必要に応じて例外をスロー
        }

        System.out.println("[DEBUG] Final Reservation Status Map: " + reservationStatus);
        return reservationStatus;
    }


    // Livehouse_applicationの情報を表示するメソッド
    public void printLivehouse_application(Livehouse_application livehouse_application) {
        if (livehouse_application != null) {
            System.out.println("ID: " + livehouse_application.getId());
            System.out.println("ユーザーID" + livehouse_application.getUser_id());
            System.out.println("ライブハウス情報ID: " + livehouse_application.getLivehouse_information_id());
            System.out.println("日時: " + livehouse_application.getDate_time());
            System.out.println("承認: " + livehouse_application.isTrue_False()); // フラグの表示を追加
            System.out.println("開始時間: " + livehouse_application.getStart_time());
            System.out.println("終了時間: " + livehouse_application.getFinish_time());
            System.out.println("作成日: " + livehouse_application.getCreate_date());
            System.out.println("更新日: " + livehouse_application.getUpdate_date());
        } else {
            System.out.println("該当するライブハウス申請情報が見つかりませんでした。");
        }
    }
 // livehouse_information_idを元にLivehouse_informationを取得
    public Livehouse_information getLivehouseInformationById(int livehouseId) {
        String sql = "SELECT * FROM livehouse_information WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
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
                    rs.getTimestamp("create_date"),
                    rs.getTimestamp("update_date"),
                    rs.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Livehouse information by ID: " + livehouseId);
            e.printStackTrace();
        }
        return null;
    }
 // ユーザーIDを元にライブハウス申請情報を取得
    public List<Livehouse_application> getApplicationsByUserId(int userId) throws SQLException {
        List<Livehouse_application> applications = new ArrayList<>();
        String sql = "SELECT * FROM livehouse_application_table WHERE user_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int livehouse_information_id = rs.getInt("livehouse_information_id");
                    LocalDate date_time = rs.getDate("date_time") != null ? rs.getDate("date_time").toLocalDate() : null;
                    boolean true_false = rs.getBoolean("true_false");
                    LocalDate start_time = rs.getDate("start_time") != null ? rs.getDate("start_time").toLocalDate() : null;
                    LocalDate finish_time = rs.getDate("finish_time") != null ? rs.getDate("finish_time").toLocalDate() : null;
                    LocalDate create_date = rs.getDate("create_date") != null ? rs.getDate("create_date").toLocalDate() : null;
                    LocalDate update_date = rs.getDate("update_date") != null ? rs.getDate("update_date").toLocalDate() : null;
                    int cogig_or_solo = rs.getInt("cogig_or_solo");
                    int artist_group_id = rs.getInt("artist_group_id");

                    // Livehouse_applicationオブジェクトを作成し、リストに追加
                    Livehouse_application application = new Livehouse_application(
                        id, userId, livehouse_information_id, date_time, true_false, start_time, finish_time,
                        create_date, update_date, cogig_or_solo, artist_group_id
                    );
                    applications.add(application);
                }
            }
        }
        return applications;
    }
    
    // アーティストのHomeで使ってる
    public List<Livehouse_application> getApplicationsByUserId(int userId, Boolean trueFalse) throws SQLException {
        List<Livehouse_application> applications = new ArrayList<>();
        String sql = "SELECT * FROM livehouse_application_table WHERE user_id = ?";
        if (trueFalse != null) {
            sql += " AND true_false = ?";
        }
        sql += " ORDER BY date_time ASC";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            if (trueFalse != null) {
                stmt.setBoolean(2, trueFalse);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(mapResultSetToLivehouseApplication(rs, userId));
                }
            }
        }
        return applications;
    }

    private Livehouse_application mapResultSetToLivehouseApplication(ResultSet rs, int userId) throws SQLException {
        int id = rs.getInt("id");
        int livehouse_information_id = rs.getInt("livehouse_information_id");
        LocalDate date_time = rs.getDate("date_time") != null ? rs.getDate("date_time").toLocalDate() : null;
        boolean true_false = rs.getBoolean("true_false");
        LocalDate start_time = rs.getDate("start_time") != null ? rs.getDate("start_time").toLocalDate() : null;
        LocalDate finish_time = rs.getDate("finish_time") != null ? rs.getDate("finish_time").toLocalDate() : null;
        LocalDate create_date = rs.getDate("create_date") != null ? rs.getDate("create_date").toLocalDate() : null;
        LocalDate update_date = rs.getDate("update_date") != null ? rs.getDate("update_date").toLocalDate() : null;
        int cogig_or_solo = rs.getInt("cogig_or_solo");
        int artist_group_id = rs.getInt("artist_group_id");

        return new Livehouse_application(
            id, userId, livehouse_information_id, date_time, true_false, start_time, finish_time,
            create_date, update_date, cogig_or_solo, artist_group_id
        );
    }
    
    private Artist_group rs2model(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int user_id = rs.getInt("user_id");
        String account_name = rs.getString("account_name");
        String picture_image_movie = rs.getString("picture_image_movie");
        String group_genre = rs.getString("group_genre");
        int band_years = rs.getInt("band_years");
        Date create_date = rs.getDate("create_date");
        Date update_date = rs.getDate("update_date");
        String rating_star = rs.getString("rating_star");
        boolean at_true_false = rs.getBoolean("at_true_false");

        return new Artist_group(
            id,
            user_id,
            account_name,
            picture_image_movie,
            group_genre,
            band_years,
            create_date != null ? create_date.toLocalDate() : null,
            update_date != null ? update_date.toLocalDate() : null,
            rating_star,
            at_true_false
        );
    }
}