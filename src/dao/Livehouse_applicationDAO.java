package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.LivehouseApplicationWithGroup;
import model.Livehouse_application;

public class Livehouse_applicationDAO {
    private DBManager dbManager;

    public Livehouse_applicationDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // Livehouse_applicationを挿入するメソッド
    public boolean insertLivehouse_application(Livehouse_application livehouse_application) {
        String sql = "INSERT INTO livehouse_application (id, livehouse_information_id, datetime, true_false, start_time, finish_time, create_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, livehouse_application.getId());
            pstmt.setInt(2, livehouse_application.getLivehouse_information_id());
            pstmt.setDate(3, Date.valueOf(livehouse_application.getDatetime()));
            pstmt.setBoolean(4, livehouse_application.isTrueFalse()); // booleanなのでsetBooleanに修正
            pstmt.setDate(5, Date.valueOf(livehouse_application.getStart_time()));
            pstmt.setDate(6, Date.valueOf(livehouse_application.getFinish_time()));
            pstmt.setDate(7, Date.valueOf(livehouse_application.getCreate_date()));
            pstmt.setDate(8, Date.valueOf(livehouse_application.getUpdate_date()));

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
                boolean true_false = rs.getBoolean("true_false"); // booleanフィールドの取得
                Date start_time = rs.getDate("start_time");
                Date finish_time = rs.getDate("finish_time");
                Date create_date = rs.getDate("create_date");
                Date update_date = rs.getDate("update_date");

                return new Livehouse_application(
                    id, 
                    livehouse_information_id, 
                    datetime.toLocalDate(),
                    true_false,
                    start_time.toLocalDate(),
                    finish_time.toLocalDate(),
                    create_date.toLocalDate(),
                    update_date.toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //申請したグループ情報を結合
    public List<LivehouseApplicationWithGroup> getApplicationsWithGroups() {
        String sql = "SELECT " +
                     "la.id AS application_id, " +
                     "la.datetime, " +
                     "la.true_false, " +
                     "la.start_time, " +
                     "la.finish_time, " +
                     "ag.id AS group_id, " +
                     "ag.account_name, " +
                     "ag.group_genre, " +
                     "ag.band_years " +
                     "FROM livehouse_application la " +
                     "JOIN artist_group ag " +
                     "ON la.livehouse_information_id = ag.id";

        List<LivehouseApplicationWithGroup> applicationList = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LivehouseApplicationWithGroup application = new LivehouseApplicationWithGroup(
                    rs.getInt("application_id"),
                    rs.getDate("datetime").toLocalDate(),
                    rs.getBoolean("true_false"),
                    rs.getDate("start_time").toLocalDate(),
                    rs.getDate("finish_time").toLocalDate(),
                    rs.getInt("group_id"),
                    rs.getString("account_name"),
                    rs.getString("group_genre"),
                    rs.getString("band_years")
                );
                applicationList.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applicationList;
    }
 // 指定された年と月のライブハウス予約件数を取得するメソッド
    public Map<Integer, Integer> getReservationCountByMonth(int year, int month) {
        Map<Integer, Integer> reservationCounts = new HashMap<>();
        
        // SQLクエリ: 日ごとの予約件数を取得
        String sql = "SELECT DAY(datetime) AS day, COUNT(*) AS count " +
                     "FROM livehouse_application " +
                     "WHERE YEAR(datetime) = ? AND MONTH(datetime) = ? " +
                     "GROUP BY DAY(datetime)";

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



    
    //<リスト表示>
    public List<LivehouseApplicationWithGroup> getReservationsByDate(int year, int month, int day) {
        String sql = "SELECT la.id AS application_id, la.datetime, la.true_false, la.start_time, la.finish_time, " +
                     "ag.id AS group_id, ag.account_name, ag.group_genre, ag.band_years " +
                     "FROM livehouse_application la " +
                     "JOIN artist_group ag ON la.livehouse_information_id = ag.id " +
                     "WHERE YEAR(la.datetime) = ? AND MONTH(la.datetime) = ? AND DAY(la.datetime) = ?";

        List<LivehouseApplicationWithGroup> reservations = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setInt(3, day);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new LivehouseApplicationWithGroup(
                        rs.getInt("application_id"),
                        rs.getDate("datetime").toLocalDate(),
                        rs.getBoolean("true_false"),
                        rs.getDate("start_time").toLocalDate(),
                        rs.getDate("finish_time").toLocalDate(),
                        rs.getInt("group_id"),
                        rs.getString("account_name"),
                        rs.getString("group_genre"),
                        rs.getString("band_years")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }


    
    

    // Livehouse_applicationの情報を表示するメソッド
    public void printLivehouse_application(Livehouse_application livehouse_application) {
        if (livehouse_application != null) {
            System.out.println("ID: " + livehouse_application.getId());
            System.out.println("ライブハウス情報ID: " + livehouse_application.getLivehouse_information_id());
            System.out.println("日時: " + livehouse_application.getDatetime());
            System.out.println("承認: " + livehouse_application.isTrueFalse()); // フラグの表示を追加
            System.out.println("開始時間: " + livehouse_application.getStart_time());
            System.out.println("終了時間: " + livehouse_application.getFinish_time());
            System.out.println("作成日: " + livehouse_application.getCreate_date());
            System.out.println("更新日: " + livehouse_application.getUpdate_date());
        } else {
            System.out.println("該当するライブハウス申請情報が見つかりませんでした。");
        }
    }
}
