package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import model.Livehouse_information;

public class Livehouse_informationDAO {
    private DBManager dbManager;

    public Livehouse_informationDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public boolean insertLivehouse_information(Livehouse_information livehouse_information) {
    	String sql = "INSERT INTO livehouse_information (id, oner_name, equipment_information, livehouse_explanation_information,livehouse_detailed_information, livehouse_name, live_address, live_tel_number, createDate, updateDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
    		
    		pstmt.setInt(1, livehouse_information.getId());
    		pstmt.setString(2, livehouse_information.getOner_name());
    		pstmt.setString(3, livehouse_information.getEquipment_information());
    		pstmt.setString(4, livehouse_information.getLivehouse_explanation_information());
    		pstmt.setString(5, livehouse_information.getLivehouse_detailed_information());
    		pstmt.setString(6, livehouse_information.getLivehouse_name());
    		pstmt.setString(7, livehouse_information.getLive_address());
    		pstmt.setString(8, livehouse_information.getLive_tel_number());
    		pstmt.setDate(9, new java.sql.Date(livehouse_information.getCreateDate().getTime()));
    		pstmt.setDate(10, new java.sql.Date(livehouse_information.getUpdateDate().getTime()));

    		
    		int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public Livehouse_information getLivehouse_informationById(int id) {
    	String sql = "SELECT * FROM livehouse_information WHERE = ?";
    	
    	try (Connection conn = dbManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

               pstmt.setInt(1, id);
               ResultSet rs = pstmt.executeQuery();

               if (rs.next()) {
                   String oner_name = rs.getString("oner_name");
                   String equipment_information = rs.getString("equipment_information");
                   String livehouse_explanation_information = rs.getString("livehouse_explanation_information");
                   String livehouse_detailed_information = rs.getString("livehouse_detailed_information");
                   String livehouse_name = rs.getString("livehouse_name");
                   String live_address = rs.getString("live_address");
                   String live_tel_number = rs.getString("live_tel_number");
                   Date createDate = rs.getDate("create_date");
                   Date updateDate = rs.getDate("update_date");
                   
                   return new Livehouse_information(id, oner_name, equipment_information, livehouse_explanation_information, livehouse_detailed_information, livehouse_name, live_address, live_tel_number, createDate, updateDate);
                   
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return null;
     }
    
     public void printLivehouse_informatinon(Livehouse_information livehouse_information) {
    	 if (livehouse_information != null) {
    		 System.out.println("オーナー名 :" + livehouse_information.getOner_name());
    		 System.out.println("機材情報 :" + livehouse_information.getEquipment_information());
    		 System.out.println("ライブハウス説明情報 :" + livehouse_information.getLivehouse_explanation_information());
    		 System.out.println("ライブハウス詳細情報 :" + livehouse_information.getLivehouse_detailed_information());
    		 System.out.println("ライブハウス名 : " + livehouse_information.getLivehouse_name());
    		 System.out.println("住所 :" + livehouse_information.getLive_address());
    		 System.out.println("電話番号 :" + livehouse_information.getLive_tel_number());
    		 System.out.println("作成日時 :" + livehouse_information.getCreateDate());
    		 System.out.println("更新日時 :" + livehouse_information.getUpdateDate());
    		 
    	 }else {
    		 System.out.println("ユーザーが見つかりませんでした。");
    	 }
     }
    
    

}
