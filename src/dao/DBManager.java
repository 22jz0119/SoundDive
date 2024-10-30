package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String DATABASE_NAME = "databaseg09";
    private static final String USER = "new_root";
    private static final String PASS = "#98MxdslOf;lg09";
    private static final String PROPERTIES = "?characterEncoding=UTF-8&enabledTLSProtocols=TLSv1.2";
    private static final String URL = "jdbc:mysql://158.101.151.242:3306/" + DATABASE_NAME + PROPERTIES;

    // シングルトンインスタンス
    private static DBManager instance = null;

    // プライベートコンストラクタ
    private DBManager() {}

    // インスタンスを取得するメソッド
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    // データベース接続を取得するメソッド
    public Connection getConnection() throws SQLException {
        // JDBCドライバを手動で登録
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL JDBCドライバのクラス名
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found.");
        }

        // 接続を取得して返す
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // データベースに接続するテストメソッド
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
