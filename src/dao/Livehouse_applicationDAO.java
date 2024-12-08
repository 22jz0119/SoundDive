package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.LivehouseApplicationWithGroup;
import model.Livehouse_application;
import model.Livehouse_information;
import model.Member;


public class Livehouse_applicationDAO {
    private DBManager dbManager;

    public Livehouse_applicationDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // Livehouse_applicationを挿入するメソッド
    public boolean insertLivehouse_application(Livehouse_application livehouse_application) {
        // AUTO_INCREMENTの場合、idを除外
        String sql = "INSERT INTO livehouse_application_table (livehouse_information_id, user_id, date_time, true_false, start_time, finish_time, create_date, update_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 各パラメータを設定
            pstmt.setInt(1, livehouse_application.getLivehouse_information_id());
            pstmt.setInt(2, livehouse_application.getUser_id());
            pstmt.setDate(3, Date.valueOf(livehouse_application.getDate_time()));  // LocalDate -> Date
            pstmt.setBoolean(4, livehouse_application.isTrue_False());
            pstmt.setDate(5, Date.valueOf(livehouse_application.getStart_time()));  // LocalDate -> Date
            pstmt.setDate(6, Date.valueOf(livehouse_application.getFinish_time()));  // LocalDate -> Date
            pstmt.setDate(7, Date.valueOf(livehouse_application.getCreate_date()));  // LocalDate -> Date
            pstmt.setDate(8, Date.valueOf(livehouse_application.getUpdate_date()));  // LocalDate -> Date
            pstmt.setInt(9, livehouse_application.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // 挿入が成功した場合、1以上の行が影響を受ける

        } catch (SQLException e) {
            // エラーメッセージをログに出力し、再スローして呼び出し元に伝える
            System.err.println("Error while inserting livehouse application: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // IDでLivehouse_applicationを取得するメソッド
    public Livehouse_application getLivehouse_applicationById(int id) {
        String sql = "SELECT * FROM livehouse_application_table WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.err.println("Executing query to fetch Livehouse_application with ID: " + id);

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.err.println("Data found for ID: " + id);
                
                int livehouse_information_id = rs.getInt("livehouse_information_id");
                int user_id = rs.getInt("user_id");  // user_id を取得
                Date datetime = rs.getDate("date_time");
                boolean true_false = rs.getBoolean("true_false");
                Date start_time = rs.getDate("start_time");
                Date finish_time = rs.getDate("finish_time");
                Date create_date = rs.getDate("create_date");
                Date update_date = rs.getDate("update_date");

                System.err.println("Fetched data: livehouse_information_id=" + livehouse_information_id +
                                   ", user_id=" + user_id + ", date_time=" + datetime +
                                   ", true_false=" + true_false + ", start_time=" + start_time +
                                   ", finish_time=" + finish_time + ", create_date=" + create_date +
                                   ", update_date=" + update_date);

                return new Livehouse_application(
                    id, 
                    livehouse_information_id, 
                    user_id,  // user_id を設定
                    datetime.toLocalDate(),
                    true_false,
                    start_time.toLocalDate(),
                    finish_time.toLocalDate(),
                    create_date.toLocalDate(),
                    update_date.toLocalDate(), user_id, user_id
                );
            } else {
                System.err.println("No data found for ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while fetching Livehouse_application with ID: " + id);
            e.printStackTrace();
        }

        System.err.println("Returning null for ID: " + id);
        return null;
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
                    rs.getTimestamp("date_time").toLocalDateTime(), // dateTime
                    rs.getBoolean("true_false"),  // trueFalse
                    rs.getTimestamp("start_time").toLocalDateTime().toLocalDate(), // startTime
                    rs.getTimestamp("finish_time").toLocalDateTime().toLocalDate(), // finishTime
                    groupId,                      // groupId
                    rs.getString("account_name"), // accountName
                    rs.getString("group_genre"),  // groupGenre
                    rs.getString("band_years"),   // bandYears
                    rs.getInt("user_id"),         // userId
                    rs.getString("us_name"),      // usName
                    members                       // メンバーリストを追加
                );
                applicationList.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applicationList;

    }

 // グループIDに関連するメンバーリストを取得
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
                     "JOIN user u ON la.user_id = u.id " +
                     "JOIN artist_group ag ON u.id = ag.user_id " +
                     "WHERE la.id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int groupId = rs.getInt("group_id");
                    List<Member> members = getMembersByGroupId(groupId); // メンバー情報を取得

                    return new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getInt("application_id"),
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        rs.getBoolean("true_false"),
                        rs.getTimestamp("start_time").toLocalDateTime().toLocalDate(),
                        rs.getTimestamp("finish_time").toLocalDateTime().toLocalDate(),
                        groupId,
                        rs.getString("account_name"),
                        rs.getString("group_genre"),
                        rs.getString("band_years"),
                        rs.getInt("user_id"),
                        rs.getString("us_name"),
                        members // 取得したメンバーリストを設定
                    );
                }
            }
        } catch (SQLException e) {
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

    // リスト表示
    public List<LivehouseApplicationWithGroup> getReservationsWithTrueFalseZero() {
        String sql = "SELECT la.id AS application_id, la.date_time, la.true_false, la.start_time, la.finish_time, " +
                     "ag.id AS group_id, ag.account_name, ag.group_genre, ag.band_years, " +
                     "la.user_id, u.us_name " +
                     "FROM livehouse_application_table la " +
                     "JOIN user u ON la.user_id = u.id " +
                     "JOIN artist_group ag ON la.user_id = ag.user_id " +
                     "WHERE la.true_false = 0"; // ここで true_false = 0 のみを取得

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int groupId = rs.getInt("group_id");

                    // メンバー情報を取得
                    List<Member> members = getMembersByGroupId(groupId);

                    // データを LivehouseApplicationWithGroup に追加
                    reservations.add(new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),                       // applicationId
                        rs.getInt("application_id"),                       // id (同じカラムを代入)
                        rs.getTimestamp("date_time").toLocalDateTime(),    // dateTime
                        rs.getBoolean("true_false"),                       // trueFalse
                        rs.getDate("start_time").toLocalDate(),            // startTime
                        rs.getDate("finish_time").toLocalDate(),           // finishTime
                        groupId,                                           // groupId
                        rs.getString("account_name"),                      // accountName
                        rs.getString("group_genre"),                       // groupGenre
                        rs.getString("band_years"),                        // bandYears
                        rs.getInt("user_id"),                              // userId
                        rs.getString("us_name"),                           // usName
                        members                                            // メンバーリスト
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

 // ユーザーIDからus_nameを取得
    public String getUserNameByUserId(int userId) {
        String sql = "SELECT us_name FROM user WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("us_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, Boolean> getDailyReservationStatus(Livehouse_information livehouse, int year, int month) {
        int livehouseInformationId = livehouse.getId();  // Livehouse_informationからIDを取得

        // SQLクエリのテーブル名を修正
        String sql = "SELECT DAY(date_time) AS day, true_false " +
                     "FROM livehouse_application_table " +  // 修正：テーブル名を livehouse_application_table に変更
                     "WHERE livehouse_information_id = ? " +
                     "AND YEAR(date_time) = ? " +
                     "AND MONTH(date_time) = ?";

        Map<Integer, Boolean> reservationStatus = new HashMap<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouseInformationId); // Livehouse_informationからIDを取得
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int day = rs.getInt("day"); // 日付
                    boolean status = rs.getInt("true_false") == 1; // true_falseが1なら予約済み、0なら空き
                    reservationStatus.put(day, status); // 予約状態を保存
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationStatus;
    }
    

    //カレンダー申請件数表示
    public Map<String, Integer> getReservationCountsForMonth(int year, int month) throws SQLException {
        String query = "SELECT DATE_FORMAT(date_time, '%Y-%m-%d') AS date, COUNT(*) AS count " +
                       "FROM livehouse_application_table " +
                       "WHERE YEAR(date_time) = ? AND MONTH(date_time) = ? " +
                       "GROUP BY DATE(date_time)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();

            Map<String, Integer> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getString("date"), rs.getInt("count"));
            }
            return result;
        }
    }
        // `true_false`を更新するメソッド.
    
    public String updateTrueFalse(int applicationId, int trueFalseValue) {
        // `true_false`の現在の値を確認するクエリ
        String selectQuery = "SELECT true_false FROM livehouse_application_table WHERE id = ?";
        String updateQuery = "UPDATE livehouse_application_table SET true_false = ? WHERE id = ?";
        
        try (Connection connection = dbManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            
            // 現在の値を取得
            selectStmt.setInt(1, applicationId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int currentValue = rs.getInt("true_false");
                if (currentValue == 1) {
                    return "already_approved"; // すでに承認済み
                }
            }

            // 値を更新
            updateStmt.setInt(1, trueFalseValue);
            updateStmt.setInt(2, applicationId);
            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated > 0 ? "approval_success" : "update_failed";

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
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
}
