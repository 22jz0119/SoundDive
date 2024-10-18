package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectSample {

    public static void main(String[] args) {
        //DB接続用定数
        String DATABASE_NAME = "information_schema";
        String PROPERTIES = "?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
        String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + PROPERTIES;
        //DB接続用・ユーザ定数
        String USER = "root";
        String PASS = "";

        try {
            //MySQL に接続する
            Class.forName("com.mysql.cj.jdbc.Driver");
            //データベースに接続
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // データベースに対する処理
            System.out.print("データベースに接続に成功");
            
            // 接続を閉じる
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}