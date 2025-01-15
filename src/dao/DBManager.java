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
    // private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + PROPERTIES;

    // シングルトンインスタンス
    private static DBManager instance = null;

    // プライベートコンストラクタ
    private DBManager() {}

    // インスタンスを取得するメソッド
    public static DBManager getInstance() {
        if (instance == null) {
            System.out.println("[DEBUG] DBManager instance is null. Creating new instance.");
            instance = new DBManager();
        } else {
            System.out.println("[DEBUG] DBManager instance already exists.");
        }
        return instance;
    }

    // データベース接続を取得するメソッド
    public Connection getConnection() throws SQLException {
        System.out.println("[DEBUG] Attempting to load JDBC Driver...");

        // JDBCドライバを手動で登録
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DEBUG] JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] JDBC Driver not found.");
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found.");
        }

        // 接続情報の確認ログ
        System.out.println("[DEBUG] Attempting to connect to DB...");
        System.out.println("[DEBUG] URL: " + URL);
        System.out.println("[DEBUG] USER: " + USER);

        // 接続取得
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            if (connection != null) {
                System.out.println("[DEBUG] DB connection established successfully.");
            } else {
                System.err.println("[ERROR] DB connection failed. Connection object is null.");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to connect to DB.");
            System.err.println("[ERROR] SQL State: " + e.getSQLState());
            System.err.println("[ERROR] Error Code: " + e.getErrorCode());
            System.err.println("[ERROR] Message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // データベースに接続するテストメソッド
    public boolean testConnection() {
        System.out.println("[DEBUG] Testing DB connection...");
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[DEBUG] DB connection test successful.");
                return true;
            } else {
                System.err.println("[ERROR] DB connection test failed. Connection is null or closed.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] DB connection test failed.");
            System.err.println("[ERROR] SQL State: " + e.getSQLState());
            System.err.println("[ERROR] Error Code: " + e.getErrorCode());
            System.err.println("[ERROR] Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
