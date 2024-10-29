package model;

import java.sql.Timestamp;

public class User {
    private int id; // 主キー: INT
    private String login_id;
    private String name; // VARCHAR(255) と仮定
    private String password; // VARCHAR(255) と仮定
    private String telNumber; // BIGINT
    private String address; // VARCHAR(255) と仮定
    private Timestamp createDate; // DATETIME
    private Timestamp updateDate; // DATETIME

    // ゲッターとセッター
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreateDate() { // 修正：Timestamp型
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) { // 修正：Timestamp型
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() { // 修正：Timestamp型
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) { // 修正：Timestamp型
        this.updateDate = updateDate;
    }

    // コンストラクタ
    public User(int id, String login_id, String name, String password, String telNumber, String address, Timestamp createDate, Timestamp updateDate) {
        this.id = id;
        this.login_id = login_id;
        this.name = name;
        this.password = password;
        this.telNumber = telNumber;
        this.address = address;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
