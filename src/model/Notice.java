package model;

import java.time.LocalDateTime;

public class Notice {
    private int id;
    private int livehouseApplicationId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String message; // MySQLのTEXT型に対応
    private boolean isRead; // MySQLのtinyint(1)型に対応
    private int userId;

    // デフォルトコンストラクタ
    public Notice() {}

    // コンストラクタ
    public Notice(int id, int livehouseApplicationId, LocalDateTime createDate, LocalDateTime updateDate, String message, boolean isRead, int userId) {
        this.id = id;
        this.livehouseApplicationId = livehouseApplicationId;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.message = message;
        this.isRead = isRead;
        this.userId = userId;
    }

    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivehouseApplicationId() {
        return livehouseApplicationId;
    }

    public void setLivehouseApplicationId(int livehouseApplicationId) {
        this.livehouseApplicationId = livehouseApplicationId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
