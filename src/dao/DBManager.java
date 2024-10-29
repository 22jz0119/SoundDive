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
    private static DBManager instance;

    // プライベートコンストラクタで外部からのインスタンス化を防止
    private DBManager() {}

    // シングルトンインスタンスを取得するメソッド
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    // データベース接続を取得するメソッド
    public Connection getConnection() throws SQLException {
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
