package model;

import java.time.LocalDate;

public class Notice {
    private int id;
    private int livehouse_application_id;
    private LocalDate create_date;
    private LocalDate update_date;
    private String message; // MySQLのTEXT型に対応
    private boolean is_approved;

    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivehouse_application_id() {
        return livehouse_application_id;
    }

    public void setLivehouse_application_id(int livehouse_application_id) {
        this.livehouse_application_id = livehouse_application_id;
    }

    public LocalDate getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDate create_date) {
        this.create_date = create_date;
    }

    public LocalDate getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(LocalDate update_date) {
        this.update_date = update_date;
    }

    public String getMessage() { // MySQL TEXT型に対応
        return message;
    }

    public void setMessage(String message) { // MySQL TEXT型に対応
        this.message = message;
    }

    public boolean isApproved() { // メソッド名を一般的な形式に
        return is_approved;
    }

    public void setApproved(boolean is_approved) {
        this.is_approved = is_approved;
    }

    // コンストラクタ
    public Notice(int id, int livehouse_application_id, LocalDate create_date, LocalDate update_date, String message, boolean is_approved) {
        this.id = id;
        this.livehouse_application_id = livehouse_application_id;
        this.create_date = create_date;
        this.update_date = update_date;
        this.message = message; // MySQL TEXT型に対応
        this.is_approved = is_approved;
    }
}
