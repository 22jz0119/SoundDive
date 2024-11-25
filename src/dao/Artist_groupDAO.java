package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Artist_group;
import model.Member;

/**
 * Artist_groupDAO: アーティストグループに関連するデータベース操作を行うクラス
 */
public class Artist_groupDAO {
    private DBManager dbManager;

    // コンストラクタでDBManagerを受け取る
    public Artist_groupDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }
    
    /**
     * 画像ファイルをサーバーに保存し、そのパスを返すメソッド
     * @param imageData バイト配列形式の画像データ
     * @param fileName 保存するファイル名
     * @return 保存先のファイルパス
     * @throws IOException 入出力例外
     */
    private String saveImageToFileSystem(byte[] imageData, String fileName) throws IOException {
        String uploadDir = "/path/to/uploads"; // 保存先ディレクトリ
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

    /**
     * アーティストグループを新規作成し、成功時にそのIDを返すメソッド
     * @param artistGroup 新規作成するアーティストグループ
     * @param members グループに属するメンバーリスト
     * @return 作成されたグループのID、エラーの場合は-1
     */
    public int createAndReturnId(Artist_group artistGroup, List<Member> members) {
        System.out.println("[createAndReturnId] Start creating artist group with UserID: " + artistGroup.getUser_id());
        String sql = "INSERT INTO artist_group (user_id, account_name, picture_image_movie, group_genre, band_years, create_date, update_date, rating_star) "
                   + "VALUES (?, ?, ?, ?, ?, NOW(), NOW(), ?)";
        try (Connection conn = dbManager.getConnection()) {
            conn.setAutoCommit(false); // トランザクション開始
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, artistGroup.getUser_id());
                pstmt.setString(2, artistGroup.getAccount_name());
                pstmt.setString(3, artistGroup.getPicture_image_movie());
                pstmt.setString(4, artistGroup.getGroup_genre());
                pstmt.setInt(5, artistGroup.getBand_years());
                pstmt.setString(6, artistGroup.getRating_star());
                int affectedRows = pstmt.executeUpdate();
                System.out.println("[createAndReturnId] Rows affected: " + affectedRows);

                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int groupId = rs.getInt(1);
                            System.out.println("[createAndReturnId] Generated GroupID: " + groupId);

                            // 同じConnectionを使ってメンバーを挿入
                            Member_tableDAO memberDAO = new Member_tableDAO(dbManager);
                            memberDAO.insertMembers(groupId, members);

                            conn.commit(); // 成功時にコミット
                            System.out.println("[createAndReturnId] Transaction committed successfully.");
                            return groupId;
            int affectedRows = pstmt.executeUpdate();

            // 挿入成功後、生成されたIDを取得
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int groupId = rs.getInt(1);

                        // メンバーをデータベースに挿入
                        Member_tableDAO memberDAO = new Member_tableDAO(dbManager);
                        for (Member member : members) {
                            member.setArtist_group_id(groupId); // グループIDを設定
                            memberDAO.insertMember(member);
                        }
                    }
                }
        } catch (SQLException e) {
            System.err.println("[createAndReturnId] Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // エラー時
    }

    // アーティストグループを更新するメソッド
    public boolean updateArtistGroupByUserId(int userId, Artist_group updatedGroup) {
        System.out.println("[updateArtistGroupByUserId] Updating artist group for UserID: " + userId);
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
            System.out.println("[updateArtistGroupByUserId] Rows affected: " + rowsUpdated);
            return rowsUpdated > 0; // 更新が成功した場合はtrueを返す
        } catch (SQLException e) {
            System.err.println("[updateArtistGroupByUserId] Error updating artist group: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // エラーが発生した場合はfalse
    }

    // user_id に紐づくアーティストグループを取得するメソッド
    public Artist_group getGroupByUserId(int userId) {
        System.out.println("[getGroupByUserId] Retrieving artist group for UserID: " + userId);
        String sql = "SELECT * FROM artist_group WHERE user_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Artist_group group = rs2model(rs);
                System.out.println("[getGroupByUserId] Retrieved artist group: " + group);
                return group;
            }
        } catch (SQLException e) {
            System.err.println("[getGroupByUserId] Error retrieving artist group: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // IDでアーティストグループを取得するメソッド
    public Artist_group getArtistGroupById(int id) {
        System.out.println("[getArtistGroupById] Retrieving artist group for ID: " + id);
        String sql = "SELECT * FROM artist_group WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Artist_group group = rs2model(rs);
                System.out.println("[getArtistGroupById] Retrieved artist group: " + group);
                return group;
            }
        } catch (SQLException e) {
            System.err.println("[getArtistGroupById] Error retrieving artist group: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ResultSetからArtist_groupオブジェクトを作成するメソッド
    /**
     * 指定したユーザーIDに関連付けられたグループリストを取得
     * @param userId ユーザーID
     * @return アーティストグループのリスト
     */
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

    /**
     * アーティストグループの情報を更新
     * @param group 更新対象のアーティストグループ
     * @return 更新成功時はtrue、それ以外はfalse
     */
    public boolean updateGroup(Artist_group group) {
        String sql = "UPDATE artist_group SET account_name = ?, picture_image_movie = ?, group_genre = ?, band_years = ?, update_date = ? WHERE user_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, group.getAccount_name());
            pstmt.setString(2, group.getPicture_image_movie());
            pstmt.setString(3, group.getGroup_genre());
            pstmt.setInt(4, group.getBand_years());
            pstmt.setDate(5, Date.valueOf(group.getUpdate_date()));
            pstmt.setInt(6, group.getUser_id());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * グループIDを指定して画像パスを取得
     * @param id グループID
     * @return 画像の保存パス、エラー時はnull
     */
    public String getImagePathById(int id) {
        String sql = "SELECT picture_image_movie FROM artist_group WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("picture_image_movie");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ResultSetからArtist_groupオブジェクトを生成するメソッド
     * @param rs データベースから取得したResultSet
     * @return Artist_groupオブジェクト
     * @throws SQLException SQL例外
     */
    private Artist_group rs2model(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int user_id = rs.getInt("user_id");
        String account_name = rs.getString("account_name");
        String picture_image_movie = rs.getString("picture_image_movie");
        Date create_date = rs.getDate("create_date");
        Date update_date = rs.getDate("update_date");
        String rating_star = rs.getString("rating_star");
        String group_genre = rs.getString("group_genre");
        int band_years = rs.getInt("band_years");

        System.out.println("[rs2model] Mapping ResultSet to Artist_group: ID: " + id + ", UserID: " + user_id);
        return new Artist_group(
                id,
                user_id,
                account_name,
                picture_image_movie,
                group_genre,
                band_years,
                create_date != null ? create_date.toLocalDate() : null,
                update_date != null ? update_date.toLocalDate() : null,
                rating_star
        );
    }
}