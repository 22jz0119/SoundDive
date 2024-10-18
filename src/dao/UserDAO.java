package dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
    static DataSource ds;

    public Connection getConnection() throws Exception {

        // 初期コンテキスト構築
        InitialContext ic = new InitialContext();

        // DBの接続先情報を取得（context.xmlの内容）
        ds = (DataSource) ic.lookup("java:/comp/env/jdbc/book");

        return ds.getConnection();
    }
}
