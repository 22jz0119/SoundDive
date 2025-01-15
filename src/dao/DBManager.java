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
            instance = new DBManager();
        } else {
        }
        return instance;
    }

    // データベース接続を取得するメソッド
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found.");
        }

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            if (connection != null) {
            } else {
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // データベースに接続するテストメソッド
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
