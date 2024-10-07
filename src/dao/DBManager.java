package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	private static final String CN_STRING = "jdbc:oracle:thin:@158.101.151.242:1521/jz2201G09atp01";

	private static final String USER = "ADMIN";
	private static final String PASS = "BtWQhvCgMrhBe)%H.h9T(5G5i,d88&";
	
	private static DBManager self;
	
	/**
	 * コンストラクタ
	 */
	private DBManager() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException e) {
			System.out.println("JDBCドライバのロードに失敗しました");
			e.printStackTrace();
			return;
		}
	}
	
	
	
	/**
	 * インスタンスを取得する
	 * @return self
	 */
	public static DBManager getInstance() {
		if (self == null) {// まだ一度もインスタンス化してなければ
			self = new DBManager();
		}
		return self;
	}
	
	/**
	 * コネクションを取得
	 */
	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CN_STRING, USER, PASS);
	}
	
	public static void testConnection() {
        Connection connection = null;

        try {
            connection = getInstance().getConnection();
            if (connection != null) {
                System.out.println("接続に成功しました！");
            } else {
                System.out.println("接続に失敗しました。");
            }
        } catch (SQLException e) {
            System.out.println("SQLエラーが発生しました。");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close(); // 接続を閉じる
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
	    public static void main(String[] args) {
	        DBManager.testConnection();
	    }
}

