package service;

import java.sql.SQLException;
import java.util.List;

import dao.DBManager;
import dao.NoticeDAO;
import model.Notice;

public class NotificationService {
    private NoticeDAO noticeDAO;

    public NotificationService(DBManager dbManager) {
        this.noticeDAO = NoticeDAO.getInstance(dbManager);
    }

    public void sendNotification(int applicationId, int userId, String message) {
        try {
            noticeDAO.insertNotice(applicationId, userId, message);
        } catch (SQLException e) {
            throw new RuntimeException("通知の送信に失敗しました", e);
        }
    }

    public List<Notice> getUserNotifications(int userId) {
        try {
            return noticeDAO.getNotificationsByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException("通知の取得に失敗しました", e);
        }
    }
    
    public List<Notice> getNotificationsByUserId(int userId) {
        return getUserNotifications(userId); // 既存のメソッドを呼び出す
    }


    public void markAsRead(int noticeId) {
        try {
            noticeDAO.markNotificationAsRead(noticeId);
        } catch (SQLException e) {
            throw new RuntimeException("通知の更新に失敗しました", e);
        }
    }
}
