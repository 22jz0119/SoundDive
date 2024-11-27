package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Artist_group;
import model.Member;

public class Artist_groupDAO {
    private static Artist_groupDAO instance; // シングルトンインスタンス
    private DBManager dbManager;

    // キャッシュ（キーはuserIdやgroupId）
    private static final Map<Integer, Artist_group> groupCacheByUserId = new HashMap<>();
    private static final Map<Integer, Artist_group> groupCacheById = new HashMap<>();

    // コンストラクタはプライベートにして外部からの直接インスタンス化を防ぐ
    private Artist_groupDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    // シングルトンインスタンスを取得するメソッド
    public static synchronized Artist_groupDAO getInstance(DBManager dbManager) {
        if (instance == null) {
            instance = new Artist_groupDAO(dbManager);
        }
        return instance;
    }

    // アーティストグループを新規作成し、成功時にIDを返すメソッド
    public int createAndReturnId(Artist_group artistGroup, List<Member> members) {
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

                if (affectedRows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int groupId = rs.getInt(1);

                            // 同じConnectionを使ってメンバーを挿入
                            Member_tableDAO memberDAO = Member_tableDAO.getInstance(dbManager); // Member_tableDAOもシングルトンにするべき
                            memberDAO.insertMembers(groupId, members);

                            conn.commit(); // 成功時にコミット

                            // キャッシュに追加
                            artistGroup.setId(groupId);
                            addToCache(artistGroup);
                            return groupId;
                        }
                    }
                }
                conn.rollback(); // 挿入失敗時にロールバック
            } catch (Exception e) {
                conn.rollback(); // エラー発生時にロールバック
                throw e; // エラーを再スロー
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // エラー時
    }

    // アーティストグループを更新するメソッド
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

            if (rowsUpdated > 0) {
                // キャッシュを更新
                addToCache(updatedGroup);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // エラーが発生した場合はfalse
    }

    // user_id に紐づくアーティストグループを取得するメソッド
    public Artist_group getGroupByUserId(int userId) {
        // キャッシュを確認
        if (groupCacheByUserId.containsKey(userId)) {
            return groupCacheByUserId.get(userId);
        }

        String sql = "SELECT * FROM artist_group WHERE user_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Artist_group group = rs2model(rs);
                addToCache(group);
                return group;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // キャッシュ操作メソッド
    private void addToCache(Artist_group group) {
        groupCacheByUserId.put(group.getUser_id(), group);
        groupCacheById.put(group.getId(), group);
    }

    public void clearCacheForUserId(int userId) {
        Artist_group group = groupCacheByUserId.remove(userId);
        if (group != null) {
            groupCacheById.remove(group.getId());
        }
    }

    public void clearAllCache() {
        groupCacheByUserId.clear();
        groupCacheById.clear();
    }

    // ResultSetからArtist_groupオブジェクトを作成するメソッド
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
