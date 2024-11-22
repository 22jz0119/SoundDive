package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Artist_group;
import model.Member;

public class Artist_groupDAO {
    private DBManager dbManager;

    // コンストラクタでDBManagerを受け取る
    public Artist_groupDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }
    
    // 画像ファイルをサーバーに保存し、そのパスを返すメソッド
    private String saveImageToFileSystem(byte[] imageData, String fileName) throws IOException {
        String uploadDir = "/path/to/uploads"; // 保存先ディレクトリ（適宜修正）
        File dir = new File(uploadDir);

        // ディレクトリが存在しない場合は作成
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ファイルパスを構築
        String filePath = uploadDir + File.separator + fileName;

        // ファイルに書き込む
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageData);
        }

        return filePath;
    }

    // アーティストグループを新規作成し、成功時にIDを返すメソッド
    public int createAndReturnId(Artist_group artistGroup, List<Member> members) {
        String sql = "INSERT INTO artist_group (user_id, account_name, picture_image_movie, group_genre, band_years, create_date, update_date, rating_star) "
                   + "VALUES (?, ?, ?, ?, ?, NOW(), NOW(), ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // アーティストグループを挿入
            pstmt.setInt(1, artistGroup.getUser_id());
            pstmt.setString(2, artistGroup.getAccount_name());
            pstmt.setString(3, artistGroup.getPicture_image_movie());
            pstmt.setString(4, artistGroup.getGroup_genre());
            pstmt.setInt(5, artistGroup.getBand_years());
            pstmt.setString(6, artistGroup.getRating_star());

            int affectedRows = pstmt.executeUpdate();

            // 挿入成功後、グループの ID を取得
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int groupId = rs.getInt(1);

                        // メンバーを挿入（連番で設定）
                        Member_tableDAO memberDAO = new Member_tableDAO(dbManager);
                        int artistGroupId = 1;
                        for (Member member : members) {
                            member.setArtist_group_id(groupId); // グループIDを設定
                            memberDAO.insertMember(new Member(0, groupId, member.getMember_name(), member.getMember_position()));
                            artistGroupId++;
                        }

                        return groupId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // エラーが発生した場合
    }

    
    public List<Artist_group> getGroupsByUserId(int userId) {
        String sql = "SELECT * FROM artist_group WHERE user_id = ?";
        List<Artist_group> groups = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Artist_group group = rs2model(rs);
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    
    public boolean updateGroup(Artist_group group) {
        // SQLクエリ
        String sql = "UPDATE artist_group SET account_name = ?, picture_image_movie = ?, group_genre = ?, band_years = ?, update_date = ? WHERE user_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // パラメータの設定
            if (group.getAccount_name() != null) {
                pstmt.setString(1, group.getAccount_name());
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR); // nullの場合はnullを設定
            }

            if (group.getPicture_image_movie() != null) {
                pstmt.setString(2, group.getPicture_image_movie());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR); // nullの場合はnullを設定
            }

            pstmt.setString(3, group.getGroup_genre());
            pstmt.setInt(4, group.getBand_years());
            pstmt.setDate(5, Date.valueOf(group.getUpdate_date()));
            pstmt.setInt(6, group.getUser_id());

            // 実行
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 更新が成功した場合はtrueを返す

        } catch (SQLException e) {
            System.err.println("Error updating artist group: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateArtistGroupByUserId(int userId, Artist_group updatedGroup) {
        String sql = "UPDATE artist_group SET account_name = ?, picture_image_movie = ?, group_genre = ?, band_years = ?, update_date = NOW(), rating_star = ? WHERE user_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, updatedGroup.getAccount_name());
            pstmt.setString(2, updatedGroup.getPicture_image_movie());
            pstmt.setString(3, updatedGroup.getGroup_genre());
            pstmt.setInt(4, updatedGroup.getBand_years());
            pstmt.setString(5, updatedGroup.getRating_star());
            pstmt.setInt(6, userId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // 更新が成功した場合は`true`を返す
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // エラーが発生した場合は`false`
    }


    // IDで画像パスを取得するメソッド
    public String getImagePathById(int id) {
        String sql = "SELECT picture_image_movie FROM artist_group WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("picture_image_movie"); // ファイルパスを返す
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving image path by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Artist_group getGroupByUserId(int userId) {
        String sql = "SELECT * FROM artist_group WHERE user_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs2model(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding artist group by user_id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Artist_group getArtistGroupById(int id) {
        String sql = "SELECT * FROM artist_group WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs2model(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding artist group by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ResultSetからArtist_groupオブジェクトを作成するメソッド
    private Artist_group rs2model(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int user_id = rs.getInt("user_id");
        String account_name = rs.getString("account_name");
        String picture_image_movie = rs.getString("picture_image_movie"); // ファイルパス
        Date create_date = rs.getDate("create_date");
        Date update_date = rs.getDate("update_date");
        String rating_star = rs.getString("rating_star");
        String group_genre = rs.getString("group_genre");
        int band_years = rs.getInt("band_years"); // 修正: `int` 型として取得

        return new Artist_group(id, user_id, account_name, picture_image_movie, group_genre, band_years,
                create_date != null ? create_date.toLocalDate() : null,
                update_date != null ? update_date.toLocalDate() : null,
                rating_star);
    }
}
